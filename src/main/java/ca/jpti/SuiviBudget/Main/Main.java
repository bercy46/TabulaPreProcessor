package ca.jpti.SuiviBudget.Main;

import ca.jpti.SuiviBudget.Desjardins.DesjardinsJsonProcessor;
import ca.jpti.SuiviBudget.Desjardins.DesjardinsProcessor;
import ca.jpti.SuiviBudget.TD.TDProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Component
@Slf4j
public class Main {
    private TDProcessor tdProcessor;
    private DesjardinsProcessor desjardinsProcessor;
    private DesjardinsJsonProcessor desjardinsJsonProcessor;
    @Value("${files.weeklyDetailedReport}")
    private String weeklyDetailedReport;
    @Value("${files.weeklySummaryReport}")
    private String weeklySummaryReport;
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

        creerRapportHebdoDetaille(transactions);
        creerRapportHebdoSommaire(transactions);
        creerRapportMensuelSommaire(transactions);
    }

    private void creerRapportHebdoDetaille(List<Transaction> transactions) {
        Set<WeeklyReport> weeklyReports = createWeeklyReports(transactions);
        List<WeeklyReport> listWeeklyReports = new ArrayList<>(weeklyReports);
        Collections.reverse(listWeeklyReports);
        StringBuffer sb = new StringBuffer();
        for (WeeklyReport report : listWeeklyReports) {
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

    private void creerRapportHebdoSommaire(List<Transaction> transactions) {
        Set<WeeklyReport> weeklyReports = createWeeklyReports(transactions);
        List<WeeklyReport> listWeeklyReports = new ArrayList<>(weeklyReports);
        Collections.reverse(listWeeklyReports);
        StringBuffer sb = new StringBuffer();
        sb.append("-----------------------------------------------------------\n")
                .append("Période                 Dépenses fixes   Dépenses variables\n")
                .append("-----------------------------------------------------------\n");
        for (WeeklyReport report : listWeeklyReports) {
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

    private Set<WeeklyReport> createWeeklyReports(List<Transaction> transactions) {
        Set<WeeklyReport> weeklyReports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String period = getWeeklyPeriod(transaction.getDate());
            WeeklyReport report = null;
            for (WeeklyReport weeklyReport : weeklyReports) {
                if (weeklyReport.getPeriod().equals(period)) {
                    report = weeklyReport;
                    weeklyReport.getTransactionReport().getTransactions().add(transaction);
                    break;
                }
            }
            if (report == null) {
                report = new WeeklyReport();
                report.setPeriod(period);
                TransactionReport transactionReport = new TransactionReport();
                List<Transaction> transactionsForReport = new ArrayList<>();
                transactionsForReport.add(transaction);
                transactionReport.setTransactions(transactionsForReport);
                report.setTransactionReport(transactionReport);
                weeklyReports.add(report);
            }
        }

        for (WeeklyReport weeklyReport : weeklyReports) {
            float total = (float) weeklyReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Variable".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue())
                    .sum();
            weeklyReport.getTransactionReport().setTotalDepensesVariables(total);
            total = (float) weeklyReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Fixe".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue())
                    .sum();
            weeklyReport.getTransactionReport().setTotalDepensesFixes(total);
        }
        return weeklyReports;
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
                    .mapToDouble(o -> o.getDebit().doubleValue())
                    .sum();
            monthlyReport.getTransactionReport().setTotalDepensesVariables(total);
            total = (float) monthlyReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Fixe".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue())
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

    private String getMonthlyPeriod(LocalDate date) {
        LocalDate premier = date.withDayOfMonth(1);
        LocalDate dernier = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return premier.format(formatter) + " - " + dernier.format(formatter);
    }
}
