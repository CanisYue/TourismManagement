package UI;
import algorithm.BoyerMoore;
import dataStructure.Graph;
import dataStructure.MyLinkedList;
import dataStructure.MyStack;
import item.ScenicSpot;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * 景区管理系统UI
 */
public class ScenicMangement {
    private Graph map;    //由景区信息生成的图
    private MyLinkedList<ScenicSpot> scenicspots; //保存所有景点信息
    private MyLinkedList<String[]> edges;   //保存所有路的信息
    private MyLinkedList<String[]> manager; //存储管理员账号密码信息
    private File fileSpot; //景点文件
    private File fileEdge; //路文件
    private File fileUser; //管理员账号密码文件
    private File fileBroadcast;  //通告文件
    private MyStack<String[]> broadCasts;  //存储景区公告
    private SimpleDateFormat format;  //日期格式
    private Date date;

    /**
     * 初始化，读取文件信息，构建图
     */
    public ScenicMangement(){
        scenicspots=new MyLinkedList<>();
        date = new Date();
        edges=new MyLinkedList<>();
        manager=new MyLinkedList<>();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        broadCasts=new MyStack<>();
        readGraph(); //文件读取路信息
        readSenic(); //文件读取景点信息
        readUser(); //文件读写管理员账号密码
        readBroadcast(); //文件读写通知信息
        map=new Graph(scenicspots, edges);  //构建图
    }

    public Graph getMap() {
        return map;
    }

    public void setMap(Graph map) {
        this.map = map;
    }

    public MyLinkedList<ScenicSpot> getScenicspots() {
        return scenicspots;
    }

    public void setScenicspots(MyLinkedList<ScenicSpot> scenicspots) {
        this.scenicspots = scenicspots;
    }

    public MyLinkedList<String[]> getEdges() {
        return edges;
    }

    public void setEdges(MyLinkedList<String[]> edges) {
        this.edges = edges;
    }

    public MyStack<String[]> getBroadCasts() {
        return broadCasts;
    }

