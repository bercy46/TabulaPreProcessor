package ca.jpti.SuiviBudget.Main;

import lombok.Data;

import java.util.List;

@Data
public class TransactionReport {
    float totalDepensesFixes;
    float totalDepensesVariables;
    float totalDepensesIgnorees;
//    List<Transaction> transactionsDepensesFixes;
//    List<Transaction> transactionsDepensesVariables;
//    List<Transaction> transactionsDepensesIgnorees;
    List<Transaction> transactions;
}
