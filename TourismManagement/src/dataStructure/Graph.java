package dataStructure;

import algorithm.Dijkstra;
import algorithm.Floyd;
import algorithm.MyHamilton;
import algorithm.QuickSort;
import item.ScenicSpot;

/**
 * 邻接链表图类，包含了保存景点信息的邻接链表和与图有关的相关功能实现
 */
public class Graph {
    private int edgeNum;  //图中边的数量
    private int vertexNum; //图中节点的数量
    private int INFINITY=32767; //用该数字代表无限，表示两个节点不相连
    private MyLinkedList<Vertex> vertexes; //用于保存图中所有节点

    public int getEdgeNum() {
        return edgeNum;
    }

    public void setEdgeNum(int edgeNum) {
        this.edgeNum = edgeNum;
    }

    public int getVertexNum() {
        return vertexNum;
    }

    public void setVertexNum(int vertexNum) {
        this.vertexNum = vertexNum;
    }

    public int getINFINITY() {
        return INFINITY;
    }

    public void setINFINITY(int INFINITY) {
        this.INFINITY = INFINITY;
    }

    public MyLinkedList<Vertex> getVertexes() {
        return vertexes;
    }

    public void setVertexes(MyLinkedList<Vertex> vertexes) {
        this.vertexes = vertexes;
    }

    /**
     * 图的构造函数
     */
    public Graph(MyLinkedList<ScenicSpot> scenicSpots, MyLinkedList<String[]> edges){
        vertexes = new MyLinkedList<>();
        this.vertexNum=scenicSpots.size();
        this.edgeNum=edges.size();

        //初始化图的节点
        for(int i=0; i<vertexNum; i++){
            vertexes.add(new Vertex(scenicSpots.get(i).getName(),scenicSpots.get(i)));
        }

        for(int i=0; i<edgeNum; i++){
            //读取边的起始顶点和结束顶点，初始化图的边
            int p1=getPosition(edges.get(i)[0]); //获取边的起点位置
            int p2=getPosition(edges.get(i)[1]); //获取边的终点位置
            Vertex tmp1=vertexes.get(p2);        //获取边的起点
            Vertex tmp2=vertexes.get(p1);        //获取边的终点
            EdgeNode node1 =new EdgeNode(Integer.parseInt(edges.get(i)[2]), tmp2, tmp1); //创建边
            vertexes.get(p1).addEdge(node1);  //将边添加到对应节点

            EdgeNode node2 =new EdgeNode(Integer.parseInt(edges.get(i)[2]), tmp1, tmp2);  //创建边
            vertexes.get(p2).addEdge(node2);  //将边添加到对应节点
        }
    }

