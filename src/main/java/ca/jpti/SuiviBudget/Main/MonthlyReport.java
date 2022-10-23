package ca.jpti.SuiviBudget.Main;

import lombok.Data;

@Data
public class MonthlyReport implements Comparable {
    String period;
    TransactionReport transactionReport;

    @Override
    public int compareTo(Object o) {
        MonthlyReport other = (MonthlyReport) o;
        return this.getPeriod().compareTo(other.getPeriod());
    }
}
