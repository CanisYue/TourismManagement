package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 负责管理员界面的维护
 */
public class ManagementMain extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("manager.fxml"));
        stage.setTitle("管理员");
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }

    public static void main(String[] args) { launch(ManagementMain.class, args); }
}