    /**
     * 文件读取路的信息
     */
    public  void readGraph(){
        fileEdge = new File("ScenicInfo.txt");
        try ( BufferedReader br = new BufferedReader(new FileReader(fileEdge))){
            String input;
            while((input=br.readLine())!=null){
                String[] info = input.split("——");
                edges.add(info);
                //System.out.println(Arrays.toString(info));
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * 文件读取景区信息
     */
    public void readSenic(){
        fileSpot = new File("SpotInfo.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(fileSpot))){
            String input;
            while((input=br.readLine())!=null){
                String[] info=input.split("——");
                scenicspots.add(new ScenicSpot(info[0], info[1], Double.parseDouble(info[2]),
                        Boolean.parseBoolean(info[3]), Boolean.parseBoolean(info[4])));
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * 文件读取管理员账号密码
     */
    public void readUser(){
        fileUser = new File("UserInfo.txt");
        try(BufferedReader br = new BufferedReader(new FileReader(fileUser))){
            String input;
            while((input=br.readLine())!=null){
                String[] info=input.split("——");
                manager.add(info);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * 文件读取通知信息
     */
    public void readBroadcast(){
        fileBroadcast=new File("Broadcast.txt");
        try(BufferedReader br=new BufferedReader(new FileReader(fileBroadcast))){
            String input;
            while((input=br.readLine())!=null){
                String[] info=input.split("——");
                broadCasts.push(info);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * 输出景点分布图
     * @return
     */
    public String[][] outputMap(){
        return map.outputGraph();
    }

    /**
     * 添加景点
     * @param name
     * @param popularity
     * @param hasRest
     * @param hasTolet
     * @param introduction
     * @return
     */
    public boolean addScenery(String name, double popularity, boolean hasRest, Boolean hasTolet, String introduction){
        ScenicSpot tmp=new ScenicSpot(name, introduction, popularity, hasRest, hasTolet);
        if(scenicspots.contain(tmp)){
            return false;
        }
        else {
            map.addVertex(tmp);
            scenicspots.add(tmp);
            return true;
        }
    }

    /**
     * 删除景点
     * @param name
     */
    public void deleteScenery(String name){
        scenicspots.remove(map.removeVex(name)); //
       // System.out.println(edges.size()-1);
        for(int i=edges.size()-1; i>-1; i--){
            if(edges.get(i)[0].equals(name)||edges.get(i)[1].equals(name)){
                edges.remove(i);
            }
        }
    }

    /**
     * 添加路
     * @param prev
     * @param dest
     * @param distance
     * @return
     */
    public boolean addPath(String prev, String dest, int distance){
        String[] tmp={prev, dest, distance+""};
        if(map.addEdge(tmp)) {
            edges.add(tmp);
            System.out.println("ch");
            return true;
        }else {
            return false;
        }

    }

    /**
     * 删除路
     * @param prev
     * @param dest
     * @return
     */
    public boolean deletePath(String prev, String dest){
        String[] tmp={prev, dest};
        boolean flag=false;
        for(int i=0; i<edges.size(); i++){
            if(edges.get(i)[0].equals(tmp[0])&&edges.get(i)[1].equals(tmp[1])){
                edges.remove(i);
                flag=true;
            }
        }
        if(flag==true)
            map.removeEdge(tmp);
        return flag;
    }

    /**
     * 根据景点名字返回对应景点对象
     * @param key
     * @return
     */
    public ScenicSpot findSpot(String key){
        ScenicSpot tmp;
        Iterator<ScenicSpot> it=scenicspots.getIterator();
        while(it.hasNext()){
            tmp=it.next();
            if(tmp.getName().equals(key)){
                return tmp;
            }
        }
        return null;
    }

    /**
     * 根据关键字查找对应景点
     */
    public String[][] search(String key){
        ScenicSpot tmp;
        String[][] res=new String[scenicspots.size()][2];
        int j=0;

        for(int i=0; i<scenicspots.size(); i++){
            tmp=scenicspots.get(i);
            if(BoyerMoore.match(key,tmp.getName())!=-1){
                res[j][0]=tmp.getName();
                res[j++][1]=tmp.getIntroduction();
            }else if(BoyerMoore.match(key, tmp.getIntroduction())!=-1){
                res[j][0]=tmp.getName();
                res[j++][1]=tmp.getIntroduction();
            }
        }
        if(j==0){
            return null;
        }
        else {
            return res;
        }
    }

    /**
     * 按照用户需求对经典进行排序
     * @param choice
     * @return
     */
    public String[] sort(int choice){
        String[] res=new String[scenicspots.size()];
        if(choice==1){//按照欢迎度排序
            res=map.sortByWelcomed();
        }
        else if(choice==2){//按照岔路数排序
            res=map.sortByPaths();
        }
        return res;
    }

    /**
     * 查找对应景点之间的最短路径
     * @param start
     * @param dest
     * @return
     */
    public String[] findWay(String start, String dest){
        return map.findWay(start, dest);
    }

    /**
     * 发布通告
     * @param broadCast
     */
    public void broadcast(String broadCast){
        String time = format.format(date.getTime());
        String[] res={broadCast, time};
        broadCasts.push(res);
    }

    /**
     * 文件保存所有信息
     */
    public void save(){
        Iterator<ScenicSpot> itScenic=scenicspots.getIterator();
        Iterator<String[]> itEdge=edges.getIterator();
        Iterator<String[]> itBroad=broadCasts.getIterator();
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(fileEdge))){
            while(itEdge.hasNext()){
                String tmp=String.format("%s——%s——%s\n", itEdge.next());
                bw.write(tmp);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        try (BufferedWriter bw=new BufferedWriter(new FileWriter(fileSpot))){
            while(itScenic.hasNext()){
                bw.write(itScenic.next().toString());
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        try (BufferedWriter bw=new BufferedWriter(new FileWriter(fileBroadcast))){
            while(itBroad.hasNext()){
                String tmp=String.format("%s——%s\n", itBroad.next());
                bw.write(tmp);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        System.out.println("存储成功");
    }

    public String[] guideMap(String start, String dest){
        String[] res=map.createTourSortGraph(start, dest);

        int startNum=0;
        return res;
    }

    /**
     * 管理员登陆，需要匹配用户名和密码是否正确
     * @param user
     * @param password
     * @return
     */
    public boolean logIn(String user, String password){
        Iterator<String[]> it=manager.getIterator();
        while(it.hasNext()){
            String[] u=it.next();
            if(u[0].equals(user)){
                if (u[1].equals(password)){
                    return true;
                }
            }
            else {
                return false;
            }
        }
        return false;
    }
}
