package sample;

import UI.ScenicMangement;
import item.ScenicSpot;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 负责管理员界面的逻辑控制
 */
public class ManagerController implements Initializable {
    private ScenicMangement sm;
    private ToggleGroup tg;
    private ObservableList<String> strList;
    private ToggleGroup tgTol;
    private ToggleGroup tgRest;
    private Stage managerStage;
    public Stage setManagerStage(Stage managerStage){
        this.managerStage=managerStage;
        return managerStage;
    }

    @FXML
    private Label lbTest;

    @FXML
    private TextField txtMStart;

    @FXML
    private TextField txtMEnd;

    @FXML
    private Button btnAddPath;

    @FXML
    private Button btnDeletePath;

    @FXML
    private ListView<String> listStart;

    @FXML
    private ListView<String> listDest;

    @FXML
    private ListView<String> listSpot;

    @FXML
    private TextField txtDistance;

    @FXML
    private RadioButton radioAdd;

    @FXML
    private RadioButton radioRemove;

    @FXML
    private RadioButton rdioRestY;

    @FXML
    private RadioButton rdioRestN;

    @FXML
    private RadioButton rdioTolY;

    @FXML
    private RadioButton rdioTolN;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPop;

    @FXML
    private TextArea txtIntro;

    @FXML
    private TextArea txtBroadcast;

    @FXML
    private Button btnBroadcast;

    private TextField[] txtGroup={txtName, txtPop};

    /**
     * 初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tg=new ToggleGroup();
        tgRest=new ToggleGroup();
        tgTol=new ToggleGroup();
        radioAdd.setToggleGroup(tg);
        radioRemove.setToggleGroup(tg);
        rdioRestN.setToggleGroup(tgRest);
        rdioRestY.setToggleGroup(tgRest);
        rdioTolN.setToggleGroup(tgTol);
        rdioTolY.setToggleGroup(tgTol);
        sm=new ScenicMangement();
        lbTest.setVisible(false);

        Iterator<ScenicSpot> it=sm.getScenicspots().getIterator();
        strList = FXCollections.observableArrayList();
        while(it.hasNext()){
            strList.add(it.next().getName());
        }

        refreshList();

    }


    /**
     * 刷新表格
     */
    public void refreshList(){
        listStart.setItems(strList);
        listDest.setItems(strList);
        listSpot.setItems(strList);
    }

    /**
     * 刷新输入栏
     */
    public void refreshSpotRemove(){
        txtName.setEditable(false);
        txtName.clear();
        txtPop.setEditable(false);
        txtPop.clear();
        txtIntro.setEditable(false);
        txtIntro.clear();
        rdioTolY.fire();
        rdioRestY.fire();
    }

    public void refreshSpotAdd(){
        txtName.setEditable(true);
        txtName.clear();
        txtPop.setEditable(true);
        txtPop.clear();
        txtIntro.setEditable(true);
        txtIntro.clear();
        rdioTolY.fire();
        rdioRestY.fire();
    }

    public void refreshBroadcast(){
        txtBroadcast.clear();
    }

    /**
     * 处理路径的增加和删除界面
     */
    public void handlePath(ActionEvent actionEvent){
        String start=txtMStart.getText();
        String dest=txtMEnd.getText();
        int distance;
        try {
            if(actionEvent.getSource()==btnAddPath){
                distance=Integer.parseInt(txtDistance.getText());
                if(sm.addPath(start, dest, distance)){
                    success("添加成功");
                }else{
                    alert("添加失败");
                }
            }else if(actionEvent.getSource()==btnDeletePath){
                if(sm.deletePath(start, dest)){
                    success("删除成功");
                }else {
                    alert("删除失败，景点间不存在路径");
                }
            }
        }catch (Exception ex){
            alert("添加失败");
        }

    }

    /**
     * 产生警告
     */
    public static void alert( String p_message){
        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setTitle("Error");
        _alert.setHeaderText("警告");
        _alert.setContentText(p_message);
        //_alert.initOwner(this);
        _alert.show();
    }

