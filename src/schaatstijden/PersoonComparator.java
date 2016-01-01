package schaatstijden;

import java.util.Comparator;

public class PersoonComparator implements Comparator<Persoon> {
    @Override
    public int compare(Persoon p1, Persoon p2) {
        return p1.getTijd().compareTo(p2.getTijd());
    }
}
