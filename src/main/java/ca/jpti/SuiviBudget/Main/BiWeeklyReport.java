package ca.jpti.SuiviBudget.Main;

import lombok.Data;

@Data
public class BiWeeklyReport {
    String startDate;
    String endDate;
    TransactionReport transactionReport;
}
