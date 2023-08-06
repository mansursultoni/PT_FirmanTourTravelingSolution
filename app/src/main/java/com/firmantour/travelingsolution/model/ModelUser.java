package com.firmantour.travelingsolution.model;

public class ModelUser {
    private String sebagai, nama, nomortelepon, tanggallahir, jeniskelamin, alamat, password;

    public ModelUser(){

    }

    public ModelUser(String sebagai, String nama, String nomortelepon, String tanggallahir, String jeniskelamin, String alamat, String password) {
        this.sebagai = sebagai;
        this.nama = nama;
        this.nomortelepon = nomortelepon;
        this.tanggallahir = tanggallahir;
        this.jeniskelamin = jeniskelamin;
        this.alamat = alamat;
        this.password = password;
    }

    public String getSebagai() {
        return sebagai;
    }
    public void setSebagai(String sebagai) {
        this.sebagai = sebagai;
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

    public String getTanggallahir() {
        return tanggallahir;
    }

    public void setTanggallahir(String tanggallahir) {
        this.tanggallahir = tanggallahir;
    }

    public String getJeniskelamin() {
        return jeniskelamin;
    }

    public void setJeniskelamin(String jeniskelamin) {
        this.jeniskelamin = jeniskelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
