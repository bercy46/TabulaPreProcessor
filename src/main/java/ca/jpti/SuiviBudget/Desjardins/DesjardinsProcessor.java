package ca.jpti.SuiviBudget.Desjardins;

import ca.jpti.SuiviBudget.Configuration.DesjardinsMerchantProperties;
import ca.jpti.SuiviBudget.Main.Transaction;
import ca.jpti.SuiviBudget.Main.TransactionReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class DesjardinsProcessor {
    private DesjardinsMerchantProperties desjardinsMerchantProperties;

    private List<Transaction> transactions = new ArrayList<>();
    @Value("${file.input.desjardins}")
    private String fileInput;

    @Value("${desjardins.reportStartYear}")
    private int reportStartYear;

    private int lastMonth;
    private int lastDay;
    private final List<String> accounts = Arrays.asList("Nadine", "Jacques", "Juliette", "Gabrielle");
    private int accountIdx = 0;
    private Set<String> unmatchedLabels = new HashSet<>();

    public DesjardinsProcessor(DesjardinsMerchantProperties desjardinsMerchantProperties) {
        this.desjardinsMerchantProperties = desjardinsMerchantProperties;
    }
    public TransactionReport process() {
        Resource resource = new ClassPathResource(fileInput);
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
                String output = processLine(line);
                float total = (float) transactions.stream().filter(o->"Variable".equals(o.getCategorie())).mapToDouble(o->o.getCredit()-o.getDebit()).sum();
                log.info("line: " + line);
                log.info("total: " + total);
                if (output != null)
                    sb.append(output).append("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Transactions: " + sb.toString());
        log.info("Unmatched labels: " + unmatchedLabels);

        float total = (float) transactions.stream().filter(o->"Variable".equals(o.getCategorie())).mapToDouble(o->o.getCredit()-o.getDebit()).sum();
        TransactionReport transactionReport = new TransactionReport();
        transactionReport.setTransactions(transactions);
        transactionReport.setTotalDepensesVariables(total);
        return transactionReport;
    }

    private String processLine(String line) {
        try {
            if (line.startsWith("\""))
                return null;
            line = line.replaceAll("\"(\\d+)\\s+(\\d+)", "\"$1$2");
            line = line.replaceAll("\"(\\d),(\\d\\d)\\s(%)\"", "$1\\.$2$3");
            line = line.replaceAll("\"(\\d+),(\\d+)\"", "$1\\.$2");
            line = line.replaceAll("\"(\\d+),(\\d+)CR\"", "-$1\\.$2");
            String[] tokens = line.split(",");
//            int month = Integer.parseInt(tokens[1]);
//            int day = Integer.parseInt(tokens[0]);
//            int month2 = Integer.parseInt(tokens[3]);
//            int day2 = Integer.parseInt(tokens[2]);
            tokens = ArrayUtils.remove(tokens, 0);
            tokens = ArrayUtils.remove(tokens, 0);
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].matches(".*[a-zA-Z].*")
                        && tokens[i + 1].matches(".*[a-zA-Z].*")) {
                    tokens[i] = tokens[i] + tokens[i + 1];
                    tokens = ArrayUtils.remove(tokens, i + 1);
                    break;
                }
            }
            Set<String> matchKeys = desjardinsMerchantProperties.getMatchRegex().keySet();
            Map<String, String> map = desjardinsMerchantProperties.getMatchRegex();
            for (String key : desjardinsMerchantProperties.getMatchRegex().keySet()) {
                String regex = ".*" + key + ".*";
                if (tokens[2].matches(regex)) {
                    tokens[2] = map.get(key);
                }
            }
            String category = desjardinsMerchantProperties.getCategories().get(tokens[2]);
            if (category == null) {
                unmatchedLabels.add(tokens[2] + "\n");
                category = "";
            }

            // TODO: figure out how to infer whose account it is
            String account = "VISA";
//            String account = accounts.get(accountIdx);
//            if (Math.abs(month-month2) < 2 &&
//                    (month < lastMonth || month == lastMonth && day < lastDay || month == 12 && lastMonth == 1)) {
//                account = accounts.get(++accountIdx);
//            }

            line = tokens[0] + "," +
                    tokens[1] + "," +
                    account + "," +
                    tokens[2] + "," +
                    tokens[tokens.length - 2] + "," +
                    tokens[tokens.length - 1] + "," +
                    category;

//            lastMonth = Integer.parseInt(tokens[1]);
//            lastDay = Integer.parseInt(tokens[0]);

            int year = reportStartYear;

            Transaction transaction = new Transaction();
            transaction.setCategorie("Variable");
            transaction.setCompte(account);
            transaction.setInstitution("Desjardins");
            transaction.setDate(LocalDate.of(year, Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0])));
            transaction.setBalance(0);
            float amount = Float.parseFloat(tokens[tokens.length - 1]);
            if (amount < 0) {
                transaction.setCredit(-1 * amount);
                transaction.setDebit(0);
            } else {
                transaction.setDebit(amount);
                transaction.setCredit(0);
            }
            transaction.setDescription(tokens[2]);
            transaction.setPosteDepense(category);
            transactions.add(transaction);
        } catch (Exception e) {
            log.error("Exception for " + line, e);
        }
        return line;
    }
}
