package com.firmantour.travelingsolution.model;

public class ModelPesanan {
    private String key, nama, nomortelepon, alamat, platnomor, namamerk,namamobil,warna, kursi,harga,
            tanggalsewa,tanggalkembali;

    public ModelPesanan() {

    }

    public ModelPesanan(String key, String nama, String nomortelepon, String alamat, String platnomor,
                        String namamerk, String namamobil, String warna, String kursi,
                        String harga, String tanggalsewa, String tanggalkembali) {
        this.key = key;
        this.nama = nama;
        this.nomortelepon = nomortelepon;
        this.alamat = alamat;
        this.platnomor = platnomor;
        this.namamerk = namamerk;
        this.namamobil = namamobil;
        this.warna = warna;
        this.kursi = kursi;
        this.harga = harga;
        this.tanggalsewa = tanggalsewa;
        this.tanggalkembali = tanggalkembali;
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

    public String getKursi() {
        return kursi;
    }

    public void setKursi(String kursi) {
        this.kursi = kursi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
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
}
