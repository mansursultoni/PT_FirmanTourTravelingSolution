package com.firmantour.travelingsolution.model;

public class Admin {
    String nama, nomortelepon, password;

    public Admin(String nama, String nomortelepon, String password) {
        this.nama = nama;
        this.nomortelepon = nomortelepon;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
