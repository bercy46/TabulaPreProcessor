package ca.jpti.SuiviBudget.TD;

import ca.jpti.SuiviBudget.Main.Transaction;
import ca.jpti.SuiviBudget.Main.TransactionReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
@Slf4j
public class TDProcessor {
    private TDTransactionProperties tdTransactionProperties;
    private Scanner scanner = new Scanner(System.in);

    @Value("${file.input.td}")
    private List<String> fileInputs;
    @Value("${startDate}")
    private String startDate;

    List<Transaction> transactions = new ArrayList<>();
    private Set<String> unmatchedLabels = new HashSet<>();

    public TDProcessor(TDTransactionProperties tdTransactionProperties) {
        this.tdTransactionProperties = tdTransactionProperties;
    }

    public TransactionReport process() {
        for (String filename : fileInputs) {
            Resource resource = new ClassPathResource(filename);
            File file = null;
            try {
                file = resource.getFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            StringBuffer sb = new StringBuffer();
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line == null) break;
                    String output = processLine(line, filename);
                    if (output != null)
                        sb.append(output).append("\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("Finished " + filename);
        }
        log.info("Transactions: " + transactions);
        log.info("Unmatched labels: " + unmatchedLabels);
        Map<String, Float> mapTotaux = new HashMap<>();
        mapTotaux.put("Fixes", (float) transactions.stream().filter(o->"Fixe".equals(o.getCategorie())).mapToDouble(o->o.getCredit()-o.getDebit()).sum());
        mapTotaux.put("Variables", (float) transactions.stream().filter(o->"Variable".equals(o.getCategorie())).mapToDouble(o->o.getCredit()-o.getDebit()).sum());
        mapTotaux.put("Ignorees", (float) transactions.stream().filter(o->"Ignoree".equals(o.getCategorie())).mapToDouble(o->o.getCredit()-o.getDebit()).sum());
        log.info("Totaux: " + mapTotaux);
        TransactionReport transactionReport = new TransactionReport();
        transactionReport.setTransactions(transactions);
        transactionReport.setTotalDepensesFixes(mapTotaux.get("Fixes"));
        transactionReport.setTotalDepensesVariables(mapTotaux.get("Variables"));
        transactionReport.setTotalDepensesIgnorees(mapTotaux.get("Ignorees"));
        return transactionReport;
    }

    private String processLine(String line, String filename) {
        String[] tokens = line.split(",");

        Transaction transaction = Transaction.fromTokens(tokens);
        transaction.setInstitution("TD");

        transaction.setCompte(filename.replace(".csv", "").replace("accountactivity", ""));

        Set<String> matchKeys = tdTransactionProperties.getMatchRegex().keySet();
        Map<String, String> map = tdTransactionProperties.getMatchRegex();
        boolean matched = false;
        String description = transaction.getDescription();
        float debit = transaction.getDebit();
        if (description.matches(".*Envoi.*")) {
            matched = true;
            if (debit == 75.0 || debit == 85.0 || debit == 110) {
                transaction.setCategorie("Fixe");
                transactions.add(transaction);
            } else {
                System.out.print("SVP vérifier cette transaction: " + transaction.toString() + " (F/V/I): ");
                String userInput = null;
                while (userInput == null || !(userInput.matches("^[fFvViI].*"))) {
                    userInput = scanner.next();
                    if (userInput.matches("^[fF].*")) {
                        transaction.setCategorie("Fixe");
                        transactions.add(transaction);
                    } else if (userInput.matches("^[vV].*")) {
                        transaction.setCategorie("Variable");
                        transactions.add(transaction);
                    } else {
                        transaction.setCategorie("Ignoree");
                        transactions.add(transaction);
                    }
                }
            }
        } else if (description.contains(" 6478799") || description.contains(" 6479221") || description.contains(" 3296586")) {
            // Transferts
            transaction.setCategorie("Ignoree");
            transactions.add(transaction);
        } else {
            for (String key : tdTransactionProperties.getMatchRegex().keySet()) {
                String regex = ".*" + key + ".*";
                if (transaction.getDescription().matches(regex)) {
                    if ("FIXE".equals(map.get(key))) {
                        matched = true;
                        transaction.setCategorie("Fixe");
                        transactions.add(transaction);
                    } else if ("VARIABLE".equals(map.get(key))) {
                        matched = true;
                        transaction.setCategorie("Variable");
                        transactions.add(transaction);
                    } else if ("IGNORE".equals(map.get(key))) {
                        matched = true;
                        transaction.setCategorie("Ignoree");
                        transactions.add(transaction);
                    }
                    break;
                }
            }
        }
        if (!matched) {
            unmatchedLabels.add(transaction.getDescription());
        }
        return line;
    }
}
