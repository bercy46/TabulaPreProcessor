package ca.jpti.SuiviBudget.Main;

import ca.jpti.SuiviBudget.Desjardins.DesjardinsJsonProcessor;
import ca.jpti.SuiviBudget.Desjardins.DesjardinsProcessor;
import ca.jpti.SuiviBudget.TD.TDProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@Slf4j
public class Main {
    private TDProcessor tdProcessor;
    private DesjardinsProcessor desjardinsProcessor;
    private DesjardinsJsonProcessor desjardinsJsonProcessor;
    @Value("${startDate}")
    private String startDate;
    @Value("${files.weeklyPostesDepensesReport}")
    private String weeklyPostesDepensesReport;
    @Value("${files.weeklyDetailedReport}")
    private String weeklyDetailedReport;
    @Value("${files.weeklySummaryReport}")
    private String weeklySummaryReport;
    @Value("${files.biweeklyPostesDepensesReport}")
    private String biweeklyPostesDepensesReport;
    @Value("${files.biweeklyDetailedReport}")
    private String biweeklyDetailedReport;
    @Value("${files.biweeklySummaryReport}")
    private String biweeklySummaryReport;
    @Value("${files.monthlyPostesDepensesReport}")
    private String monthlyPostesDepensesReport;
    @Value("${files.monthlyDetailedReport}")
    private String monthlyDetailedReport;
    @Value("${files.monthlySummaryReport}")
    private String monthlySummaryReport;


    public Main(TDProcessor tdProcessor, DesjardinsProcessor desjardinsProcessor, DesjardinsJsonProcessor desjardinsJsonProcessor) {
        this.tdProcessor = tdProcessor;
        this.desjardinsProcessor = desjardinsProcessor;
        this.desjardinsJsonProcessor = desjardinsJsonProcessor;
    }

    @PostConstruct
    public void process() {
        TransactionReport tdReport = tdProcessor.process();
//        TransactionReport desjardinsReport = desjardinsProcessor.process();
        TransactionReport desjardinsInfiniteReport = desjardinsJsonProcessor.process("Infinite");

        log.info("Transactions TD: " + tdReport);
//        log.info("Transactions Desjardins: " + desjardinsReport);
        log.info("Transactions Desjardins: " + desjardinsInfiniteReport);

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(tdReport.getTransactions());
        transactions.addAll(desjardinsInfiniteReport.getTransactions());

        creerRapportHebdoSommaire(transactions);
        creerRapportHebdoDetaille(transactions);
        creerRapportHebdoPostesDepenses(transactions);
        creerRapportBiHebdoSommaire(transactions);
        creerRapportBiHebdoDetaille(transactions);
        creerRapportBiHebdoPostesDepenses(transactions);
        creerRapportMensuelSommaire(transactions);
        creerRapportMensuelDetaille(transactions);
        creerRapportMensuelPostesDepenses(transactions);
    }

