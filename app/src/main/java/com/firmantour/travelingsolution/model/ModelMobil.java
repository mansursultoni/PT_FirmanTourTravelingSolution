package com.firmantour.travelingsolution.model;

public class ModelMobil {
    private String foto, platnomor, status, namamerk, namamobil, warna, jumlahkursi, harga;

    public ModelMobil(){

    }

    public ModelMobil(String foto, String platnomor, String status, String namamerk, String namamobil, String warna, String jumlahkursi, String harga) {
        this.foto = foto;
        this.platnomor = platnomor;
        this.status = status;
        this.namamerk = namamerk;
        this.namamobil = namamobil;
        this.warna = warna;
        this.jumlahkursi = jumlahkursi;
        this.harga = harga;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlatnomor() {
        return platnomor;
    }

    public void setPlatnomor(String platnomor) {
        this.platnomor = platnomor;
    }

    public String getNamamerk() {
        return namamerk;
    }

    public void setNamamerk(String namamerk) {
        this.namamerk = namamerk;
    }

    public String getNamamobil() {
        return namamobil;
    }

    public void setNamamobil(String namamobil) {
        this.namamobil = namamobil;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getJumlahkursi() {
        return jumlahkursi;
    }

    public void setJumlahkursi(String jumlahkursi) {
        this.jumlahkursi = jumlahkursi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
