package ca.jpti.SuiviBudget.TD;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class TDTransaction {
    private LocalDate date;
    private String description;
    private float debit;
    private float credit;
    private float balance;

    public static TDTransaction fromTokens(String[] tokens) {
        TDTransaction tdTransaction = new TDTransaction();
        tdTransaction.setDate(LocalDate.parse(tokens[0], DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        tdTransaction.setDescription(tokens[1]);
        float debit = 0;
        try {
            debit = Float.parseFloat(tokens[2]);
        } catch (NumberFormatException e) {

        }
        tdTransaction.setDebit(debit);
        float credit = 0;
        try {
            credit = Float.parseFloat(tokens[3]);
        } catch (NumberFormatException e) {

        }
        tdTransaction.setCredit(credit);
        float balance = 0;
        try {
            balance = Float.parseFloat(tokens[4]);
        } catch (NumberFormatException e) {

        }
        tdTransaction.setBalance(balance);
        return tdTransaction;
    }

    @Override
    public String toString() {
        return "TDTransaction{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                ", balance=" + balance +
                "}\n";
    }
}
