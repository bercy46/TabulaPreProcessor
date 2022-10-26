package ca.jpti.SuiviBudget.Main;

import java.util.Comparator;

public class TotalPosteDepenseComparator implements Comparator<TotalPosteDepense> {
    @Override
    public int compare(TotalPosteDepense o1, TotalPosteDepense o2) {
        if (o1 == null || o2 == null) {
            return 0;
        }

        return -1 * o1.getMontant().compareTo(o2.getMontant());
    }
}