    /**
     * 根据景点名字获取表示该景点的节点在list中的位置
     * @param name
     * @return
     */
    public int getPosition(String name){
        for(int i=0; i<vertexNum; i++){
            if((vertexes.get(i).getName()).equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取两个节点之间的直接距离，如果节点不相邻返回无穷大
     * @param start
     * @param end
     * @return
     */
    public int getDistance(int start, int end) {
        if (start==end)
            return 0;
        Vertex tmp=vertexes.get(start);
        for(int i=0; i<tmp.getEdge().size(); i++){
            EdgeNode tmpEdge=tmp.getEdge().get(i);
            String dest=tmpEdge.getDestination().getName();
            if(dest.equals(vertexes.get(end).getName())){
                return tmpEdge.getDistance();
            }
        }
        return getINFINITY();
    }

    /**
     * 给当前图增加边
     * 需要在与一条边相关的两个节点分别添加对应信息
     * @param edge
     * @return
     */
    public boolean addEdge(String[] edge){
        int p1=getPosition(edge[0]);
        int p2=getPosition(edge[1]);
        if(p1!=-1&&p2!=-1){
            Vertex tmp1=vertexes.get(p2);
            Vertex tmp2=vertexes.get(p1);
            EdgeNode node1 =new EdgeNode(Integer.parseInt(edge[2]), tmp2, tmp1);
            EdgeNode node2 =new EdgeNode(Integer.parseInt(edge[2]), tmp1, tmp2);
            if(vertexes.get(p1).addEdge(node1)&&vertexes.get(p2).addEdge(node2)) {
                return true;
            }
        }else{
            System.out.println("顶点" + edge[0] + "或" + edge[1] + "不存在！");
        }
        return false;
    }

    /**
     * 给当前图增添节点
     * @param data
     */
    public void addVertex(ScenicSpot data){
        Vertex vertex = new Vertex(data.getName(), data);
        vertexes.add(vertex);
    }

    /**
     * 给当前图删除边
     * 需要在与该边相关的两个节点分别删除对应信息
     * @param edge
     */
    public void removeEdge(String[] edge){
        int p1=getPosition(edge[0]);
        int p2=getPosition(edge[1]);

        //需要对两个节点进行操作才能删掉一条边
        if(p1!=-1&&p2!=-1){
            Vertex vertex=vertexes.get(p1);
            vertex.removeEdge(edge[1] );
            vertex=vertexes.get(p2);
            vertex.removeEdge(edge[0]);

        }
    }

    /**
     * 删除节点，不仅要删掉节点，还要删掉包含该节点的边
     * @param name
     * @return
     */
    public int removeVex(String name){
        int p1=getPosition(name);
        if(p1!=-1){
            for(int i=0; i<vertexes.size(); i++){
                vertexes.get(i).removeEdge(name);
            }
            vertexes.remove(p1);
            return p1;
        }else{
            System.out.println("顶点"+name+"不存在");
            return -1;
        }
    }

    /**
     * 获取图的邻接矩阵
     */
    public int[][] outputAdj(){
        vertexNum=vertexes.size(); //获取图中节点数量
        int[][] res=new int[vertexNum][vertexNum]; //带返回的结果

        //用迭代获取每一个节点所包含的边对应的另一个节点的位置
        for(int i=0; i<vertexNum; i++){
            Vertex tmp=vertexes.get(i);
            EdgeNode tmpEdge;
            for(int j=0; j<tmp.getEdge().size(); j++){
                tmpEdge=tmp.getEdge().get(j);
                tmpEdge.setVertexIndex(getPosition(tmpEdge.getDestination().getName()));
            }
        }

        int[] length;//用于更新距离
        MyLinkedList<EdgeNode> tmp;
        EdgeNode edgeNode;

        //先预设所有节点不相连，访问每一个节点的所有边，若存在边，将距离由无穷大更新为新距离
        for(int i=0; i<vertexNum; i++){
            length=new int[vertexNum];
            //初始化距离为无穷大
            for(int j=0; j<vertexNum; j++){
                length[j]=INFINITY;
            }
            length[i]=0;
            tmp=vertexes.get(i).getEdge();
            for(int j=0; j<tmp.size(); j++){
                edgeNode=tmp.get(j);
                length[edgeNode.getVertexIndex()]=edgeNode.getDistance();
            }
            for(int j=0; j<vertexNum; j++) {
                res[i][j]=length[j];
            }
        }
        return res;
    }

    /**
     * 打印景区分布图
     */
    public String[][] outputGraph(){
        vertexNum=vertexes.size(); //获取图中节点数量
        String[][] res=new String[vertexNum+1][vertexNum+1]; //带返回的结果

        //用迭代获取每一个节点所包含的边对应的另一个节点的位置
        for(int i=0; i<vertexNum; i++){
            Vertex tmp=vertexes.get(i);
            EdgeNode tmpEdge;
            for(int j=0; j<tmp.getEdge().size(); j++){
                tmpEdge=tmp.getEdge().get(j);
                tmpEdge.setVertexIndex(getPosition(tmpEdge.getDestination().getName()));
            }
        }
        res[0][0]="景点分布图";//方便打印

        //打印的表头
        for(int i=0; i<vertexNum; i++){
            res[0][i+1]=vertexes.get(i).getName();
        }
        int[] length;//用于更新距离
        MyLinkedList<EdgeNode> tmp;
        EdgeNode edgeNode;

        //先预设所有节点不相连，访问每一个节点的所有边，若存在边，将距离由无穷大更新为新距离
        for(int i=0; i<vertexNum; i++){
            length=new int[vertexNum];

            //初始化距离为无穷大
            for(int j=0; j<vertexNum; j++){
                length[j]=INFINITY;
            }
            length[i]=0;
            tmp=vertexes.get(i).getEdge();
            for(int j=0; j<tmp.size(); j++){
                edgeNode=tmp.get(j);
                length[edgeNode.getVertexIndex()]=edgeNode.getDistance();
            }
            res[i+1][0]=vertexes.get(i).getName();
            for(int j=0; j<vertexNum; j++) {
                res[i+1][j+1]=length[j]+"";
            }
        }

        //打印结果
        for(int i=0; i<res.length; i++){
            for(int j=0; j<res.length; j++){
                System.out.printf("\t%20s\t", res[i][j]);
            }
            System.out.println();
        }
        return res;
    }

    /**
     * 排序功能：按照欢迎度排序
     */
    public String[] sortByWelcomed(){
        ScenicSpot[] matrix=new ScenicSpot[vertexes.size()];
        for(int i=0; i<vertexes.size(); i++){
            matrix[i]=vertexes.get(i).getData();
        }
        String[] res=new String[vertexes.size()];
        int num=0;
        QuickSort.quickSort(matrix, 0, vertexes.size()-1, new PopularityComparator());  //快速排序
        for(int i=0; i<vertexes.size(); i++){
            res[num++]=matrix[i].getName()+" 人气值："+matrix[i].getPopularity();
        }
        return res;
    }

    /**
     * 排序功能：按照岔路数排序
     */
    public String[] sortByPaths(){
        String[] res=new String[vertexes.size()];
        Vertex[] matrix=new Vertex[vertexes.size()];
        for(int i=0; i<vertexes.size(); i++){
            matrix[i]=vertexes.get(i);
        }
        QuickSort.quickSort(matrix, 0, vertexes.size()-1, new PathComparator());       //快速排序
        int num=0;
        for(int i=0; i<vertexes.size(); i++){
            res[num++]=matrix[i].getName()+" 岔路数："+matrix[i].getEdge().size();
        }
        return res;
    }

    /**
     * 查找两个节点之间的最短路
     */
    public String[] findWay(String start, String dest){
        int p1=getPosition(start);
        int p2=getPosition(dest);
        if(p1==-1||p2==-1){
            return null;
        }
        //return Floyd.floyd(this, p1, p2);
        return Dijkstra.dijkstra(this, p1, p2);
    }

    /**
     * 创建导游图
     */
    public String[] createTourSortGraph(String start, String dest){
        MyHamilton myHamilton=new MyHamilton(start, dest, this);
        String[] res=myHamilton.getHamiltonCircuit();
        //MyHamilton.getHamiltonCircuit(start, dest, this);
        return res;
    }
}
