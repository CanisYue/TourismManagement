package dataStructure;
import item.ScenicSpot;

import java.util.Comparator;

/**
 * Camparator:用于比较欢迎度
 */
public class PopularityComparator implements Comparator<ScenicSpot> {
    @Override
    public int compare(ScenicSpot o1, ScenicSpot o2) {
        return o2.compareTo(o1);
    }
}
