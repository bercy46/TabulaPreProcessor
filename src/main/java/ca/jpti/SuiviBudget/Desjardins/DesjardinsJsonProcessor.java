package ca.jpti.SuiviBudget.Desjardins;

import ca.jpti.SuiviBudget.Configuration.MerchantProperties;
import ca.jpti.SuiviBudget.Main.PosteDepense;
import ca.jpti.SuiviBudget.Main.Transaction;
import ca.jpti.SuiviBudget.Main.TransactionReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class DesjardinsJsonProcessor {
    private MerchantProperties merchantProperties;

    private List<Transaction> transactions = new ArrayList<>();
    @Value("${file.input.desjardinsJson}")
    private String fileInput;

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

    public TransactionReport process() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource(fileInput);
            File file = null;
            try {
                file = resource.getFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RapportVisa rapportVisa = mapper.readValue(file, RapportVisa.class);

            // print book
            log.info("Transactions aurotirees: " + rapportVisa.getSectionAutorisee().getTransactionListe().size());
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
                transaction.setCompte("VISA");
                transaction.setDescription(facturee.getDescriptionCourte());
                transaction.setCategorie("Variable");
                transaction.setPosteDepense(posteDepense.getPosteDepense(facturee.getDescriptionCourte(), unmatchedLabels));
                transactions.add(transaction);
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
