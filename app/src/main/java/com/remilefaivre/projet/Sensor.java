package com.remilefaivre.projet;

public class Sensor {
    private String name;
    private int idPicture;
    private int idSensorType;
    private int idRoom;

    public Sensor(String name, int idPicture, int idSensorType, int idRoom) {
        this.name = name;
        this.idPicture = idPicture;
        this.idSensorType = idSensorType;
        this.idRoom = idRoom;
    }

    public int getIdPicture() {
        return idPicture;
    }
    public int getIdSensorType() {
        return idSensorType;
    }
    public int getIdRoom() {
        return idRoom;
    }

    public String getName() {
        return name;
    }

    public String toString() { return name; }
}
