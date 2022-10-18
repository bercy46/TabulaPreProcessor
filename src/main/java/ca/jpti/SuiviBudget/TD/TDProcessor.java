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
    private TDTransactionProperties tdTransactionProperties;
    private Scanner scanner = new Scanner(System.in);

    @Value("${file.input.td}")
    private List<String> fileInputs;
    @Value("${files.outputPath}")
    private String outputPath;
    @Value("${startDate}")
    private String startDate;

    List<Transaction> transactions = new ArrayList<>();
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
                Path path = Paths.get(filename.replace(".csv", ".out"));
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
        mapTotaux.put("Fixes", (float) transactions.stream().filter(o->"Fixe".equals(o.getCategorie())).mapToDouble(o->o.getCredit().subtract(o.getDebit()).doubleValue()).sum());
        mapTotaux.put("Variables", (float) transactions.stream().filter(o->"Variable".equals(o.getCategorie())).mapToDouble(o->o.getCredit().subtract(o.getDebit()).doubleValue()).sum());
        mapTotaux.put("Ignorees", (float) transactions.stream().filter(o->"Ignoree".equals(o.getCategorie())).mapToDouble(o->o.getCredit().subtract(o.getDebit()).doubleValue()).sum());
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
        if (tokens.length > 5) {
            transaction.setCategorie(tokens[5]);
        }
        if (tokens.length > 6) {
            transaction.setPosteDepense(tokens[6]);
        }
        transaction.setInstitution("TD");

        transaction.setCompte(filename.replace(".csv", "").replace("accountactivity", ""));

        String description = transaction.getDescription();

        if (StringUtils.isEmpty(transaction.getPosteDepense())) {
            transaction.setPosteDepense(posteDepense.getPosteDepense(description, unmatchedLabelsPosteDepense));
        }

        Set<String> matchKeys = tdTransactionProperties.getMatchRegex().keySet();
        Map<String, String> map = tdTransactionProperties.getMatchRegex();
        boolean matched = false;
        float debit = (float) transaction.getDebit().doubleValue();
        if (description.matches(".*Envoi.*")) {
            matched = true;
            if (debit == 75.0 || debit == 85.0 || debit == 110) {
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
                            transactions.add(transaction);
                        } else if (userInput.matches("^[vV].*")) {
                            if (StringUtils.isEmpty(transaction.getCategorie())) {
                                transaction.setCategorie("Variable");
                            }
                            transactions.add(transaction);
                        } else {
                            if (StringUtils.isEmpty(transaction.getCategorie())) {
                                transaction.setCategorie("Ignoree");
                            }
                            transactions.add(transaction);
                        }
                    }
                } else {
                    transactions.add(transaction);
                }
            }
        } else if (description.contains(" 6478799") || description.contains(" 6479221") || description.contains(" 3296586")) {
            // Transferts
            if (StringUtils.isEmpty(transaction.getCategorie())) {
                transaction.setCategorie("Ignoree");
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
                        transactions.add(transaction);
                    } else if ("VARIABLE".equals(map.get(key))) {
                        matched = true;
                        if (StringUtils.isEmpty(transaction.getCategorie())) {
                            transaction.setCategorie("Variable");
                        }
                        transactions.add(transaction);
                    } else if ("IGNORE".equals(map.get(key))) {
                        matched = true;
                        if (StringUtils.isEmpty(transaction.getCategorie())) {
                            transaction.setCategorie("Ignoree");
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
        if (tokens.length > 5) {
            return line;
        } else {
            return line + "," + transaction.getCategorie() + "," + (transaction.getPosteDepense() == null ? "" : transaction.getPosteDepense());
        }
    }
}
