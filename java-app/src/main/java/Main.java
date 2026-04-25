import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargamos el FXML desde la carpeta view
        // Importante: El nombre debe coincidir exactamente con tu archivo (login.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Bermeoko Udala - Galdutakoak");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}