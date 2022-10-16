package ca.jpti.SuiviBudget.Main;

import ca.jpti.SuiviBudget.Desjardins.DesjardinsJsonProcessor;
import ca.jpti.SuiviBudget.Desjardins.DesjardinsProcessor;
import ca.jpti.SuiviBudget.TD.TDProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    public Main(TDProcessor tdProcessor, DesjardinsProcessor desjardinsProcessor, DesjardinsJsonProcessor desjardinsJsonProcessor) {
        this.tdProcessor = tdProcessor;
        this.desjardinsProcessor = desjardinsProcessor;
        this.desjardinsJsonProcessor = desjardinsJsonProcessor;
    }

    @PostConstruct
    public void process() {
        TransactionReport tdReport = tdProcessor.process();
//        TransactionReport desjardinsReport = desjardinsProcessor.process();
        TransactionReport desjardinsReport = desjardinsJsonProcessor.process();

        log.info("Transactions TD: " + tdReport);
//        log.info("Transactions Desjardins: " + desjardinsReport);
        log.info("Transactions Desjardins: " + desjardinsReport);

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(tdReport.getTransactions());
        transactions.addAll(desjardinsReport.getTransactions());

        Set<WeeklyReport> weeklyReports = createWeeklyReports(transactions);

        for (WeeklyReport report : weeklyReports) {
            log.info("\nPériode: {} - Dépenses fixes: {} - Dépenses variables: {}\n{}",
                    report.getPeriod(),
                    report.getTransactionReport().getTotalDepensesFixes(),
                    report.getTransactionReport().getTotalDepensesVariables(),
                    tableauDepenses(report.getTransactionReport().getTransactions()));
        }
    }

    private String tableauDepenses(List<Transaction> transactions) {
        String tableau = "-----------------------------------------------------------------------------------------------------\n";
        tableau +=       "Date       V/F Description                                Debit    Credit   Compte  Poste de dépenses\n";
        tableau +=       "-----------------------------------------------------------------------------------------------------\n";
        String[] types = new String[]{"Fixe","Variable"};
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String type : types) {
            Collections.sort(transactions);
            for (Transaction transaction : transactions) {
                if (type.equals(transaction.getCategorie())) {
                    tableau += String.format("%-11s", transaction.getDate().format(dateTimeFormatter));
                    tableau += type.substring(0,1)+"   ";
                    tableau += String.format("%-43s", transaction.getDescription());
                    tableau += String.format("%-9.2f", transaction.getDebit().doubleValue());
                    tableau += String.format("%-9.2f", transaction.getCredit().doubleValue());
                    tableau += String.format("%-8s", transaction.getCompte());
                    tableau += transaction.getPosteDepense() + "\n";
                }
            }
        }
        return tableau;
    }

    private Set<WeeklyReport> createWeeklyReports(List<Transaction> transactions) {
        Set<WeeklyReport> weeklyReports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String period = getPeriod(transaction.getDate());
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
            float total = (float) weeklyReport.getTransactionReport().getTransactions().stream().filter(o->"Variable".equals(o.getCategorie())).mapToDouble(o->o.getDebit().doubleValue()).sum();
            weeklyReport.getTransactionReport().setTotalDepensesVariables(total);
            total = (float) weeklyReport.getTransactionReport().getTransactions().stream().filter(o->"Fixe".equals(o.getCategorie())).mapToDouble(o->o.getDebit().doubleValue()).sum();
            weeklyReport.getTransactionReport().setTotalDepensesFixes(total);
        }
        return weeklyReports;
    }

    private String getPeriod(LocalDate date) {
        LocalDate monday = date
                .with(
                        TemporalAdjusters.previousOrSame( DayOfWeek.MONDAY )
                ) ;
        LocalDate sunday = monday
                .with(
                        TemporalAdjusters.next( DayOfWeek.SUNDAY )
                ) ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return monday.format(formatter)+" - "+sunday.format(formatter);
    }
}
