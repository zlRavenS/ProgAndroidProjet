package com.remilefaivre.projet;

public class Device {
    private String name;
    private int idPicture;
    private int idDeviceType;
    private int idRoom;

    public Device(String name, int idPicture, int idSensorType, int idRoom) {
        this.name = name;
        this.idPicture = idPicture;
        this.idDeviceType = idSensorType;
        this.idRoom = idRoom;
    }

    public int getIdPicture() {
        return idPicture;
    }
    public int getIdDeviceType() {
        return idDeviceType;
    }
    public int getIdRoom() {
        return idRoom;
    }

    public String getName() {
        return name;
    }

    public String toString() { return name; }
}
