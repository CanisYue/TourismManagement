package sample;

import UI.GarageManagement;
import UI.ScenicMangement;
import dataStructure.MyStack;
import item.ScenicSpot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import sample.ManagerController;

/**
 * 主界面菜单controller类
 */
public class Controller implements Initializable {
    private ScenicMangement sm;
    private GarageManagement gm;
    private Stage mainStage;
    private ObservableList<String> strList;
    private int i=0;
    private ObservableList<String> str;

    @FXML
    private TextArea txtPath;

    @FXML
    private VBox vGarage;

    @FXML
    private Button btnSearchGuide;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnSortPath;

    @FXML
    private Button btnSortWelcome;

    @FXML
    private Button btnPathSearch;

    @FXML
    private Button btnLogIn;

    @FXML
    private Button btnSpotFind;

    @FXML
    private ListView<String> list;

    @FXML
    private TextField txtID;

    @FXML
    private Button btnEnter;

    @FXML
    private Button btnLeave;

    @FXML
    private TextField txtBegin;

    @FXML
    private TextField txtEnd;

    @FXML
    private TextField txtAdmin;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtSpot;

    @FXML
    private VBox vSpot;

    @FXML
    private TextField txtStart;

    @FXML
    private TextField txtDest;

    @FXML
    private VBox vSort;

    @FXML
    private TextArea txtTable;

    @FXML
    private Pane pnlAdmin;

    @FXML
    private VBox vBroadcast;

    @FXML
    private TextArea txtGuide;

    @FXML
    private TextField txtDistance;

    /**
     * 初始化
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        str = FXCollections.observableArrayList();
        sm = new ScenicMangement();
        gm = new GarageManagement();
        TextArea ta;
        MyStack<String[]> broadcasts=sm.getBroadCasts();
        while(!broadcasts.isEmpty()){
            String[] tmp=broadcasts.pop();
            ta=new TextArea(tmp[0]+'\n'+tmp[1]);
            ta.setPrefHeight(30);
            ta.setEditable(false);
            vBroadcast.getChildren().add(ta);
        }

        Iterator<ScenicSpot> it=sm.getScenicspots().getIterator();
        strList = FXCollections.observableArrayList();
        while(it.hasNext()){
            strList.add(it.next().getName());
        }
        list.setItems(strList);
    }

    public Stage setMainStage(Stage stage){
        this.mainStage=stage;
        return stage;
    }

    /**
     * 处理查找最短距离界面
     */
    public void handlePath(ActionEvent actionEvent){
        if(actionEvent.getSource()==btnPathSearch){
            String start=txtStart.getText();
            String dest=txtDest.getText();
            String[] res=sm.findWay(start, dest);
            TextArea ta;
            txtPath.clear();
            if(res==null||res.length==0||Integer.parseInt(res[1])>30000){
                ManagerController.alert("输入有误，找不到对应景点或路");
            }
            else {
                txtPath.setText(res[0]);
                txtDistance.setText(res[1]);
            }
        }
    }

    /**
     * 处理查找景点界面
     */
    public void handleSpot(ActionEvent actionEvent){
        if(actionEvent.getSource()==btnSpotFind){
            String key=txtSpot.getText();
            String[][] res=sm.search(key);
            //System.out.println(res.length);
            TextArea ta;
            int num=vSpot.getChildren().size();
            if(num!=0)
                vSpot.getChildren().remove(0, num);
            if(res==null||key.trim().equals("")){
                ManagerController.alert("没有匹配信息");
            }
            else{
                for(int i=0; (i<res.length)&&(res[i][0]!=null); i++) {
                    str.add(res[i][0]);
                    ta=new TextArea(res[i][0]+"："+res[i][1]);
                    ta.setPrefHeight(30);
                    ta.setEditable(false);
                    vSpot.getChildren().add(ta);
                }
            }
        }
    }

