package com.firmantour.travelingsolution;

public class ModelPemesanan {
    private String nama;
    private String notlep;
    private String alamat;
    private String platnomor;
    private String namamerk;
    private String namamobil;
    private String warna;
    private String jumlahkursi;
    private String totalharga;
    private String sewa;
    private String kembali;

    public ModelPemesanan(String platnomor, String namamobil, String namamerk){
        this.platnomor = platnomor;
        this.namamobil = namamobil;
        this.namamerk = namamerk;
    }

    public ModelPemesanan(String nama, String notlep, String alamat, String platnomor, String namamerk, String namamobil, String warna, String jumlahkursi, String totalharga, String sewa, String kembali) {
        this.nama = nama;
        this.notlep = notlep;
        this.alamat = alamat;
        this.platnomor = platnomor;
        this.namamerk = namamerk;
        this.namamobil = namamobil;
        this.warna = warna;
        this.jumlahkursi = jumlahkursi;
        this.totalharga = totalharga;
        this.sewa = sewa;
        this.kembali = kembali;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNotlep() {
        return notlep;
    }

    public void setNotlep(String notlep) {
        this.notlep = notlep;
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

    public String getTotalharga() {
        return totalharga;
    }

    public void setTotalharga(String totalharga) {
        this.totalharga = totalharga;
    }

    public String getSewa() {
        return sewa;
    }

    public void setSewa(String sewa) {
        this.sewa = sewa;
    }

    public String getKembali() {
        return kembali;
    }

    public void setKembali(String kembali) {
        this.kembali = kembali;
    }
}
