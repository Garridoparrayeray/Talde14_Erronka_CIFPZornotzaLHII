package app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //login froga
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Bermeoko Udala - Galdutakoak");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(new Scene(root, 1100, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}