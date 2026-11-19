/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Produk;

/**
 * FXML Controller class
 *
 * @author muama
 */
public class KartuKatalogController {

    private static final Map<String, Image> CACHE_GAMBAR = new HashMap<>();

    @FXML
    private ImageView fotoView;
    @FXML
    private Label namaLabel;
    @FXML
    private Label jenisLabel;
    @FXML
    private Label hargaLabel;

    /**
     * Initializes the controller class.
     */
    public void setData(Produk produk) {
        namaLabel.setText(produk.getNama());
        jenisLabel.setText(jenis(produk.getFoto()));
        hargaLabel.setText(rupiah(produk.getHarga()));
        fotoView.setImage(muatGambar(produk.getFoto()));
    }

    public static String rupiah(long harga) {
        return "Rp " + NumberFormat.getIntegerInstance(Locale.of("id", "ID")).format(harga);
    }

    /**
     * Jenis diturunkan dari nama file foto: "roti.png" -> "Roti".
     */
    public static String jenis(String namaFile) {
        if (namaFile == null || namaFile.isBlank()) {
            return "";
        }
        int titik = namaFile.lastIndexOf('.');
        String dasar = titik > 0 ? namaFile.substring(0, titik) : namaFile;
        return Character.toUpperCase(dasar.charAt(0)) + dasar.substring(1);
    }

    /**
     * Memuat gambar dari src/main/resources/image/. Mengembalikan null jika
     * file tidak ada.
     */
    public static Image muatGambar(String namaFile) {
        if (namaFile == null || namaFile.isBlank()) {
            return null;
        }
        Image gambar = CACHE_GAMBAR.get(namaFile);
        if (gambar == null) {
            URL lokasi = KartuKatalogController.class.getResource("/image/" + namaFile);
            if (lokasi != null) {
                gambar = new Image(lokasi.toExternalForm());
                CACHE_GAMBAR.put(namaFile, gambar);
            }
        }
        return gambar;
    }
}
