package ca.jpti.SuiviBudget.Desjardins;

import java.util.Comparator;

public class TransactionFactureeComparator implements Comparator<TransactionFacturee> {
    @Override
    public int compare(TransactionFacturee o1, TransactionFacturee o2) {
        if (o1 == null || o2 == null) {
            return 0;
        }

        return -1 * o1.getNumeroSequence().compareTo(o2.getNumeroSequence());
    }
}
