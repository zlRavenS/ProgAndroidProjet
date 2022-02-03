package com.remilefaivre.projet;

public class Room {
    private int id;
    private int idPicture;
    private String name;

    public Room(String name, int idPicture, int id) {
        this.name = name;
        this.idPicture = idPicture;
        this.id = id;
    }

    public int getIdPicture() {
        return idPicture;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String toString() { return name; }
}
