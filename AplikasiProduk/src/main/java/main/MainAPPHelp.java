package main;

import db.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Menyiapkan database lalu menampilkan jendela utama dari produkKatalog.fxml.
 * Stylesheet dimuat lewat atribut stylesheets di FXML itu sendiri.
 */
public class MainAPPHelp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Database.prepareDatabase();
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/fxml/Katalog-FXML.fxml")));
            stage.setTitle("Katalog Produk");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setTitle("Terjadi kesalahan");
            alert.setHeaderText("Aplikasi gagal dimulai");
            alert.showAndWait();
            Platform.exit();
        }
    }
}
