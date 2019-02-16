package UI;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import dataStructure.MyQueue;
import dataStructure.MyStack;
import item.Vehicle;

/**
 * 停车场系统UI
 * 包含车辆进入和离开停车场功能
 */
public class GarageManagement {
    private Vehicle[] vehicles; //存储所有车辆信息
    private int MAXIMUM=100000; //信息存储上限
    private int CAPACITY=6;     //车库容量
    private int index=0;        //记录当前所保存的车辆总信息
    private int feePerHour=5;   //停车费
    private MyStack<Vehicle> garage;  //存储停车场中的车辆信息
    private MyQueue<Vehicle> waitedQueue;   //存储便道中的车辆信息
    private MyStack<Vehicle> buffer;   //存储从停车场暂时移除的车辆信息

    private Date date;
    private SimpleDateFormat format;  //格式化日期
    private File file;

    public GarageManagement(){
        date = new Date();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        vehicles=new Vehicle[MAXIMUM];
        garage=new MyStack<>();
        waitedQueue=new MyQueue<>();
        buffer=new MyStack<>();

        /**
         * 从文件中读取车辆信息
         */
        file=new File("GarageInfo.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String input;
            while((input=br.readLine())!=null){
                String[] info=input.split("——");
                if(info.length==2){
                    Vehicle tmp=new Vehicle(info[0], info[1]);
                    if(checkCapacity()){ //检测停车场空间是否足够
                        garage.push(tmp);
                        vehicles[index++]=tmp;
                    }
                    else {
                        waitedQueue.push(tmp);
                        vehicles[index++]=tmp;
                    }
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    /**
     * 车辆请求进入停车场
     * 分进入停车场和进入便道两种情况处理
     * @param id
     * @return
     */
    public String[] inGarage(String id){
        String[] res=new String[2];
        String time = format.format(date.getTime());

        for(int i=0; i<index; i++){
            if(id.equals(vehicles[i].getId())){
                return null;
            }
        }
        //如果空间充足，进入停车场
        if(checkCapacity()){
            Vehicle vehicle=new Vehicle(time, id);
            garage.push(vehicle);
            res[0]="到达时间："+time;
            res[1]="停放在停车场："+garage.size()+"车道";
            vehicles[index++]=vehicle;
        }

        //否则，进入便道
        else{
            Vehicle vehicle=new Vehicle(time, id);
            waitedQueue.push(vehicle);
            res[0]="停车场空间不足";
            res[1]="停放在便道："+waitedQueue.size()+"车道";
            vehicles[index++]=vehicle;
        }
        return res;
    }

    /**
     * 汽车离开停车场
     * 离开车辆之后的汽车先进入缓冲区，车辆离开后，其他车辆依次进入
     * 若有等待车辆，则等待车辆依次进入停车场
     * @param id
     * @return
     * @throws ParseException
     */
    public String[] outGarage(String id)throws ParseException{
        String time = format.format(date.getTime()); //获取当前时间
        boolean flag=true;  //判断车牌信息是否有误
        String[] res=new String[2];
        while(!garage.isEmpty()&&flag){
            flag=false;

            //后面车辆进入缓冲区
            if(!garage.peek().getId().equals(id)){
                buffer.push(garage.pop());
                flag=true;
            }
        }

        //若车牌信息无误，对离开车辆进行停车费结算
        if(!flag){
            Vehicle vehicle=garage.pop();
            String arrTime=vehicle.getArrTime();
            Date d1 = format.parse(arrTime);
            Date d2 = format.parse(time);
            long charge = (d2.getTime() - d1.getTime()) / (60 * 60 * 1000);
            System.out.println(charge);
            int fee=(int)charge*feePerHour;
            vehicle.setLveTime(time);
            res[0]="车牌号："+vehicle.getId()+'\n'+"停车时间："+charge+'\n'+"费用："+fee;
            //vehicles[++index]=vehicle;

            //缓冲区车辆回到车库
            while(!buffer.isEmpty()){
                garage.push(buffer.pop());
            }

            //便道上的车辆一次进入车库
            if(!waitedQueue.isEmpty()){
                vehicle=waitedQueue.pop();
                vehicle.setArrTime(time);
                garage.push(vehicle);
                res[1]="车牌号："+vehicle.getId()+'\n'+"到达时间："+time+'\n'+"停放在停车场："+garage.size()+"车道";
            }
        }

        //车牌信息有误
        else {
            while(!buffer.isEmpty()){
                garage.push(buffer.pop());
            }
        }
        return res;
    }


    /**
     * 检测车库是否满载
     * @return
     */
    public boolean checkCapacity(){
        return !(garage.size()==CAPACITY);
    }

    /**
     * 文件读写，保存车辆信息
     * @throws IOException
     */
    public void save() throws IOException{
        Iterator<Vehicle> iter;
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(file))){
            iter=garage.getIterator();
            String tmp;
            while(iter.hasNext()){
                String[] temp=iter.next().toString().split("——");
                tmp=String.format("%s——%s\n", temp[0], temp[1]);
                bw.write(tmp);
            }

            iter=waitedQueue.getIterator();
            while (iter.hasNext()){
                String[] temp=iter.next().toString().split("——");
                tmp=String.format("%s——%s\n", temp[0], temp[1]);
                bw.write(tmp);
            }

        }
    }
}