    private void creerRapportHebdoPostesDepenses(List<Transaction> transactions) {
        Set<PostesDepensesReport> postesDepensesReports = createPostesDepensesReportsWeekly(transactions);
        List<PostesDepensesReport> listPostesDepensesReports = new ArrayList<>(postesDepensesReports);
        Collections.reverse(listPostesDepensesReports);
        TotalPosteDepenseComparator comparator = new TotalPosteDepenseComparator();
        StringBuffer sb = new StringBuffer();
        sb.append("---------------------------------------------------------\n")
                .append("Période                 Poste de Dépenses         Montant\n")
                .append("---------------------------------------------------------\n");
        String currentPeriod = null;
        List<PostesDepensesReport> listePostesDepensesReport = new ArrayList<>();
        for (PostesDepensesReport postesDepensesReport : listPostesDepensesReports) {
            Collections.sort(postesDepensesReport.getTotauxPostesDepenses(), comparator);
            for (TotalPosteDepense totalPosteDepense : postesDepensesReport.getTotauxPostesDepenses()) {
                sb.append(String.format("%-24s", postesDepensesReport.getPeriod().equals(currentPeriod) ? "" : postesDepensesReport.getPeriod()))
                        .append(String.format("%-26s", totalPosteDepense.getPosteDepense()))
                        .append(String.format("%-9.02f", totalPosteDepense.getMontant()))
                        .append("\n");
                currentPeriod = postesDepensesReport.getPeriod();
            }
            sb.append("Total: ").append(postesDepensesReport.getTotal()).append("\n");
            sb.append("\n");
        }
        Path path = Paths.get(weeklyPostesDepensesReport);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportBiHebdoPostesDepenses(List<Transaction> transactions) {
        Set<PostesDepensesReport> postesDepensesReports = createPostesDepensesReportsBiWeekly(transactions);
        List<PostesDepensesReport> listPostesDepensesReports = new ArrayList<>(postesDepensesReports);
        Collections.reverse(listPostesDepensesReports);
        TotalPosteDepenseComparator comparator = new TotalPosteDepenseComparator();
        StringBuffer sb = new StringBuffer();
        sb.append("---------------------------------------------------------\n")
                .append("Période                 Poste de Dépenses         Montant\n")
                .append("---------------------------------------------------------\n");
        String currentPeriod = null;
        List<PostesDepensesReport> listePostesDepensesReport = new ArrayList<>();
        for (PostesDepensesReport postesDepensesReport : listPostesDepensesReports) {
            Collections.sort(postesDepensesReport.getTotauxPostesDepenses(), comparator);
            for (TotalPosteDepense totalPosteDepense : postesDepensesReport.getTotauxPostesDepenses()) {
                sb.append(String.format("%-24s", postesDepensesReport.getPeriod().equals(currentPeriod) ? "" : postesDepensesReport.getPeriod()))
                        .append(String.format("%-26s", totalPosteDepense.getPosteDepense()))
                        .append(String.format("%-9.02f", totalPosteDepense.getMontant()))
                        .append("\n");
                currentPeriod = postesDepensesReport.getPeriod();
            }
            sb.append("Total: ").append(postesDepensesReport.getTotal()).append("\n");
            sb.append("\n");
        }
        Path path = Paths.get(biweeklyPostesDepensesReport);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportMensuelPostesDepenses(List<Transaction> transactions) {
        Set<PostesDepensesReport> postesDepensesReports = createPostesDepensesReportsMonthly(transactions);
        List<PostesDepensesReport> listPostesDepensesReports = new ArrayList<>(postesDepensesReports);
        Collections.reverse(listPostesDepensesReports);
        TotalPosteDepenseComparator comparator = new TotalPosteDepenseComparator();
        StringBuffer sb = new StringBuffer();
        sb.append("-----------------------------------------------------------\n")
                .append("Période                 Poste de Dépenses         Montant\n")
                .append("-----------------------------------------------------------\n");
        String currentPeriod = null;
        List<PostesDepensesReport> listePostesDepensesReport = new ArrayList<>();
        for (PostesDepensesReport postesDepensesReport : listPostesDepensesReports) {
            Collections.sort(postesDepensesReport.getTotauxPostesDepenses(), comparator);
            for (TotalPosteDepense totalPosteDepense : postesDepensesReport.getTotauxPostesDepenses()) {
                sb.append(String.format("%-24s", postesDepensesReport.getPeriod().equals(currentPeriod) ? "" : postesDepensesReport.getPeriod()))
                        .append(String.format("%-26s", totalPosteDepense.getPosteDepense()))
                        .append(String.format("%-17.02f", totalPosteDepense.getMontant()))
                        .append("\n");
                currentPeriod = postesDepensesReport.getPeriod();
            }
            sb.append("Total: ").append(postesDepensesReport.getTotal()).append("\n");
            sb.append("\n");
        }
        Path path = Paths.get(monthlyPostesDepensesReport);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportHebdoDetaille(List<Transaction> transactions) {
        Set<DetailedReport> detailedReports = createWeeklyReports(transactions);
        List<DetailedReport> listDetailedReports = new ArrayList<>(detailedReports);
        Collections.reverse(listDetailedReports);
        StringBuffer sb = new StringBuffer();
        for (DetailedReport report : listDetailedReports) {
            sb.append("\nPériode: ")
                    .append(report.getPeriod())
                    .append(" - Dépenses fixes: ")
                    .append(report.getTransactionReport().getTotalDepensesFixes())
                    .append(" - Dépenses variables: ")
                    .append(report.getTransactionReport().getTotalDepensesVariables())
                    .append("\n")
                    .append(tableauDepenses(report.getTransactionReport().getTransactions()));
        }
        Path path = Paths.get(weeklyDetailedReport);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportBiHebdoDetaille(List<Transaction> transactions) {
        Set<DetailedReport> detailedReports = createBiWeeklyReports(transactions);
        List<DetailedReport> listDetailedReports = new ArrayList<>(detailedReports);
        Collections.reverse(listDetailedReports);
        StringBuffer sb = new StringBuffer();
        for (DetailedReport report : listDetailedReports) {
            sb.append("\nPériode: ")
                    .append(report.getPeriod())
                    .append(" - Dépenses fixes: ")
                    .append(report.getTransactionReport().getTotalDepensesFixes())
                    .append(" - Dépenses variables: ")
                    .append(report.getTransactionReport().getTotalDepensesVariables())
                    .append("\n")
                    .append(tableauDepenses(report.getTransactionReport().getTransactions()));
        }
        Path path = Paths.get(biweeklyDetailedReport);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportMensuelDetaille(List<Transaction> transactions) {
        Set<MonthlyReport> monthlyReports = createMonthlyReports(transactions);
        List<MonthlyReport> listMonthlyReports = new ArrayList<>(monthlyReports);
        Collections.reverse(listMonthlyReports);
        StringBuffer sb = new StringBuffer();
        for (MonthlyReport report : listMonthlyReports) {
            sb.append("\nPériode: ")
                    .append(report.getPeriod())
                    .append(" - Dépenses fixes: ")
                    .append(report.getTransactionReport().getTotalDepensesFixes())
                    .append(" - Dépenses variables: ")
                    .append(report.getTransactionReport().getTotalDepensesVariables())
                    .append("\n")
                    .append(tableauDepenses(report.getTransactionReport().getTransactions()));
        }
        Path path = Paths.get(monthlyDetailedReport);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void creerRapportHebdoSommaire(List<Transaction> transactions) {
        Set<DetailedReport> detailedReports = createWeeklyReports(transactions);
        List<DetailedReport> listDetailedReports = new ArrayList<>(detailedReports);
        Collections.reverse(listDetailedReports);
        StringBuffer sb = new StringBuffer();
        sb.append("-----------------------------------------------------------\n")
                .append("Période                 Dépenses fixes   Dépenses variables\n")
                .append("-----------------------------------------------------------\n");
        for (DetailedReport report : listDetailedReports) {
            sb.append(String.format("%-24s", report.getPeriod()))
                    .append(String.format("%-17.02f", report.getTransactionReport().getTotalDepensesFixes()))
                    .append(String.format("%-17.02f", report.getTransactionReport().getTotalDepensesVariables()))
                    .append("\n");
        }
        Path path = Paths.get(weeklySummaryReport);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportBiHebdoSommaire(List<Transaction> transactions) {
        Set<DetailedReport> detailedReports = createBiWeeklyReports(transactions);
        List<DetailedReport> listDetailedReports = new ArrayList<>(detailedReports);
        Collections.reverse(listDetailedReports);
        StringBuffer sb = new StringBuffer();
        sb.append("-----------------------------------------------------------\n")
                .append("Période                 Dépenses fixes   Dépenses variables\n")
                .append("-----------------------------------------------------------\n");
        for (DetailedReport report : listDetailedReports) {
            sb.append(String.format("%-24s", report.getPeriod()))
                    .append(String.format("%-17.02f", report.getTransactionReport().getTotalDepensesFixes()))
                    .append(String.format("%-17.02f", report.getTransactionReport().getTotalDepensesVariables()))
                    .append("\n");
        }
        Path path = Paths.get(biweeklySummaryReport);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportMensuelSommaire(List<Transaction> transactions) {
        Set<MonthlyReport> monthlyReports = createMonthlyReports(transactions);
        List<MonthlyReport> listMonthlyReports = new ArrayList<>(monthlyReports);
        Collections.reverse(listMonthlyReports);
        StringBuffer sb = new StringBuffer();
        sb.append("-----------------------------------------------------------\n")
                .append("Période                 Dépenses fixes   Dépenses variables\n")
                .append("-----------------------------------------------------------\n");
        for (MonthlyReport report : listMonthlyReports) {
            sb.append(String.format("%-24s", report.getPeriod()))
                    .append(String.format("%-17.02f", report.getTransactionReport().getTotalDepensesFixes()))
                    .append(String.format("%-17.02f", report.getTransactionReport().getTotalDepensesVariables()))
                    .append("\n");
        }
        Path path = Paths.get(monthlySummaryReport);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String tableauDepenses(List<Transaction> transactions) {
        String tableau = "-----------------------------------------------------------------------------------------------------------\n";
        tableau += "Date       V/F Description                                Debit    Credit   Compte        Poste de dépenses\n";
        tableau += "-----------------------------------------------------------------------------------------------------------\n";
        String[] types = new String[]{"Fixe", "Variable"};
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String type : types) {
            Collections.sort(transactions);
            for (Transaction transaction : transactions) {
                if (type.equals(transaction.getCategorie())) {
                    tableau += String.format("%-11s", transaction.getDate().format(dateTimeFormatter));
                    tableau += type.substring(0, 1) + "   ";
                    tableau += String.format("%-43s", transaction.getDescription());
                    tableau += String.format("%-9.2f", transaction.getDebit().doubleValue());
                    tableau += String.format("%-9.2f", transaction.getCredit().doubleValue());
                    tableau += String.format("%-14s", transaction.getCompte());
                    tableau += transaction.getPosteDepense() + "\n";
                }
            }
        }
        return tableau;
    }

    private Set<DetailedReport> createWeeklyReports(List<Transaction> transactions) {
        Set<DetailedReport> detailedReports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String period = getWeeklyPeriod(transaction.getDate());
            DetailedReport report = null;
            for (DetailedReport detailedReport : detailedReports) {
                if (detailedReport.getPeriod().equals(period)) {
                    report = detailedReport;
                    detailedReport.getTransactionReport().getTransactions().add(transaction);
                    break;
                }
            }
            if (report == null) {
                report = new DetailedReport();
                report.setPeriod(period);
                TransactionReport transactionReport = new TransactionReport();
                List<Transaction> transactionsForReport = new ArrayList<>();
                transactionsForReport.add(transaction);
                transactionReport.setTransactions(transactionsForReport);
                report.setTransactionReport(transactionReport);
                detailedReports.add(report);
            }
        }

        for (DetailedReport detailedReport : detailedReports) {
            float total = (float) detailedReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Variable".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue() - o.getCredit().doubleValue())
                    .sum();
            detailedReport.getTransactionReport().setTotalDepensesVariables(total);
            total = (float) detailedReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Fixe".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue() - o.getCredit().doubleValue())
                    .sum();
            detailedReport.getTransactionReport().setTotalDepensesFixes(total);
        }
        return detailedReports;
    }

    private Set<DetailedReport> createBiWeeklyReports(List<Transaction> transactions) {
        Set<DetailedReport> detailedReports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String period = getBiWeeklyPeriod(transaction.getDate());
            DetailedReport report = null;
            for (DetailedReport detailedReport : detailedReports) {
                if (detailedReport.getPeriod().equals(period)) {
                    report = detailedReport;
                    detailedReport.getTransactionReport().getTransactions().add(transaction);
                    break;
                }
            }
            if (report == null) {
                report = new DetailedReport();
                report.setPeriod(period);
                TransactionReport transactionReport = new TransactionReport();
                List<Transaction> transactionsForReport = new ArrayList<>();
                transactionsForReport.add(transaction);
                transactionReport.setTransactions(transactionsForReport);
                report.setTransactionReport(transactionReport);
                detailedReports.add(report);
            }
        }

        for (DetailedReport detailedReport : detailedReports) {
            float total = (float) detailedReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Variable".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue() - o.getCredit().doubleValue())
                    .sum();
            detailedReport.getTransactionReport().setTotalDepensesVariables(total);
            total = (float) detailedReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Fixe".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue() - o.getCredit().doubleValue())
                    .sum();
            detailedReport.getTransactionReport().setTotalDepensesFixes(total);
        }
        return detailedReports;
    }

    private Set<PostesDepensesReport> createPostesDepensesReportsWeekly(List<Transaction> transactions) {
        Set<PostesDepensesReport> reports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String posteDepense = transaction.getPosteDepense();
            if (StringUtils.equals(transaction.getCategorie(), "Ignoree")
                    || StringUtils.equals(transaction.getCategorie(), "Fixe")
                    || posteDepense == null
                    || StringUtils.equals(posteDepense, "IGNORER")) {
//                log.info("Ignore cette transaction {}", transaction);
                continue;
            }
            String period = getWeeklyPeriod(transaction.getDate());
            PostesDepensesReport report = null;
            for (PostesDepensesReport postesDepensesReport : reports) {
                if (postesDepensesReport.getPeriod().equals(period)) {
                    report = postesDepensesReport;
                    break;
                }
            }
            if (report == null) {
                report = new PostesDepensesReport();
                report.setPeriod(period);
                reports.add(report);
            }

            BigDecimal montant = transaction.getDebit().subtract(transaction.getCredit());
            TotalPosteDepense totalPosteDepenseAjoute = null;
            for (TotalPosteDepense totalPosteDepense : report.getTotauxPostesDepenses()) {
                if (totalPosteDepense.getPosteDepense().equals(posteDepense)) {
                    totalPosteDepense.setMontant(totalPosteDepense.getMontant().add(montant));
                    totalPosteDepenseAjoute = totalPosteDepense;
                    break;
                }
            }

            if (totalPosteDepenseAjoute == null) {
                TotalPosteDepense totalPosteDepense = new TotalPosteDepense();
                totalPosteDepense.setPosteDepense(posteDepense);
                totalPosteDepense.setMontant(montant);
                report.getTotauxPostesDepenses().add(totalPosteDepense);
                totalPosteDepenseAjoute = totalPosteDepense;
            }

//            log.info("Ajoute ce totalPosteDepense {} pour transaction {}", totalPosteDepenseAjoute, transaction);
        }

        for (PostesDepensesReport report : reports) {
            for (TotalPosteDepense totalPosteDepense : report.getTotauxPostesDepenses()) {
                report.setTotal(report.getTotal().add(totalPosteDepense.getMontant()));
            }
        }

        return reports;
    }

    private Set<PostesDepensesReport> createPostesDepensesReportsBiWeekly(List<Transaction> transactions) {
        Set<PostesDepensesReport> reports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String posteDepense = transaction.getPosteDepense();
            if (StringUtils.equals(transaction.getCategorie(), "Ignoree")
                    || StringUtils.equals(transaction.getCategorie(), "Fixe")
                    || posteDepense == null
                    || StringUtils.equals(posteDepense, "IGNORER")) {
//                log.info("Ignore cette transaction {}", transaction);
                continue;
            }
            String period = getBiWeeklyPeriod(transaction.getDate());
            PostesDepensesReport report = null;
            for (PostesDepensesReport postesDepensesReport : reports) {
                if (postesDepensesReport.getPeriod().equals(period)) {
                    report = postesDepensesReport;
                    break;
                }
            }
            if (report == null) {
                report = new PostesDepensesReport();
                report.setPeriod(period);
                reports.add(report);
            }

            BigDecimal montant = transaction.getDebit().subtract(transaction.getCredit());
            TotalPosteDepense totalPosteDepenseAjoute = null;
            for (TotalPosteDepense totalPosteDepense : report.getTotauxPostesDepenses()) {
                if (totalPosteDepense.getPosteDepense().equals(posteDepense)) {
                    totalPosteDepense.setMontant(totalPosteDepense.getMontant().add(montant));
                    totalPosteDepenseAjoute = totalPosteDepense;
                    break;
                }
            }

            if (totalPosteDepenseAjoute == null) {
                TotalPosteDepense totalPosteDepense = new TotalPosteDepense();
                totalPosteDepense.setPosteDepense(posteDepense);
                totalPosteDepense.setMontant(montant);
                report.getTotauxPostesDepenses().add(totalPosteDepense);
                totalPosteDepenseAjoute = totalPosteDepense;
            }

//            log.info("Ajoute ce totalPosteDepense {} pour transaction {}", totalPosteDepenseAjoute, transaction);
        }

        for (PostesDepensesReport report : reports) {
            for (TotalPosteDepense totalPosteDepense : report.getTotauxPostesDepenses()) {
                report.setTotal(report.getTotal().add(totalPosteDepense.getMontant()));
            }
        }

        return reports;
    }

    private Set<PostesDepensesReport> createPostesDepensesReportsMonthly(List<Transaction> transactions) {
        Set<PostesDepensesReport> reports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String posteDepense = transaction.getPosteDepense();
            if (StringUtils.equals(transaction.getCategorie(), "Ignoree")
                    || StringUtils.equals(transaction.getCategorie(), "Fixe")
                    || posteDepense == null
                    || StringUtils.equals(posteDepense, "IGNORER")
            ) {
                continue;
            }
            String period = getMonthlyPeriod(transaction.getDate());
            PostesDepensesReport report = null;
            for (PostesDepensesReport postesDepensesReport : reports) {
                if (postesDepensesReport.getPeriod().equals(period)) {
                    report = postesDepensesReport;
                    break;
                }
            }
            if (report == null) {
                report = new PostesDepensesReport();
                report.setPeriod(period);
                reports.add(report);
            }

            BigDecimal montant = transaction.getDebit().subtract(transaction.getCredit());
            TotalPosteDepense totalPosteDepenseAjoute = null;
            for (TotalPosteDepense totalPosteDepense : report.getTotauxPostesDepenses()) {
                if (totalPosteDepense.getPosteDepense().equals(posteDepense)) {
                    totalPosteDepense.setMontant(totalPosteDepense.getMontant().add(montant));
                    totalPosteDepenseAjoute = totalPosteDepense;
                    break;
                }
            }

            if (totalPosteDepenseAjoute == null) {
                TotalPosteDepense totalPosteDepense = new TotalPosteDepense();
                totalPosteDepense.setPosteDepense(posteDepense);
                totalPosteDepense.setMontant(montant);
                report.getTotauxPostesDepenses().add(totalPosteDepense);
                totalPosteDepenseAjoute = totalPosteDepense;
            }
        }

        for (PostesDepensesReport report : reports) {
            for (TotalPosteDepense totalPosteDepense : report.getTotauxPostesDepenses()) {
                report.setTotal(report.getTotal().add(totalPosteDepense.getMontant()));
            }
        }

        return reports;
    }

    private Set<MonthlyReport> createMonthlyReports(List<Transaction> transactions) {
        Set<MonthlyReport> monthlyReports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String period = getMonthlyPeriod(transaction.getDate());
            MonthlyReport report = null;
            for (MonthlyReport monthlyReport : monthlyReports) {
                if (monthlyReport.getPeriod().equals(period)) {
                    report = monthlyReport;
                    monthlyReport.getTransactionReport().getTransactions().add(transaction);
                    break;
                }
            }
            if (report == null) {
                report = new MonthlyReport();
                report.setPeriod(period);
                TransactionReport transactionReport = new TransactionReport();
                List<Transaction> transactionsForReport = new ArrayList<>();
                transactionsForReport.add(transaction);
                transactionReport.setTransactions(transactionsForReport);
                report.setTransactionReport(transactionReport);
                monthlyReports.add(report);
            }
        }

        for (MonthlyReport monthlyReport : monthlyReports) {
            float total = (float) monthlyReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Variable".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue() - o.getCredit().doubleValue())
                    .sum();
            monthlyReport.getTransactionReport().setTotalDepensesVariables(total);
            total = (float) monthlyReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Fixe".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue() - o.getCredit().doubleValue())
                    .sum();
            monthlyReport.getTransactionReport().setTotalDepensesFixes(total);
        }
        return monthlyReports;
    }

    private String getWeeklyPeriod(LocalDate date) {
        LocalDate monday = date
                .with(
                        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
                );
        LocalDate sunday = monday
                .with(
                        TemporalAdjusters.next(DayOfWeek.SUNDAY)
                );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return monday.format(formatter) + " - " + sunday.format(formatter);
    }

    private String getBiWeeklyPeriod(LocalDate date) {
        LocalDate startDate = LocalDate.parse(this.startDate);
        long nbDaysDiff = DAYS.between(startDate, date);
        long nbTwoWeekPeriodDiff = nbDaysDiff / 14;
        long remainder = nbDaysDiff % 14;
        LocalDate periodStartDate = null;
        LocalDate periodEndDate = null;
        if (nbDaysDiff >= 0) {
            periodStartDate = startDate.plusDays(nbTwoWeekPeriodDiff * 14);
        } else {
            periodStartDate = startDate.minusDays(-1 * (nbTwoWeekPeriodDiff - 1) * 14);
        }
        periodEndDate = periodStartDate.plusDays(13);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return periodStartDate.format(formatter) + " - " + periodEndDate.format(formatter);
    }

    private String getMonthlyPeriod(LocalDate date) {
        LocalDate premier = date.withDayOfMonth(1);
        LocalDate dernier = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return premier.format(formatter) + " - " + dernier.format(formatter);
    }
}
