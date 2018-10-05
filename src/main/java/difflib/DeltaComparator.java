package difflib;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author mksenzov
 */
public class DeltaComparator implements Comparator<Delta>, Serializable {
    private static final long serialVersionUID = 1L;
    public static final Comparator<Delta> INSTANCE = new DeltaComparator();

    private DeltaComparator() {
    }

    public int compare(final Delta a, final Delta b) {
        if (a.getOriginal().getPosition() > b.getOriginal().getPosition()) {
            return 1;
        } else if (a.getOriginal().getPosition() < b.getOriginal().getPosition()) {
            return -1;
        }

        return 0;
    }
}