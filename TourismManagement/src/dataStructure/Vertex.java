package dataStructure;

import item.ScenicSpot;

/**
 * 节点类，保存了该节点所包含的景点信息和与它相连的边
 */
public class Vertex implements Comparable<Vertex>{
    private String name; //景点名
    private ScenicSpot data; //景点信息
    private MyLinkedList<EdgeNode> edge; //节点所包含的边
    public Vertex(String name, ScenicSpot data){
        this.name=name;
        this.data=data;
        edge=new MyLinkedList<>();
    }

    /**
     * 比较两个节点的岔路数
     */
    @Override
    public int compareTo(Vertex v){
        int pathNum=edge.size();
        if(pathNum>v.getEdge().size()){
            return 1;
        }
        else if(pathNum==v.getEdge().size()){
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

    public ScenicSpot getData() {
        return data;
    }

    public void setData(ScenicSpot data) {
        this.data = data;
    }

    public void setEdge(MyLinkedList<EdgeNode> edge) {
        this.edge = edge;
    }

    /**
     * 添加边
     */
    public boolean addEdge(EdgeNode edgeNode){
        return edge.addNotSame(edgeNode);
    }


    public MyLinkedList<EdgeNode> getEdge(){
        return edge;
    }

    /**
     * 删除边
     */
    public boolean removeEdge(String name){
        for(int i=0; i<edge.size(); i++){
            if(edge.get(i).getDestination().getName().equals(name)){
                edge.remove(i);
                return true;
            }
        }return false;
    }

    @Override
    public boolean equals(Object o){
        return getName().equals(((Vertex) o).getName());
    }
}
