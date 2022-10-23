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
    private MerchantProperties merchantProperties;

    private List<Transaction> transactions = new ArrayList<>();
    @Value("${file.input.desjardinsInfiniteJson}")
    private String fileInputInfinite;
    @Value("${file.input.desjardinsWorldJson}")
    private String fileInputWorld;

    @Value("${desjardins.reportStartYear}")
    private int reportStartYear;

    private int lastMonth;
    private int lastDay;
    private final List<String> accounts = Arrays.asList("Nadine", "Jacques", "Juliette", "Gabrielle");
    private int accountIdx = 0;
    private Set<String> unmatchedLabels = new HashSet<>();
    private PosteDepense posteDepense;

    public DesjardinsJsonProcessor(PosteDepense posteDepense) {
        this.posteDepense = posteDepense;
    }

    public TransactionReport process(String carte) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Jdk8Module());

            Resource resource = new ClassPathResource(fileInputInfinite);
            File file = null;
            try {
                file = resource.getFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RapportVisa rapportVisa = mapper.readValue(file, RapportVisa.class);

            // print book
            log.info("Transactions autorisees: " + rapportVisa.getSectionAutorisee().getTransactionListe().size());
            log.info("Transactions facturees: " + rapportVisa.getSectionFacturee().getTransactionListe().size());

            List<TransactionFacturee> facturees = rapportVisa.getSectionFacturee().getTransactionListe();
            for (TransactionFacturee facturee : facturees) {
                if (facturee.getDescriptionCourte().startsWith("PAIEMENT CAISSE")) {
                    continue;
                }
                Transaction transaction = new Transaction();
                Float montant = Float.parseFloat(facturee.getMontantDevise());
                if (montant < 0) {
                    transaction.setCredit(BigDecimal.valueOf(-1*montant).setScale(2, RoundingMode.CEILING));
                    transaction.setDebit(BigDecimal.ZERO);
                } else {
                    transaction.setCredit(BigDecimal.ZERO);
                    transaction.setDebit(BigDecimal.valueOf(montant).setScale(2, RoundingMode.CEILING));
                }
                transaction.setDate(LocalDate.parse(facturee.getDateTransaction().substring(0,10)));
                transaction.setInstitution("Desjardins");
                transaction.setCompte("VISA " + carte);
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

            log.info("Finished " + fileInputInfinite);
            try {
                Path path = Paths.get(fileInputInfinite.replace(".json", ".out"));
                System.out.println("Output file: " + path.toAbsolutePath());
                Files.write(path, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rapportVisa).getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            log.error("Exception", e);
        }


        TransactionReport transactionReport = new TransactionReport();
        transactionReport.setTransactions(transactions);
        transactionReport.setTotalDepensesFixes(0);
        transactionReport.setTotalDepensesIgnorees(0);
        float total = (float) transactions.stream().filter(o->"Variable".equals(o.getCategorie())).mapToDouble(o->o.getDebit().doubleValue()).sum();
        transactionReport.setTotalDepensesVariables(total);
        log.info("Unmatched labels Desjardins: " + unmatchedLabels);
        return transactionReport;
    }

}
