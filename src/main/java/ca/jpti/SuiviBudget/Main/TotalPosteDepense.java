package ca.jpti.SuiviBudget.Main;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalPosteDepense {
    String posteDepense;
    BigDecimal montant = BigDecimal.ZERO;
}
