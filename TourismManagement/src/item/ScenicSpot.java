package item;
import java.io.*;

/**
 * 景点类，保存景点所有信息
 */
public class ScenicSpot implements Comparable<ScenicSpot>{
    private String name;  //景点名字
    private String introduction;//景点对象
    private double popularity;//景点欢迎度
    private boolean hasRest;//有无休息区
    private boolean hasTolet;//有无厕所

    public ScenicSpot(String name, String introduction, double popularity, boolean hasRest, boolean hasTolet) {
        this.name = name;
        this.introduction = introduction;
        this.popularity = popularity;
        this.hasRest = hasRest;
        this.hasTolet = hasTolet;
    }

    public ScenicSpot(String spotA, String spotB, int distance){

    }

    /**
     * 按照受欢迎程度比较景点
     */
    @Override
    public int compareTo(ScenicSpot o){
        if(popularity>o.popularity){
            return 1;
        }
        else if(popularity==o.popularity){
            return 0;
        }
        else
            return -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public boolean hasRest() {
        return hasRest;
    }

    public void setHasRest(boolean hasRest) {
        this.hasRest = hasRest;
    }

    public boolean hasTolet() {
        return hasTolet;
    }

    public void setHasTolet(boolean hasTolet) {
        this.hasTolet = hasTolet;
    }

    @Override
    public String toString(){
        return name+"——"+introduction+"——"+popularity+"——"+hasRest+"——"+hasTolet+'\n';
    }

    /**
     * 若景点名字相同，则两景点为同一个景点
     */
    @Override
    public boolean equals(Object o){
        ScenicSpot tmp=(ScenicSpot) o;
        return getName().equals(tmp.getName());
    }

}
