package com.firmantour.travelingsolution.model;

public class ModelWisata {
    private String foto, kodewisata, namawisata, harga;

    public ModelWisata(){

    }

    public ModelWisata(String foto, String kodewisata, String namawisata, String harga) {
        this.foto = foto;
        this.kodewisata = kodewisata;
        this.namawisata = namawisata;
        this.harga = harga;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getKodewisata() {
        return kodewisata;
    }

    public void setKodewisata(String kodewisata) {
        this.kodewisata = kodewisata;
    }

    public String getNamawisata() {
        return namawisata;
    }

    public void setNamawisata(String namawisata) {
        this.namawisata = namawisata;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
