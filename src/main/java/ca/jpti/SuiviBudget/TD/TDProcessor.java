package ca.jpti.SuiviBudget.TD;

import ca.jpti.SuiviBudget.Main.PosteDepense;
import ca.jpti.SuiviBudget.Main.Transaction;
import ca.jpti.SuiviBudget.Main.TransactionReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
@Slf4j
public class TDProcessor {
    List<Transaction> transactions = new ArrayList<>();
    private TDTransactionProperties tdTransactionProperties;
    private Scanner scanner = new Scanner(System.in);
    @Value("${file.input.td}")
    private List<String> fileInputs;
    @Value("${files.outputPath}")
    private String outputPath;
    @Value("${startDate}")
    private String startDate;
    private Set<String> unmatchedLabelsFVI = new HashSet<>();
    private Set<String> unmatchedLabelsPosteDepense = new HashSet<>();
    private PosteDepense posteDepense;

    public TDProcessor(TDTransactionProperties tdTransactionProperties, PosteDepense posteDepense) {
        this.tdTransactionProperties = tdTransactionProperties;
        this.posteDepense = posteDepense;
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
            try {
                Path path = Paths.get(filename);
                System.out.println("Output file: " + path.toAbsolutePath());
                Files.write(path, sb.toString().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Transactions: " + transactions);
        log.info("Unmatched labels FVI TD: " + unmatchedLabelsFVI);
        log.info("Unmatched labels posteDepense TD: " + unmatchedLabelsPosteDepense);
        Map<String, Float> mapTotaux = new HashMap<>();
        mapTotaux.put("Fixes", (float) transactions.stream().filter(o -> "Fixe".equals(o.getCategorie())).mapToDouble(o -> o.getCredit().subtract(o.getDebit()).doubleValue()).sum());
        mapTotaux.put("Variables", (float) transactions.stream().filter(o -> "Variable".equals(o.getCategorie())).mapToDouble(o -> o.getCredit().subtract(o.getDebit()).doubleValue()).sum());
        mapTotaux.put("IGNORER", (float) transactions.stream().filter(o -> "IGNORER".equals(o.getCategorie())).mapToDouble(o -> o.getCredit().subtract(o.getDebit()).doubleValue()).sum());
        log.info("Totaux: " + mapTotaux);
        TransactionReport transactionReport = new TransactionReport();
        transactionReport.setTransactions(transactions);
        transactionReport.setTotalDepensesFixes(mapTotaux.get("Fixes"));
        transactionReport.setTotalDepensesVariables(mapTotaux.get("Variables"));
        transactionReport.setTotalDepensesIgnorees(mapTotaux.get("IGNORER"));
        return transactionReport;
    }

    private String processLine(String line, String filename) {
        String[] tokens = line.split(",");

        Transaction transaction = Transaction.fromTokens(tokens);
        if (tokens.length > 5) {
            transaction.setCategorie(tokens[5]);
        }
        if (tokens.length > 6) {
            transaction.setPosteDepense(tokens[6]);
        }
        transaction.setInstitution("TD");

        transaction.setCompte(filename.replace(".csv", "").replace("accountactivity", ""));

        String description = transaction.getDescription();

        Set<String> matchKeys = tdTransactionProperties.getMatchRegex().keySet();
        Map<String, String> map = tdTransactionProperties.getMatchRegex();
        boolean matched = false;
        float debit = (float) transaction.getDebit().doubleValue();
        if (description.matches(".*Envoi.*")) {
            matched = true;
            if (debit == 85.0 || debit == 110) {
                if (StringUtils.isEmpty(transaction.getCategorie())) {
                    transaction.setCategorie("Fixe");
                }
                if (StringUtils.isEmpty(transaction.getPosteDepense())) {
                    transaction.setPosteDepense("Allocations filles");
                }
                transactions.add(transaction);
            } else {
                if (StringUtils.isEmpty(transaction.getCategorie())) {
                    System.out.print("SVP vérifier cette transaction: " + transaction.toString() + " (F/V/I): ");
                    String userInput = null;
                    while (userInput == null || !(userInput.matches("^[fFvViI].*"))) {
                        userInput = scanner.next();
                        if (userInput.matches("^[fF].*")) {
                            if (StringUtils.isEmpty(transaction.getCategorie())) {
                                transaction.setCategorie("Fixe");
                            }
                        } else if (userInput.matches("^[vV].*")) {
                            if (StringUtils.isEmpty(transaction.getCategorie())) {
                                transaction.setCategorie("Variable");
                            }
                        } else {
                            if (StringUtils.isEmpty(transaction.getCategorie())) {
                                transaction.setCategorie("IGNORER");
                            }
                        }
                    }
                    if (!"IGNORER".equals(transaction.getCategorie()) && StringUtils.isEmpty(transaction.getPosteDepense())) {
                        transaction.setPosteDepense(posteDepense.getPosteDepense(description, transaction, unmatchedLabelsPosteDepense));
                    }
                    transactions.add(transaction);


                } else {
                    if (!"IGNORER".equals(transaction.getCategorie()) && StringUtils.isEmpty(transaction.getPosteDepense())) {
                        transaction.setPosteDepense(posteDepense.getPosteDepense(description, transaction, unmatchedLabelsPosteDepense));
                    }
                    transactions.add(transaction);
                }
            }
        } else if (description.contains("TFR") && (description.contains(" 6478799") || description.contains(" 6479221") || description.contains(" 3296586"))) {
            // Transferts
            if (StringUtils.isEmpty(transaction.getCategorie())) {
                transaction.setCategorie("IGNORER");
            }
            transactions.add(transaction);
        } else {
            for (String key : tdTransactionProperties.getMatchRegex().keySet()) {
                String regex = ".*" + key + ".*";
                if (transaction.getDescription().matches(regex)) {
                    if ("FIXE".equals(map.get(key))) {
                        matched = true;
                        if (StringUtils.isEmpty(transaction.getCategorie())) {
                            transaction.setCategorie("Fixe");
                        }
                        if (StringUtils.isEmpty(transaction.getPosteDepense())) {
                            transaction.setPosteDepense(posteDepense.getPosteDepense(description, transaction, unmatchedLabelsPosteDepense));
                        }
                        transactions.add(transaction);
                    } else if ("VARIABLE".equals(map.get(key))) {
                        matched = true;
                        if (StringUtils.isEmpty(transaction.getCategorie())) {
                            transaction.setCategorie("Variable");
                        }
                        if (StringUtils.isEmpty(transaction.getPosteDepense())) {
                            transaction.setPosteDepense(posteDepense.getPosteDepense(description, transaction, unmatchedLabelsPosteDepense));
                        }
                        transactions.add(transaction);
                    } else if ("IGNORE".equals(map.get(key))) {
                        matched = true;
                        if (StringUtils.isEmpty(transaction.getCategorie())) {
                            transaction.setCategorie("IGNORER");
                        }
                        transactions.add(transaction);
                    }
                    break;
                }
            }
        }
        if (!matched) {
            unmatchedLabelsFVI.add(transaction.getDescription());
        }
        String returnedLine = line;

        if (returnedLine.endsWith(",")) {
            returnedLine = returnedLine.substring(0, returnedLine.length() - 1);
        }
        if (tokens.length == 5) {
            return line
                    + (transaction.getCategorie() == null ? "" : "," + transaction.getCategorie())
                    + (transaction.getPosteDepense() == null ? "" : "," + transaction.getPosteDepense());
        } else if (tokens.length == 6) {
            return line
                    + (transaction.getPosteDepense() == null ? "" : "," + transaction.getPosteDepense());
        }
        return line;
    }
}
