package ca.jpti.SuiviBudget.Main;

import lombok.Data;

@Data
public class WeeklyReport implements Comparable {
    String period;
    TransactionReport transactionReport;

    @Override
    public int compareTo(Object o) {
        WeeklyReport other = (WeeklyReport) o;
        return this.getPeriod().compareTo(other.getPeriod());
    }
}
