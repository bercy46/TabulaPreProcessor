package ca.jpti.SuiviBudget.Main;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostesDepensesReport implements Comparable {
    String period;
    List<TotalPosteDepense> totauxPostesDepenses = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    @Override
    public int compareTo(Object o) {
        PostesDepensesReport other = (PostesDepensesReport) o;
        return this.getPeriod().compareTo(other.getPeriod());
    }

}
