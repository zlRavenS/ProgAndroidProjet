package com.remilefaivre.projet;

// Classe d'une image
public class Picture {
    private int idPicture;
    private String url;

    public Picture(int idPicture, String url) {
        this.idPicture = idPicture;
        this.url = url;
    }

    public int getIdPicture() {
        return idPicture;
    }
    public String getURL() {
        return url;
    }

    public String toString() { return url; }
}
