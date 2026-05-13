package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.BiltegiLokala;
import utils.DBKonexioa;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

/**
 * JavaFX aplikazioaren klase nagusia. Login pantaila kargatzen du hasieran.
 *
 * @author Yeray Garrido
 */
public class Main extends Application {

    /**
     * Leiho nagusia sortzen du eta login bista kargatzen du.
     *
     * @param primaryStage JavaFX leiho nagusia
     * @throws Exception FXML kargatzean errorea bada
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Bermeoko Udala - Galdutakoak");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(new Scene(root, 1100, 700));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Aplikazioa ixten denean deitzen da automatikoki. DB-tik azken datuak
     * store.dat-era gordetzen ditu offline-erako.
     */
    @Override
    public void stop() {
        if (!ModoKudeatzailea.isOffline()) {
            BiltegiLokala.sincronizatuDBtik();
        } else {
            BiltegiLokala.gorde();
        }
        DBKonexioa.itxi();
    }

    /**
     * JavaFX aplikazioa abiarazten du.
     *
     * @param args Komando-lerroko argumentuak
     */
    public static void main(String[] args) {
        LogKudeatzailea.hasieratu();
        ModoKudeatzailea.detektatu();
        launch(args);
    }
}
