package com.example.appspringdata.Utils;

public class ShelfPositionInput {
    private String deviceId;

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ShelfPositionInput(){}

    public ShelfPositionInput(String deviceId){
        this.deviceId=deviceId;
    }
}
