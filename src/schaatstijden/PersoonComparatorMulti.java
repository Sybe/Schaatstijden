package schaatstijden;

import java.util.Comparator;

public class PersoonComparatorMulti implements Comparator<Persoon>{
    @Override
    public int compare(Persoon p1, Persoon p2) {
        return ((PersoonMulti) p1).getPunten().compareTo(((PersoonMulti) p2).getPunten());
    }
}
