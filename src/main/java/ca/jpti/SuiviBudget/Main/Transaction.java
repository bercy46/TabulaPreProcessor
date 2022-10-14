package ca.jpti.SuiviBudget.Main;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class Transaction {
    private LocalDate date;
    private String description;
    private float debit;
    private float credit;
    private float balance;
    private String categorie;
    private String posteDepense;
    private String compte;
    private String institution;


    public static Transaction fromTokens(String[] tokens) {
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.parse(tokens[0], DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        transaction.setDescription(tokens[1]);
        float debit = 0;
        try {
            debit = Float.parseFloat(tokens[2]);
        } catch (NumberFormatException e) {

        }
        transaction.setDebit(debit);
        float credit = 0;
        try {
            credit = Float.parseFloat(tokens[3]);
        } catch (NumberFormatException e) {

        }
        transaction.setCredit(credit);
        float balance = 0;
        try {
            balance = Float.parseFloat(tokens[4]);
        } catch (NumberFormatException e) {

        }
        transaction.setBalance(balance);
        return transaction;
    }

    @Override
    public String toString() {
        return "TDTransaction{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                ", balance=" + balance +
                ", categorie=" + categorie +
                ", posteDepense=" + posteDepense +
                ", compte=" + compte +
                ", institution=" + institution +
                "}\n";
    }
}
