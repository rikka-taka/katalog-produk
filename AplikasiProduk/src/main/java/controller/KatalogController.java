/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import data.ProdukRepository;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import model.Produk;

/**
 * FXML Controller class
 *
 * @author muama
 */
public class KatalogController implements Initializable {

    private static final String MENU_TAMBAH = "tambah produk";
    private static final String MENU_EDIT = "edit produk";
    private static final String MENU_HAPUS = "hapus produk";

    private static final String EDIT_NAMA = "Nama";
    private static final String EDIT_HARGA = "harga";
    private static final String EDIT_GAMBAR = "gambar";

    private static final List<String> DAFTAR_GAMBAR = List.of("roti.png", "kopi.png", "susu.png");

    private static final String STYLE_BERHASIL = "pesan-berhasil";
    private static final String STYLE_ERROR = "pesan-error";

    private static final Map<String, Image> CACHE_GAMBAR = new HashMap<>();

    @FXML
    private TextField hargaField;
    @FXML
    private ComboBox<String> fotoBox;
    @FXML
    private Label pesanLabel;
    @FXML
    private Label jumlahLabel;
    @FXML
    private TilePane katalogPane;
    @FXML
    private ComboBox<String> menuPilihan;
    @FXML
    private VBox tambahProduk;
    @FXML
    private TextField tambahField;
    @FXML
    private VBox pencarianProduk;
    @FXML
    private TextField cariField;
    @FXML
    private Button cariProduk;
    @FXML
    private Label pesanLabel1;
    @FXML
    private Label jumlahLabel1;
    @FXML
    private TilePane hasilEditPane;
    @FXML
    private HBox barisEdit;
    @FXML
    private ComboBox<String> pilihanEditBox;
    @FXML
    private TextField nilaiBaruField;
    @FXML
    private ComboBox<String> fotoEditBox;
    @FXML
    private Button butSimpanEdit;
    @FXML
    private VBox hapuProduk;
    @FXML
    private TextField cariHapusProduk;
    @FXML
    private Button butCariHapusProduk;
    @FXML
    private ImageView hasilPencarian;
    @FXML
    private Label pesanHapusLabel;
    @FXML
    private Button butHapusProduk;

    private final ProdukRepository repo = new ProdukRepository();

    private Produk produkDipilihEdit;
    private Produk produkDipilihHapus;

    private Node kartuDipilihEdit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fotoBox.getItems().addAll(DAFTAR_GAMBAR);
        fotoBox.setValue("susu.png");
        fotoEditBox.getItems().addAll(DAFTAR_GAMBAR);

        pilihanEditBox.getItems().addAll(EDIT_NAMA, EDIT_HARGA, EDIT_GAMBAR);
        pilihanEditBox.valueProperty().addListener((obs, lama, baru) -> aturInputEdit(baru));

        menuPilihan.getItems().addAll(MENU_TAMBAH, MENU_EDIT, MENU_HAPUS);
        menuPilihan.valueProperty().addListener((obs, lama, baru) -> gantiPanel(baru));
        menuPilihan.setValue(MENU_TAMBAH);