    /**
     * 操作成功提示
     * @param p_message
     */
    public static void success( String p_message){
        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setContentText(p_message);
        //_alert.initOwner(this);
        _alert.show();
    }

    /**
     * 从选项栏获得起点和终点
     */
    public void handleMPath(){
        txtMStart.setText(listStart.getSelectionModel().getSelectedItem());
        txtMEnd.setText(listDest.getSelectionModel().getSelectedItem());
    }

    public void handleMSpot(){
        if(tg.getSelectedToggle()!=null&&tg.getSelectedToggle().equals(radioRemove)) {
            refreshSpotAdd();
            txtName.setText(listSpot.getSelectionModel().getSelectedItem());
            ScenicSpot tmp = sm.findSpot(txtName.getText());
            txtPop.setText(tmp.getPopularity() + "");
            txtIntro.setText(tmp.getIntroduction());
            if (!tmp.hasRest()) {
                rdioRestN.fire();
            }
            if (!tmp.hasTolet()) {
                rdioTolN.fire();
            }
        }
    }

    /**
     * 删除景点
     */
    public void removeSpot(ActionEvent actionEvent) {
        if (tg.getSelectedToggle()!=null&&tg.getSelectedToggle().equals(radioRemove)){
            if(txtName.getText()==null||txtName.getText().trim().equals("")){
                alert("未选择景点");
                return;
            }
            sm.deleteScenery(txtName.getText());
            strList.remove(txtName.getText());
            refreshList();
            refreshSpotRemove();
            success("删除成功");
        }else {
            alert("未选择模式");
        }
    }

    /**
     * 添加景点
     * @param actionEvent
     */
    public void addSpot(ActionEvent actionEvent){
        if(tg.getSelectedToggle()!=null&&tg.getSelectedToggle().equals(radioAdd)) {
            String name = txtName.getText();
            String pop = txtPop.getText();
            String intro = txtIntro.getText();
            if (name.trim().equals("") || pop.trim().equals("") || intro.trim().equals("") || pop.trim().equals("")) {
                alert("有待填项，请检查");
                return;
            }
            boolean hasTol;
            boolean hasRest;
            double popularity;
            if (tgTol.getSelectedToggle().equals(rdioRestY)) {
                hasTol = true;
            } else {
                hasTol = false;
            }
            if (tgRest.getSelectedToggle().equals(rdioRestY)) {
                hasRest = true;
            } else {
                hasRest = false;
            }
            try {
                popularity = Double.parseDouble(pop);
                if (sm.addScenery(name, popularity, hasRest, hasTol, intro)) {
                    success("添加成功");
                    strList.add(txtName.getText());
                    refreshSpotAdd();
                    refreshList();
                    //System.out.println("success");
                } else {
                    alert("景区名重复，请检查");
                }
            } catch (NumberFormatException ex) {
                alert("欢迎度输入格式错误（请输入数字）");
            }
        }else {
            alert("未选择模式");
        }
    }

    /**
     * 检测是添加还是删除模式
     */
    public void handleMode(){

        if(tg.getSelectedToggle().equals(radioAdd)){
            refreshSpotAdd();

        }
        else if(tg.getSelectedToggle().equals(radioRemove)){
            refreshSpotRemove();

        }

    }

    public void handleSpot(){
        refreshSpotRemove();
    }

    /**
     * 发布公告界面
     */
    public void handleBroadcast(){
        sm.broadcast(txtBroadcast.getText());
        success("发布成功");
        refreshBroadcast();
    }

    /**
     * 管理员退出登录界面
     */
    public void logOut(){
        sm.save();
        Controller controller=new Controller();
        Stage stage=controller.setMainStage(Main.getStage());
        stage.setTitle("Scenic Management");
        try{
            Scene scene=new Scene(FXMLLoader.load(getClass().getResource("sample.fxml")));
            stage.setScene(scene);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
