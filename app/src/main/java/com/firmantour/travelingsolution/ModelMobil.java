package com.firmantour.travelingsolution;

public class ModelMobil {
    private String nama, nomor, harga, deskripsi,status, foto;

    public ModelMobil(){

    }

    public ModelMobil(String nama, String nomor, String harga, String deskripsi, String status, String foto) {
        this.nama = nama;
        this.nomor = nomor;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.status = status;
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
