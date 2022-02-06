package com.remilefaivre.projet;

public class DeviceType {
    private int id;
    private String name;
    private String uriPicture;


    public DeviceType(int id, String name, String uriPicture) {
        this.id = id;
        this.name = name;
        this.uriPicture = uriPicture;
    }


    public int getId() {
        return id;
    }
    public String getUriPicture() {
        return uriPicture;
    }
    public String getName() {
        return name;
    }


    public String toString() { return name; }
}
