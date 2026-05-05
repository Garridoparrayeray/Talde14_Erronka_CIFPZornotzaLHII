package app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX aplikazioaren klase nagusia. Login pantaila kargatzen du hasieran.
 * @author Yeray Garrido
 */
public class Main extends Application {

    /**
     * Leiho nagusia sortzen du eta login bista kargatzen du.
     * @param primaryStage JavaFX leiho nagusia
     * @throws Exception FXML kargatzean errorea bada
     */
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

    /**
     * JavaFX aplikazioa abiarazten du.
     * @param args Komando-lerroko argumentuak
     */
    public static void main(String[] args) {
        utils.LogKudeatzailea.hasieratu();
        launch(args);
    }
}