package com.example.alumne.provaprojecte;

/**
 * Created by alumne on 12/05/16.
 */
public class Marked {
    private String titol;
    private String usuari;
    private String aceptat;
    private String idLlibre;
    private String idUsuari;
    private String idPropietari;
    private byte[] imatge;

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getUsuari() {
        return usuari;
    }

    public void setUsuari(String usuari) {
        this.usuari = usuari;
    }

    public String getAceptat() {
        return aceptat;
    }

    public void setAceptat(String aceptat) {
        this.aceptat = aceptat;
    }

    public byte[] getImatge() {
        return imatge;
    }

    public void setImatge(byte[] imatge) {
        this.imatge = imatge;
    }

    public String getIdLlibre() {
        return idLlibre;
    }

    public void setIdLlibre(String idLlibre) {
        this.idLlibre = idLlibre;
    }

    public String getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(String idUsuari) {
        this.idUsuari = idUsuari;
    }

    public String getIdPropietari() {
        return idPropietari;
    }

    public void setIdPropietari(String idPropietari) {
        this.idPropietari = idPropietari;
    }
}
