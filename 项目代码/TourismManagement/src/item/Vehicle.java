package item;

/**
 * 车辆类，保存车辆所有信息
 */
public class Vehicle {
    private String arrTime; //车辆到达时间
    private String lveTime; //车辆离开时间
    private String id;      //车牌号
    private boolean hasLeft; //车辆是否离开停车场
    private int fee;  //停车费用

    public Vehicle(String arrTime, String lveTime, String id, int fee) {
        this.arrTime = arrTime;
        this.lveTime = lveTime;
        this.id = id;
        this.hasLeft = true;
        this.fee=fee;
    }

    public Vehicle(String arrTime, String lveTime, String id) {
        this.arrTime = arrTime;
        this.lveTime = lveTime;
        this.id = id;
        this.hasLeft = true;
    }

    public Vehicle(String arrTime, String id) {
        this.arrTime = arrTime;
        this.id = id;
        this.hasLeft=false;
    }

    public Vehicle(String id){
        this.id=id;
        this.hasLeft=false;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getLveTime() {
        return lveTime;
    }

    public void setLveTime(String lveTime) {
        this.lveTime = lveTime;
        this.hasLeft = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHasLeft() {
        return hasLeft;
    }

    public void setHasLeft(boolean hasLeft) {
        this.hasLeft = hasLeft;
    }

    @Override
    public String toString(){
        return this.getArrTime()+"——"+this.getId();
    }


}
