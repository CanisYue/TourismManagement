package dataStructure;
import java.util.Comparator;

/**
 * Camparator类，用于比较不同路的长短，供排序算法使用
 */
public class PathComparator implements Comparator<Vertex> {
    @Override
    public int compare(Vertex v1, Vertex v2){
        return v2.compareTo(v1);
    }
}