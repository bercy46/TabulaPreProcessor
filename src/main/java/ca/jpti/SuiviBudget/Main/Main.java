package ca.jpti.SuiviBudget.Main;

import ca.jpti.SuiviBudget.Desjardins.DesjardinsProcessor;
import ca.jpti.SuiviBudget.TD.TDProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Main {
    private TDProcessor tdProcessor;
    private DesjardinsProcessor desjardinsProcessor;

    public Main(TDProcessor tdProcessor, DesjardinsProcessor desjardinsProcessor) {
        this.tdProcessor = tdProcessor;
        this.desjardinsProcessor = desjardinsProcessor;
    }

    @PostConstruct
    public void process() {
        TransactionReport tdReport = tdProcessor.process();
        TransactionReport desjardinsReport = desjardinsProcessor.process();

        log.info("Transactions TD: " + tdReport);
        log.info("Transactions Desjardins: " + desjardinsReport);

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(tdReport.getTransactions());
        transactions.addAll(desjardinsReport.getTransactions());

        List<BiWeeklyReport> biWeeklyReports = createBiWeeklyReports(transactions);

        for (BiWeeklyReport report : biWeeklyReports) {
            log.info("Période: {}-{} - Dépenses fixes: {} - Dépenses variables: {}",
                    report.getStartDate(),
                    report.getEndDate(),
                    report.getTransactionReport().getTotalDepensesFixes(),
                    report.getTransactionReport().getTotalDepensesVariables());
        }
    }

    private List<BiWeeklyReport> createBiWeeklyReports(List<Transaction> transactions) {
        return new ArrayList<>();
    }
}
