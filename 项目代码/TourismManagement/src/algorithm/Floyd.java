package algorithm;

import dataStructure.Graph;
import dataStructure.MyLinkedList;
import dataStructure.Vertex;

/**
 * Floyd算法
 */
public class Floyd {
    public static String[] floyd(Graph graph, int start, int dest) {
        //初始化
        int vertexNum=graph.getVertexes().size();
        int[][] path = new int[vertexNum][vertexNum]; // 路径。path[i][j]=k表示，"顶点i"到"顶点j"的最短路径会经过顶点k。
        int[][] dist = new int[vertexNum][vertexNum]; //长度数组。即，dist[i][j]=sum表示，"顶点i"到"顶点j"的最短路径的长度是sum。
        Vertex[] matrix=new Vertex[vertexNum];
        for(int i=0; i<vertexNum; i++){
            matrix[i]=graph.getVertexes().get(i);
        }
        // 初始化
        for (int i = 0; i < vertexNum; i++) {
            for (int j = 0; j < vertexNum; j++) {
                dist[i][j] = graph.getDistance(i, j);  // "顶点i"到"顶点j"的路径长度为"i到j的权值"。
                path[i][j] = j;                        // "顶点i"到"顶点j"的最短路径是经过顶点j。
            }
        }

        // 计算最短路径
        for (int k = 0; k < vertexNum; k++) {
            for (int i = 0; i < vertexNum; i++) {
                for (int j = 0; j < vertexNum; j++) {

                    // 如果经过下标为k顶点路径比原两点间路径更短，则更新dist[i][j]和path[i][j]
                    int tmp =  (dist[i][k]==graph.getINFINITY() ||
                            dist[k][j]==graph.getINFINITY()) ? graph.getINFINITY() : (dist[i][k] + dist[k][j]);
                    if (dist[i][j] > tmp) {
                        // "i到j最短路径"对应的值设，为更小的一个(即经过k)
                        dist[i][j] = tmp;
                        // "i到j最短路径"对应的路径，经过k
                        path[i][j] = path[i][k];
                    }
                }
            }
        }


        // 打印floyd最短路径的结果
        String[] res=new String[2];
        int tmp = path[start][dest];
        String road="";
        road+=matrix[start].getName();
        while(tmp != dest){
            road+="-> "+ matrix[tmp].getName();
            tmp = path[tmp][dest];
        }
        road+="-> "+matrix[dest].getName();
        res[0]=road;
        res[1]=dist[start][dest]+"";
        return res;
    }
}