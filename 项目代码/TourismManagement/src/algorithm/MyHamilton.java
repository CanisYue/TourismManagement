package algorithm;

import UI.ScenicMangement;
import dataStructure.Graph;
import dataStructure.MyLinkedList;
import dataStructure.Vertex;

/**
 * 通过Floyd和DFS获取最短导游图和汉密尔顿回路
 */
public class MyHamilton {
    private int beginNum;        //起始节点在数组中的位置
    private int endNum;          //终点在数组中的位置
    private int vertexNum;       //节点数量
    private String begin;        //起始节点名字
    private String end;          //结束节点名字
    private Graph graph;         //根据节点和边生成的图
    private Vertex[] matrix;     //节点数组
    private boolean key;         //判断深度遍历后能否到达终点
    private MyLinkedList<MyLinkedList<String>> roads;  //存储路径
    private String road;        //存储路径
    private String haven;

    public MyHamilton(String begin, String end, Graph graph){
        vertexNum=graph.getVertexes().getSize();
        this.graph=graph;
        this.begin=begin;
        this.end=end;
        matrix=new Vertex[vertexNum];
        for(int i=0; i<vertexNum; i++){
            matrix[i]=graph.getVertexes().get(i);
        }
        beginNum=graph.getPosition(begin);
        endNum=graph.getPosition(end);
        roads =new MyLinkedList<>();
        road="";
        haven="";
    }

    public String[] getHamiltonCircuit(){
        boolean[] used=new boolean[vertexNum];  //用于标记图中顶点是否被访问
        int[] path=new int[vertexNum];   //记录哈密顿回路路径
        for(int i=0; i<vertexNum; i++){
            used[i]=false;   //初始化，所有顶点均未被遍历
            path[i]=-1;      //初始化，未选中起点及到达任何顶点
        }

        used[beginNum]=true;  //表示从第1个顶点开始遍历
        path[0]=beginNum;     //表示哈密顿回路起点为第0个顶点
        int[][] adj=graph.outputAdj();
        String[] result=new String[2];
        key=dfs(adj, path, used, 1);   //从第0个顶点开始进行深度优先遍历,如果存在哈密顿回路，输出一条回路，否则无输出

        result=addPath();
        return result;
    }

    /*
     * 参数step:当前行走的步数，即已经遍历顶点的个数
     */
    public boolean dfs(int[][] adj, int[] path, boolean[] used, int step){
        if(step==adj.length){   //当已经遍历完图中所有顶点

            //如果找到终点并且起点和终点相同
            if((beginNum==endNum)&&(adj[path[step - 1]][endNum] != 32767)) {
                for (int i = 0; i < path.length; i++)
                    road += matrix[path[i]].getName() + "——>";
                road += matrix[endNum].getName();
                return true;
            }

            //如果找到终点并且起点和终点不同
            if((beginNum!=endNum)&&(path[step - 1] == endNum)){
                for (int i = 0; i < path.length-1; i++)
                    road += matrix[path[i]].getName() + "——>";
                road+=matrix[path[path.length-1]].getName();
                return true;
            }

            //遍历后未找到终点，记录当前位置和路径
            else {
                MyLinkedList<String> temp = new MyLinkedList<>();
                for (int i = 0; i < path.length-1; i++)
                    road+=matrix[path[i]].getName() + "——>";
                temp.add(road);
                //System.out.println(road);
                temp.add(path[path.length - 1] + "");
                roads.add(temp);
                //System.out.println(road);
                road="";
                haven="";
                return false;
            }

            //进行遍历
        }else {
            for(int i=0; i<adj.length; i++){
                if(!used[i]&&adj[path[step-1]][i]!=32767){
                    used[i]=true;
                    path[step]=i;
                    if(dfs(adj, path, used, step+1)){
                        return true;
                    }
                    else{
                        used[i]=false;
                        path[step]=-1;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 生成并返回最短路
     */
    public String[] addPath(){
        //如果找到终点，直接返回路径
        String[] result=new String[2];
        if(key==true){
            result[0]=road;
            //System.out.println(road);
            return result;
        }

        //如果未能找到终点，就用Floyd寻找当前到达点和终点的最短路并进行比较获得最短路
        else {

            int[] pend = new int[roads.size()];
            int shortestIndex = 0;
            String[] shortest = Floyd.floyd(graph, Integer.parseInt(roads.get(0).get(1)), endNum);
            for (int i = 1; i < pend.length; i++) {
                String[] tempShortest = Floyd.floyd(graph, Integer.parseInt(roads.get(i).get(1)), endNum);
                int a = Integer.parseInt(tempShortest[1]);
                int b = Integer.parseInt(shortest[1]);
                if (a < b) {
                    shortest = tempShortest;
                    shortestIndex = i;
                }
            }
            haven = "";
            result[0] = roads.get(shortestIndex).get(0) + shortest[0];
            return result;
        }
    }
}