        muatKatalog();
    }

    private void gantiPanel(String menu) {
        tampilkan(tambahProduk, MENU_TAMBAH.equals(menu));
        tampilkan(pencarianProduk, MENU_EDIT.equals(menu));
        tampilkan(hapuProduk, MENU_HAPUS.equals(menu));

        setPesan(pesanLabel, "", null);
        resetPanelEdit();
        resetPanelHapus();
    }

    @FXML
    private void saveProduct(ActionEvent event) {
        long harga;
        try {
            harga = Long.parseLong(hargaField.getText().trim());
        } catch (NumberFormatException e) {
            setPesan(pesanLabel, "harga harus berupa angka", STYLE_ERROR);
            return;
        }

        try {
            Produk produk = new Produk(tambahField.getText().trim(), harga, fotoBox.getValue());
            repo.add(produk);

            setPesan(pesanLabel, "Produk \"" + produk.getNama() + "\" berhasil ditambahkan", STYLE_BERHASIL);
            tambahField.clear();
            hargaField.clear();
            tambahField.requestFocus();
            muatKatalog();
        } catch (IllegalArgumentException e) {
            setPesan(pesanLabel, e.getMessage(), STYLE_ERROR);
        } catch (SQLException e) {
            setPesan(pesanLabel, "Produk gagal disimpan", STYLE_ERROR);
        }
    }

    private void tampilkanHasilEdit(String kata) {
        resetPilihanEdit();
        hasilEditPane.getChildren().clear();
        try {
            List<Produk> hasil = repo.searchByNama(kata);
            for (Produk produk : hasil) {
                Node kartu = buatKartu(produk);
                kartu.getStyleClass().add("kartu-dapat-dipilih");
                kartu.setOnMouseClicked(e -> pilihUntukEdit(produk, kartu));
                hasilEditPane.getChildren().add(kartu);
            }
            jumlahLabel1.setText("ditemukan: " + hasil.size() + "produk");

            if (!hasil.isEmpty()) {
                setPesan(pesanLabel1, "klik produk yang ingin diubah", STYLE_ERROR);
            } else if (kata.isEmpty()) {
                setPesan(pesanLabel1, "Belum ada produk di katalog.", STYLE_ERROR);
            } else {
                setPesan(pesanLabel1, "Tidak ada produk dengan nama \"" + kata + "\".", STYLE_ERROR);
            }
        } catch (SQLException | IOException e) {
            setPesan(pesanLabel1, "pencarian produk gagal.", STYLE_ERROR);
        }
    }

    @FXML
    private void cariProdukAction(ActionEvent event) {
        tampilkanHasilEdit(cariField.getText().trim());
    }

    private void pilihUntukEdit(Produk produk, Node kartu) {
        if (kartuDipilihEdit != null) {
            kartuDipilihEdit.getStyleClass().remove("karu-dipilih");
        }
        kartuDipilihEdit = kartu;
        kartu.getStyleClass().add("kartu-dipilih");
        produkDipilihEdit = produk;
        barisEdit.setDisable(false);
        if (pilihanEditBox.getValue() == null) {
            pilihanEditBox.setValue(EDIT_NAMA);
        }
        aturInputEdit(pilihanEditBox.getValue());
        setPesan(pesanLabel1, "dipilih: " + produk.getNama() + ". pilih data yang ingin diubah", null);
    }

    private void aturInputEdit(String pilihan) {
        boolean ubahFoto = EDIT_GAMBAR.equals(pilihan);
        tampilkan(nilaiBaruField, !ubahFoto);
        tampilkan(fotoEditBox, ubahFoto);
        if (produkDipilihEdit == null || pilihan == null) {
            nilaiBaruField.clear();
            nilaiBaruField.setPromptText("");
            return;
        }
        switch (pilihan) {
            case EDIT_NAMA -> {
                nilaiBaruField.setPromptText("Nama Baru");
                nilaiBaruField.setText((produkDipilihEdit.getNama()));
            }
            case EDIT_HARGA -> {
                nilaiBaruField.setPromptText("harga baru");
                nilaiBaruField.setText(String.valueOf(produkDipilihEdit.getHarga()));
            }
            case EDIT_GAMBAR ->
                fotoEditBox.setValue(produkDipilihEdit.getFoto());
        }
    }

    @FXML
    private void simpanEdit(ActionEvent event) {
        if (produkDipilihEdit == null) {
            setPesan(pesanLabel1, "Klik produk yang ingin diubah terlebih dulu.", STYLE_ERROR);
            return;
        }
        String pilihan = pilihanEditBox.getValue();
        if (pilihan == null) {
            setPesan(pesanLabel1, "pilih data yang ingin diubah.", STYLE_ERROR);
            return;
        }

        try {
            switch (pilihan) {
                case EDIT_NAMA ->
                    produkDipilihEdit.setNama(nilaiBaruField.getText().trim());
                case EDIT_HARGA ->
                    produkDipilihEdit.setHarga(Long.parseLong(nilaiBaruField.getText().trim()));
                case EDIT_GAMBAR ->
                    produkDipilihEdit.setFoto(fotoEditBox.getValue());
                default -> {
                    return;
                }
            }
            repo.update(produkDipilihEdit);
            String nama = produkDipilihEdit.getNama();
            if (EDIT_NAMA.equals(pilihan)) {
                cariField.setText(nama);
            }
            muatKatalog();
            tampilkanHasilEdit(cariField.getText().trim());
            setPesan(pesanLabel1, "perubahan pada \"" + nama + "\" sudah disimpan", STYLE_BERHASIL);
        } catch (NumberFormatException e) {
            setPesan(pesanLabel1, "harga harus berubah angka", STYLE_ERROR);
        } catch (IllegalArgumentException e) {
            setPesan(pesanLabel1, e.getMessage(), STYLE_ERROR);
        } catch (SQLException e) {
            setPesan(pesanLabel1, "perubahan gagal disimpan", STYLE_ERROR);
        }
    }

    private void resetPilihanEdit() {
        produkDipilihEdit = null;
        kartuDipilihEdit = null;
        barisEdit.setDisable(true);
        pilihanEditBox.setValue(null);
        nilaiBaruField.clear();
        fotoEditBox.setValue(null);
    }

    private void resetPanelEdit() {
        resetPilihanEdit();
        cariField.clear();
        hasilEditPane.getChildren().clear();
        jumlahLabel1.setText("ditemukan : 0 produk");
        setPesan(pesanLabel1, "", null);
    }

    @FXML
    private void cariUntukHapus(ActionEvent event) {
        resetHasilHapus();
        String kata = cariHapusProduk.getText().trim();
        if (kata.isEmpty()) {
            setPesan(pesanHapusLabel, "ketik nama produk yang ingin dihapus", STYLE_ERROR);
            return;
        }

        try {
            List<Produk> hasil = repo.searchByNama(kata);
            Produk cocok = searchHapus(hasil, kata);
            if (hasil.isEmpty()) {
                setPesan(pesanHapusLabel, "ketik nama produk TIDAK DITEMUKAN", STYLE_ERROR);
            } else if (cocok == null) {
                setPesan(pesanHapusLabel, "Ada " + hasil.size()
                        + " produk yang cocok. Ketik nama yang lebih lengkap supaya hanya satu yang cocok.", STYLE_ERROR);
            } else {
                long jumlahSama = hasil.stream()
                        .filter(p -> p.getNama().equalsIgnoreCase(kata))
                        .count();
                produkDipilihHapus = cocok;
                hasilPencarian.setImage(muatGambar(cocok.getFoto()));
                String info = "ID " + cocok.getIdProduk() + " - " + cocok.getNama() + " - " + cocok.getHarga();
                if (jumlahSama > 1) {
                    info += " (ada " + jumlahSama + " produk bernama sama, yang ini dihapus lebih dulu)";
                }
                setPesan(pesanHapusLabel, info, null);
                butHapusProduk.setDisable(false);
            }
        } catch (SQLException e) {
            setPesan(pesanHapusLabel, "pencarian produk gagal", STYLE_ERROR);
        }
    }

    private Produk searchHapus(List<Produk> hasil, String kata) {
        List<Produk> persis = hasil.stream().filter(p -> p.getNama().equalsIgnoreCase(kata)).toList();
        if (persis.size() == 1) {
            return persis.get(0);
        }
        if (!persis.isEmpty()) {
            return hasil.get(0);
        }
        return null;
    }

    @FXML
    private void hapusProdukDipilih(ActionEvent event) {
        if (produkDipilihHapus == null) {
            return;
        }
        String nama = produkDipilihHapus.getNama();

        ButtonType hapus = new ButtonType("Hapus", ButtonBar.ButtonData.OK_DONE);
        ButtonType batal = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert konfirmasi = new Alert(AlertType.CONFIRMATION);
        konfirmasi.setTitle("Hapus Produk");
        konfirmasi.setHeaderText(null);
        konfirmasi.setContentText("Hapus produk \"" + nama + "\"?");
        konfirmasi.getButtonTypes().setAll(hapus, batal);
        Optional<ButtonType> jawaban = konfirmasi.showAndWait();
        if (jawaban.isEmpty() || jawaban.get() != hapus) {
            return;
        }

        try {
            repo.delete(produkDipilihHapus.getIdProduk());
            resetHasilHapus();
            cariHapusProduk.clear();
            setPesan(pesanHapusLabel, "produk " + nama + " sudah terhapus", STYLE_BERHASIL);
            muatKatalog();
        } catch (SQLException e) {
            e.printStackTrace();
            setPesan(pesanHapusLabel, "produk gagal dihapus", STYLE_ERROR);
        }
    }

    private void resetHasilHapus() {
        produkDipilihHapus = null;
        hasilPencarian.setImage(null);
        butHapusProduk.setDisable(true);
        setPesan(pesanHapusLabel, "", null);
    }

    private void resetPanelHapus() {
        cariHapusProduk.clear();
        resetHasilHapus();
    }

    // fungi untuk muat katalog
    private void muatKatalog() {
        try {
            List<Produk> semua = repo.getAll();
            jumlahLabel.setText("jumlah produk: " + semua.size());
            katalogPane.getChildren().clear();
            if (semua.isEmpty()) {
                Label kosong = new Label("belum ada produk di katalog, silahkan pilih menu tambah produk");
                kosong.getStyleClass().add("kosong");
                katalogPane.getChildren().add(kosong);
            }
            for (Produk produk : semua) {
                katalogPane.getChildren().add(buatKartu(produk));
            }
        } catch (SQLException | IOException e) {
            Label gagal = new Label("katalog gagal dimuat.");
            gagal.getStyleClass().add("kosong");
            katalogPane.getChildren().add(gagal);

        }
    }

    private Node buatKartu(Produk produk) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kartu-Katalog-FXML.fxml"));
        Node kartu = loader.load();
        KartuKatalogController kontrol = loader.getController();
        kontrol.setData(produk);
        return kartu;
    }

    // Fungsi bantuan karena banyak dipakai dan terlihat rapih
    private long bacaHarga(String teks) {
        String angka = teks.trim().replace(".", "").replace(" ", "");
        return Long.parseLong(angka);
    }

    private void tampilkan(Node node, boolean tampil) {
        node.setVisible(tampil);
        node.setManaged(tampil);
    }

    /**
     * ini untuk menulis pesan sesuai dengan gaya label, "pesan-berhasil"
     * "pesan-error" atau "null"
     */
    private void setPesan(Label label, String teks, String gaya) {
        label.getStyleClass().removeAll(STYLE_BERHASIL, STYLE_ERROR);
        if (gaya != null) {
            label.getStyleClass().add(gaya);
        }
        label.setText(teks);
    }

    public static Image muatGambar(String namaFile) {

        if (namaFile == null || namaFile.isBlank()) {
            return null;
        }
        Image gambar = CACHE_GAMBAR.get(namaFile);
        if (gambar == null) {
            URL lokasi = KatalogController.class.getResource("/image/" + namaFile);
            if (lokasi != null) {
                gambar = new Image(lokasi.toExternalForm());
                CACHE_GAMBAR.put(namaFile, gambar);
            }
        }
        return gambar;
    }
}
