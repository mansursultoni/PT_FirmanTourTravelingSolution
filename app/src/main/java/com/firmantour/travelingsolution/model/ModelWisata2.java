package com.firmantour.travelingsolution.model;

public class ModelWisata2 {
    private String kodewisata, namawisata, harga;

    public ModelWisata2(){

    }

    public ModelWisata2( String kodewisata, String namawisata, String harga) {
        this.kodewisata = kodewisata;
        this.namawisata = namawisata;
        this.harga = harga;
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
