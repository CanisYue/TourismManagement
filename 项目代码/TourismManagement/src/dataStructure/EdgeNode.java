package dataStructure;

/**
 * 边节点类，通过保存另一个节点来保存边信息
 */
public class EdgeNode {
    private int vertexIndex;       //终点节点在节点数组中的位置
    private int distance;          //变得距离
    private Vertex destination;    //终点节点
    private Vertex start;          //起始节点

    public EdgeNode(int distance, Vertex start, Vertex destination){
        this.distance=distance;
        this.destination=destination;
        this.start=start;
    }

    public int getVertexIndex() {
        return vertexIndex;
    }

    public void setVertexIndex(int vertexIndex) {
        this.vertexIndex = vertexIndex;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    /**
     * 重写方法：一个节点的两个目标节点相同，则为同一条边
     */
    @Override
    public boolean equals(Object o){
        EdgeNode tmp=(EdgeNode) o;
        if(tmp.getDestination().getName().equals(destination.getName())){
            return true;
        }
        else {
            return false;
        }
    }
}
