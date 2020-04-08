package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 主类，整个项目的启动窗口
 */
public class Main extends Application {
    static private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../sample/sample.fxml"));
        primaryStage.setTitle("Scenic Management");
        primaryStage.setScene(new Scene(root, 978, 571));
        primaryStage.show();

    }

    public static Stage getStage(){return primaryStage;}
    public static void main(String[] args) {
        launch(args);
    }
}
