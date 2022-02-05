package com.remilefaivre.projet;

public class DeviceType {
    private int id;
    private String name;
    private String urlPicture;


    public DeviceType(int id, String name, String urlPicture) {
        this.id = id;
        this.name = name;
        this.urlPicture = urlPicture;
    }


    public int getId() {
        return id;
    }
    public String getUrlPicture() {
        return urlPicture;
    }
    public String getName() {
        return name;
    }


    public String toString() { return name; }
}
