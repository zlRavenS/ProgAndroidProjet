package com.remilefaivre.projet;

public class Room {
    private int idPicture;
    private String name;

    public Room(String name, int id) {
        this.name = name;
        this.idPicture = id;
    }

    public int getIdPicture() {
        return idPicture;
    }

    public String getName() {
        return name;
    }

    public String toString() { return name; }
}
