package com.firmantour.travelingsolution.model;

public class ModelPesanan {
    String key, nama, nomortelepon, alamat, platnomor, namamerk, namamobil, warna, jumlahkursi, tanggalsewa,
            tanggalkembali, totalharga, foto , statuspesanan;

    public ModelPesanan(){

    }

    public ModelPesanan(String foto,  String key, String nama, String nomortelepon, String alamat, String platnomor, String namamerk,
                        String namamobil, String warna, String jumlahkursi, String tanggalsewa, String tanggalkembali,
                        String totalharga, String statuspesanan) {
        this.foto = foto;
        this.key = key;
        this.nama = nama;
        this.nomortelepon = nomortelepon;
        this.alamat = alamat;
        this.platnomor = platnomor;
        this.namamerk = namamerk;
        this.namamobil = namamobil;
        this.warna = warna;
        this.jumlahkursi = jumlahkursi;
        this.tanggalsewa = tanggalsewa;
        this.tanggalkembali = tanggalkembali;
        this.totalharga = totalharga;
        this.statuspesanan = statuspesanan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomortelepon() {
        return nomortelepon;
    }

    public void setNomortelepon(String nomortelepon) {
        this.nomortelepon = nomortelepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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

    public String getTanggalsewa() {
        return tanggalsewa;
    }

    public void setTanggalsewa(String tanggalsewa) {
        this.tanggalsewa = tanggalsewa;
    }

    public String getTanggalkembali() {
        return tanggalkembali;
    }

    public void setTanggalkembali(String tanggalkembali) {
        this.tanggalkembali = tanggalkembali;
    }

    public String getTotalharga() {
        return totalharga;
    }

    public void setTotalharga(String totalharga) {
        this.totalharga = totalharga;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
