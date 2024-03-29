package ca.jpti.SuiviBudget.Desjardins;

import ca.jpti.SuiviBudget.Configuration.MerchantProperties;
import ca.jpti.SuiviBudget.Main.PosteDepense;
import ca.jpti.SuiviBudget.Main.Transaction;
import ca.jpti.SuiviBudget.Main.TransactionReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class DesjardinsJsonProcessor {
    private final List<String> accounts = Arrays.asList("Nadine", "Jacques", "Juliette", "Gabrielle");
    private MerchantProperties merchantProperties;
    private float totalAutorisees = 0;
    @Value("${file.input.desjardinsInfiniteJson}")
    private String fileInputInfinite;
    @Value("${file.input.desjardinsWorldJson}")
    private String fileInputWorld;
    @Value("${desjardins.reportStartYear}")
    private int reportStartYear;
    private int lastMonth;
    private int lastDay;
    private int accountIdx = 0;
    private Set<String> unmatchedLabels = new HashSet<>();
    private PosteDepense posteDepense;

    public DesjardinsJsonProcessor(PosteDepense posteDepense) {
        this.posteDepense = posteDepense;
    }

    public TransactionReport process(String carte) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());

            // Lire l'export le plus recent des transactions
            String fileToRead = null;
            if ("VISA Infinite".equals(carte)) {
                fileToRead = fileInputInfinite;
            } else {
                fileToRead = fileInputWorld;
            }
            Resource resource = new ClassPathResource(fileToRead);
            File file = null;
            try {
                file = resource.getFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RapportCC rapportCC = mapper.readValue(file, RapportCC.class);
            RapportCC rapportCCOld = null;

            // Si disponible, lire l'export precedent
            String fileToReadOld = fileToRead.replace(".json", ".old");
            resource = new ClassPathResource(fileToReadOld);
            try {
                file = resource.getFile();
                rapportCCOld = mapper.readValue(file, RapportCC.class);
            } catch (IOException e) {
                log.info("Pas de fichier old disponible");
            }

            if (rapportCCOld != null) {
                mergeFiles(rapportCC, rapportCCOld);
            }

            // print book
            log.info("Transactions autorisees: " + rapportCC.getSectionAutorisee().getTransactionListe().size());
            totalAutorisees = getTotalAutorisees(rapportCC.getSectionAutorisee().getTransactionListe());
            log.info("Transactions facturees: " + rapportCC.getSectionFacturee().getTransactionListe().size());

            List<TransactionFacturee> facturees = rapportCC.getSectionFacturee().getTransactionListe();
            for (TransactionFacturee facturee : facturees) {
                if (facturee.getDescriptionCourte().startsWith("PAIEMENT CAISSE")) {
                    continue;
                }
                Transaction transaction = new Transaction();
                Float montant = Float.parseFloat(facturee.getMontantDevise());
                if (montant < 0) {
                    transaction.setCredit(BigDecimal.valueOf(-1 * montant).setScale(2, RoundingMode.CEILING));
                    transaction.setDebit(BigDecimal.ZERO);
                } else {
                    transaction.setCredit(BigDecimal.ZERO);
                    transaction.setDebit(BigDecimal.valueOf(montant).setScale(2, RoundingMode.CEILING));
                }
                transaction.setDate(LocalDate.parse(facturee.getDateTransaction().substring(0, 10)));
                transaction.setInstitution("Desjardins");
                transaction.setCompte(carte);
                transaction.setDescription(facturee.getDescriptionCourte());
                transaction.setCategorie("Variable");
                facturee.setCategorie(Optional.of("Variable"));
                if (facturee.getPosteDepense() == null || StringUtils.isEmpty(facturee.getPosteDepense().get())) {
                    // si la source (facturee) n'a pas de poste de depense, l'obtenir, et mettre a jour la source
                    transaction.setPosteDepense(posteDepense.getPosteDepense(facturee.getDescriptionCourte(), transaction, unmatchedLabels));
                    facturee.setPosteDepense(Optional.of(transaction.getPosteDepense()));
                } else {
                    // si la source a un poste de depense, l'assigner a la transaction
                    transaction.setPosteDepense(facturee.getPosteDepense().get());
                }
                transactions.add(transaction);
            }

            log.info("Finished " + fileToRead);
            try {
                Path path = Paths.get(fileToRead.replace(".json", ".old"));
                System.out.println("Output file: " + path.toAbsolutePath());
                Files.write(path, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rapportCC).getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            log.error("Exception", e);
            throw e;
        }


        TransactionReport transactionReport = new TransactionReport();
        transactionReport.setTransactions(transactions);
        transactionReport.setTotalDepensesFixes(0);
        float totalVariables = (float) transactions.stream().filter(o -> "Variable".equals(o.getCategorie())).mapToDouble(o -> o.getDebit().doubleValue()).sum();
        float totalNonIgnorees = (float) transactions.stream().filter(o -> "Variable".equals(o.getCategorie()) && !"IGNORER".equalsIgnoreCase(o.getPosteDepense())).mapToDouble(o -> o.getDebit().doubleValue()).sum();
        transactionReport.setTotalDepensesVariables(totalNonIgnorees);
        float totalIgnorees = (float) transactions.stream().filter(o -> "IGNORER".equalsIgnoreCase(o.getPosteDepense())).mapToDouble(o -> o.getDebit().doubleValue()).sum();
        transactionReport.setTotalDepensesIgnorees(totalIgnorees);
        transactionReport.setTotalAutorisees(totalAutorisees);
        log.info("Unmatched labels Desjardins: " + unmatchedLabels);
        return transactionReport;
    }

    private float getTotalAutorisees(List<TransactionAutorisee> transactionAutorisees) {
        float totalAutorisees = 0;
        for (TransactionAutorisee transactionAutorisee : transactionAutorisees) {
            try {
                totalAutorisees += Float.parseFloat(transactionAutorisee.getMontantTransaction());
            } catch (NumberFormatException e) {
                log.error("Could not parse " + transactionAutorisee.getMontantTransaction());
            }
        }
        return totalAutorisees;
    }

    private void mergeFiles(RapportCC rapportCC, RapportCC rapportCCOld) {
        List<TransactionFacturee> facturees = rapportCC.getSectionFacturee().getTransactionListe();
        List<TransactionFacturee> factureesOld = rapportCCOld.getSectionFacturee().getTransactionListe();
        log.info("Avant le merge: facturees = {}, factureesOld = {}", facturees.size(), factureesOld.size());
        TransactionFactureeComparator comparator = new TransactionFactureeComparator();
        Collections.sort(facturees, comparator);
        Collections.sort(factureesOld, comparator);

        List<TransactionFacturee> factureesOldAAjouter = new ArrayList<>();

        for (TransactionFacturee transactionOld : factureesOld) {
            boolean found = false;
            for (TransactionFacturee transactionNew : facturees) {
                if (transactionOld.getNumeroSequence().equals(transactionNew.getNumeroSequence())) {
                    found = true;
                    if (transactionNew.getCategorie() == null || transactionNew.getCategorie().isEmpty()) {
                        transactionNew.setCategorie(transactionOld.getCategorie());
                    }
                    if (transactionNew.getPosteDepense() == null || transactionNew.getPosteDepense().isEmpty()) {
                        transactionNew.setPosteDepense(transactionOld.getPosteDepense());
                    }
                    break;
                }
            }
            if (!found) {
                factureesOldAAjouter.add(transactionOld);
            }
        }

//        TransactionFacturee derniereTransactionNouvelle = facturees.get(facturees.size() - 1);
//        for (int i = factureesOld.size() - 1; i >= 0; i--) {
//            TransactionFacturee factureeOld = factureesOld.get(i);
//            if (!StringUtils.equals(factureeOld.getIdentifiant(), derniereTransactionNouvelle.getIdentifiant())) {
//                factureesOldAAjouter.add(factureeOld);
//            } else {
//                break;
//            }
//        }
        log.info("{} transactions de l'export precedent a ajouter", factureesOldAAjouter.size());
        Collections.reverse(factureesOldAAjouter);

        List<TransactionFacturee> nouvelleListe = new ArrayList<>();
        nouvelleListe.addAll(facturees);
        nouvelleListe.addAll(factureesOldAAjouter);

        log.info("La nouvelle liste = {}", nouvelleListe.size());
        Collections.sort(nouvelleListe, comparator);

        rapportCC.getSectionFacturee().setTransactionListe(nouvelleListe);

    }
}
