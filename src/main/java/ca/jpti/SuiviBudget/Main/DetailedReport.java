package ca.jpti.SuiviBudget.Main;

import lombok.Data;

@Data
public class DetailedReport implements Comparable {
    String period;
    TransactionReport transactionReport;

    @Override
    public int compareTo(Object o) {
        DetailedReport other = (DetailedReport) o;
        return this.getPeriod().compareTo(other.getPeriod());
    }
}