    /**
     * 处理排序界面
     */
    public void handleSort(ActionEvent actionEvent){
        TextArea ta;
        int num=vSort.getChildren().size();
        String[] res;
        if(num!=0)
            vSort.getChildren().remove(0, num);
        if(actionEvent.getSource()==btnSortWelcome){
            res=sm.sort(1);
        }
        else {
            res=sm.sort(2);
        }
        if(res.length==0){
            ta=new TextArea("当前景区无可用景点");
            ta.setPrefHeight(30);
            ta.setEditable(false);
            vSort.getChildren().add(ta);
        }
        else {
            for(int i=0; i<res.length; i++){
                ta=new TextArea(res[i]);
                ta.setPrefHeight(30);
                ta.setEditable(false);
                vSort.getChildren().add(ta);
            }
        }
    }

    /**
     * 处理登录界面
     */
    public void handleLogIn(ActionEvent actionEvent){
        if(actionEvent.getSource()==btnLogIn){
            String user=txtAdmin.getText();
            String password=txtPassword.getText();
            pnlAdmin.setVisible(true);
            txtAdmin.clear();
            txtPassword.clear();
            if(sm.logIn(user, password)){
                try{
                    gm.save();
                    ManagerController managerController=new ManagerController();
                    Stage managerStage=managerController.setManagerStage(Main.getStage());
                    managerStage.setTitle("管理员界面");
                    Scene scene=new Scene(FXMLLoader.load(getClass().getResource("manager.fxml")));
                    managerStage.setScene(scene);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            else {
                ManagerController.alert("用户名或密码错误");
            }
        }
    }

    /**
     * 处理停车场界面
     */
    public void handleGarage(ActionEvent actionEvent){
        int num=vGarage.getChildren().size();
        if(num!=0)
            vGarage.getChildren().remove(0, num);
        String id=txtID.getText();
        String[] res;
        TextArea ta;

        //检验输入是否有效，包括是否重复添加
        if(id.trim().equals("")||id==null){
            ManagerController.alert("输入信息为空");
            return;
        }
        if(actionEvent.getSource()==btnEnter){
            res =gm.inGarage(id);
            if(res==null){
                ManagerController.alert("车牌信息重复");
                return;
            }
            ta=new TextArea(res[0]+'\n'+res[1]);
            ta.setPrefHeight(60);
            ta.setEditable(false);
            vGarage.getChildren().add(ta);
        }
        else if(actionEvent.getSource()==btnLeave){
            try {
                res = gm.outGarage(id);

                //如果车牌号不存在，返回特殊值
                if(res[0]==null){
                    ManagerController.alert("车牌号不存在");
                    return;
                }
                for(int i=0; i<2&&res[i]!=null; i++) {
                    ta = new TextArea(res[i]);
                    ta.setPrefHeight(60);
                    ta.setEditable(false);
                    vGarage.getChildren().add(ta);
                }
            }catch (ParseException pe){
                pe.printStackTrace();
                ManagerController.alert("系统错误");
            }
        }
    }

    /**
     * 处理最短距离界面
     */
    public void handleDis() {
        String[][] res = sm.outputMap();
        int num = res.length;
        //txtTable.setEditable(false);
        String tmp="";
        for(int i=0; i<num; i++){
            for(int j=0; j<num; j++){
                tmp+=String.format("\t%-20s\t", res[i][j]);
            }
            tmp+='\n';
        }
        txtTable.setText(tmp);

    }

    /**
     * 处理导游图界面
     */
    public void handleConfirm(ActionEvent actionEvent){
        if(actionEvent.getSource()==btnConfirm) {
            String tmp = list.getSelectionModel().getSelectedItem();
            if (i == 0) {
                txtBegin.setText(tmp);
                i++;
            }
            else if (i == 1) {
                txtEnd.setText(tmp);
                i++;
            }
        }
        else if(actionEvent.getSource()==btnSearchGuide){
            //System.out.println("xianshi"+i);
            if(i!=2){
                ManagerController.alert("有信息未填");
                return;
            }
            String start=txtBegin.getText();
            String dest=txtEnd.getText();
            i=0;
            txtGuide.clear();
            String[] result=sm.guideMap(start, dest);
            //System.out.println(result[0]+"hey");
            txtGuide.setText(result[0]);
            if(result[0]==null||result[0].trim().equals("")){
                ManagerController.alert("无路径");
            }
        }
    }

    /**
     * 退出界面，保存停车场数据
     */
    public void logout(){
        try {
            gm.save();
            Stage stage=this.setMainStage(Main.getStage());
            stage.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
