# Katalog Produk UPI Store

Aplikasi desktop sederhana untuk mengelola katalog produk, dibuat dengan **JavaFX** dan **SQLite**. Pengguna mengisi form (nama, harga, foto), menyimpannya, lalu produk langsung tampil sebagai kartu di katalog.

## Fitur

- Tambah produk baru melalui form (nama, harga, foto contoh).
- Katalog tampil sebagai kartu produk dalam grid yang bisa di-scroll.
- Data tersimpan permanen di database SQLite lokal, jadi tidak hilang saat aplikasi ditutup.
- Validasi input dengan pesan kesalahan yang jelas:
  - Nama produk wajib diisi.
  - Harga harus angka bulat dan lebih dari nol.
  - Foto produk wajib dipilih.
- Penghitung jumlah produk di bagian atas.
- Tampilan pengganti ("Gambar tidak tersedia" / "Gambar tidak terbaca") jika file gambar bermasalah.

## Screenshot


## Teknologi

| Komponen | Versi / Keterangan |
| --- | --- |
| Java (JDK) | 17 |
| JavaFX (`javafx-controls`, `javafx-fxml`) | 21 |
| SQLite JDBC (`org.xerial:sqlite-jdbc`) | 3.49.1.0 |
| Build tool | Maven |
| Plugin | `javafx-maven-plugin` 0.0.8 |
| Arsitektur | Model, Repository, Controller |

## Prasyarat

- **JDK 17** atau lebih baru
- **Apache Maven** (atau gunakan Maven bawaan IDE seperti NetBeans)

Semua dependensi (JavaFX dan SQLite JDBC) diunduh otomatis oleh Maven, jadi tidak perlu instalasi JavaFX SDK secara terpisah.

## Struktur Proyek

```
KatalogProduk/
├── pom.xml                               # Konfigurasi Maven
├── nbactions.xml                         # Aksi run NetBeans (javafx:run)
├── data/
│   └── katalog.db                        # Database SQLite (dibuat otomatis jika belum ada)
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── main/
    │   │   │   └── MainApp.java              # Entry point aplikasi JavaFX
    │   │   ├── controller/
    │   │   │   └── KatalogController.java    # Logika UI: form, validasi, render kartu
    │   │   ├── data/
    │   │   │   └── ProdukRepository.java     # Akses data: INSERT dan SELECT produk
    │   │   ├── db/
    │   │   │   └── Database.java             # Koneksi SQLite dan pembuatan tabel
    │   │   └── model/
    │   │       └── Produk.java               # Model produk beserta validasinya
    │   └── resources/
    │       ├── image/                        # Foto contoh: kopi.png, roti.png, susu.png
    │       └── view/
    │           ├── katalog.fxml              # Layout halaman katalog
    │           └── styles/app.css            # Styling aplikasi
    └── test/
        └── java/                             # (belum ada test)
```

## Cara Menjalankan

### Dengan Maven (terminal)

Dari folder root proyek (yang berisi `pom.xml`):

```bash
mvn clean javafx:run
```

Perintah ini mengunduh dependensi, mengompilasi kode, lalu menjalankan `main.MainApp`.

### Dengan NetBeans

1. Buka folder proyek lewat **File > Open Project**.
2. Klik kanan proyek, lalu pilih **Run**. Aksi ini sudah dipetakan ke `javafx:run` lewat `nbactions.xml`.

### Dengan IDE lain (IntelliJ IDEA / Eclipse)

Impor sebagai proyek Maven, lalu jalankan goal `javafx:run` dari panel Maven. Menjalankan class `main.MainApp` langsung dari tombol Run biasanya gagal dengan error *"JavaFX runtime components are missing"* karena `MainApp` mewarisi `Application`. Gunakan goal Maven di atas untuk menghindarinya.

## Cara Penggunaan

1. Jalankan aplikasi. Jendela **Katalog Produk UPI Store** akan terbuka.
2. Isi **Nama produk**, misalnya `Roti Cokelat`.
3. Isi **Harga (Rp)** dengan angka bulat, misalnya `10000`.
4. Pilih **Foto contoh** (`roti.png`, `susu.png`, atau `kopi.png`).
5. Klik **Simpan**. Produk muncul di katalog dan jumlah produk bertambah.

## Database

Database SQLite berada di:

```
<direktori-kerja>/data/katalog.db
```

Folder dan tabel dibuat otomatis saat aplikasi pertama kali dijalankan. Lokasi memakai `user.dir` (direktori tempat aplikasi dijalankan), jadi jalankan aplikasi dari root proyek agar selalu memakai file yang sama. Path database aktif dicetak ke console dengan awalan `DATABASE AKTIF:`.

File `data/katalog.db` yang ikut dalam proyek sudah berisi contoh data. Hapus file tersebut jika ingin memulai dari katalog kosong.

Skema tabel:

```sql
CREATE TABLE IF NOT EXISTS produk (
    id    INTEGER PRIMARY KEY,
    nama  TEXT    NOT NULL CHECK(length(trim(nama)) > 0),
    harga INTEGER NOT NULL CHECK(harga > 0),
    foto  TEXT    NOT NULL CHECK(length(trim(foto)) > 0)
);
```

## Batasan Saat Ini

- Baru mendukung **tambah** dan **tampil** produk (belum ada ubah dan hapus).
- Pilihan foto masih tetap pada tiga gambar contoh di `resources/image/`.
- Harga hanya menerima bilangan bulat tanpa pemisah ribuan.
- Belum ada unit test (`src/test/java` masih kosong).

## Pengembangan Selanjutnya

- Fitur edit dan hapus produk
- Upload foto produk sendiri
- Pencarian dan filter katalog
- Unit test untuk `Produk` dan `ProdukRepository`
