package com.firmantour.travelingsolution;

public class ModelMobil2 {
    private String platnomor, namamerk, namamobil, warna, jumlahkursi, harga;

    public ModelMobil2(){

    }

    public ModelMobil2(String platnomor, String namamerk, String namamobil, String warna, String jumlahkursi, String harga) {
        this.platnomor = platnomor;
        this.namamerk = namamerk;
        this.namamobil = namamobil;
        this.warna = warna;
        this.jumlahkursi = jumlahkursi;
        this.harga = harga;
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
