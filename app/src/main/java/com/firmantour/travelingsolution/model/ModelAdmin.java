package com.firmantour.travelingsolution.model;

public class ModelAdmin {
    String namaadmin, nomortelepon, password, sebagai;

    public ModelAdmin(){

    }

    public ModelAdmin(String namaadmin, String nomortelepon, String password, String sebagai) {
        this.namaadmin = namaadmin;
        this.nomortelepon = nomortelepon;
        this.password = password;
        this.sebagai = sebagai;

    }

    public String getNamaadmin() {
        return namaadmin;
    }

    public void setNamaadmin(String namaadmin) {
        this.namaadmin = namaadmin;
    }

    public String getNomortelepon() {
        return nomortelepon;
    }

    public void setNomortelepon(String nomortelepon) {
        this.nomortelepon = nomortelepon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSebagai() {
        return sebagai;
    }

    public void setSebagai(String sebagai) {
        this.sebagai = sebagai;
    }
}
