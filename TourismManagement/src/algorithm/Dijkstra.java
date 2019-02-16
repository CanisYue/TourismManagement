package algorithm;

import dataStructure.Graph;
import dataStructure.MyStack;
import dataStructure.Vertex;

/**
 * Dijkstra算法
 */
public class Dijkstra {
    public static String[] dijkstra(Graph graph, int start, int dest) {
        int num=0;
        int vertexNum=graph.getVertexes().size();
        int[] prev = new int[vertexNum]; //prev——到达i节点的前一个节点
        int[] dist = new int[vertexNum]; //dist——长度数组。
        Vertex[] matrix=new Vertex[vertexNum];
        MyStack<String[]> path=new MyStack<>();
        for(int i=0; i<vertexNum; i++){
            matrix[i]=graph.getVertexes().get(i);
        }

        // flag[i]=true表示"顶点vs"到"顶点i"的最短路径已成功获取。
        boolean[] flag = new boolean[vertexNum];

        // 初始化
        for (int i = 0; i < vertexNum; i++) {
            flag[i] = false;            // 顶点i的最短路径还没获取到。
            prev[i] = 0;                // 顶点i的前驱顶点为0。
            dist[i] = graph.getDistance(start, i); // 顶点i的最短路径为起点到"顶点i"的权。
        }

        // 对起点进行初始化
        flag[start] = true;
        dist[start] = 0;

        // 遍历；每次找出一个顶点的最短路径。
        int k = 0;
        for (int i = 1; i < vertexNum; i++) {

            // 寻找当前最小的路径；
            // 即，在未获取最短路径的顶点中，找到离vs最近的顶点(k)。
            int min = graph.getINFINITY();
            for (int j = 0; j < vertexNum; j++) {
                if (!flag[j] && dist[j]<min) {
                    min = dist[j];
                    k = j;
                }
            }

            // 标记"顶点k"为已经获取到最短路径
            flag[k] = true;

            // 修正当前最短路径和前驱顶点
            // 即，当已经"顶点k的最短路径"之后，更新"未获取最短路径的顶点的最短路径和前驱顶点"。
            for (int j = 0; j < vertexNum; j++) {
                int tmp = graph.getDistance(k, j);
                tmp = (tmp==graph.getINFINITY() ? graph.getINFINITY() : (min + tmp)); // 防止溢出
                if (!flag[j] && (tmp<dist[j]) )
                {
                    dist[j] = tmp;
                    prev[j] = k;
                }
            }
        }

        //输出结果
        int p=dest;
        while(p!=start){
            String p1=matrix[p].getName();
            p=prev[p];
            String p2=matrix[p].getName();
            String[] tmp={p2, p1};
            path.push(tmp);
        }

        int distance=dist[dest];
        String[] res=new String[path.size()+1];
        while(!path.isEmpty()){
            String[] tmp=path.pop();
            res[num++]=tmp[0]+"——"+tmp[1];
        }
        res[num++]=distance+"";
        return res;
    }
}
