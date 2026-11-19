/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author muama
 */
public class Produk {
    private long idProduk;
    private String nama;
    private long harga;
    private String foto;

    public Produk(long idProduk, String nama, long harga, String foto) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama produk wajib diisi.");
        }
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari nol.");
        }
        if (foto == null || foto.isBlank()) {
            throw new IllegalArgumentException("Pilih foto produk.");
        }

        
        this.idProduk = idProduk;
        this.nama = nama;
        this.harga = harga;
        this.foto = foto;
    }

    public Produk(String nama, long harga, String foto) {
        this(0, nama, harga, foto);
    }

    public long getIdProduk() {
        return idProduk;
    }

    public String getNama() {
        return nama;
    }

    public long getHarga() {
        return harga;
    }

    public String getFoto() {
        return foto;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    
    
}
