package com.firmantour.travelingsolution.model;

public class ModelRekening {
    String namabank, nomorrekening, atasnama;

    ModelRekening(){

    }

    public ModelRekening(String namabank, String nomorrekening, String atasnama) {
        this.namabank = namabank;
        this.nomorrekening = nomorrekening;
        this.atasnama = atasnama;
    }

    public String getNamabank() {
        return namabank;
    }

    public void setNamabank(String namabank) {
        this.namabank = namabank;
    }

    public String getNomorrekening() {
        return nomorrekening;
    }

    public void setNomorrekening(String nomorrekening) {
        this.nomorrekening = nomorrekening;
    }

    public String getAtasnama() {
        return atasnama;
    }

    public void setAtasnama(String atasnama) {
        this.atasnama = atasnama;
    }
}
