package com.example.appspringdata.Utils;

public class DeviceInput {
    private String deviceType;
    private String buildingName;
    private String partNumber;
    private String deviceName;
    
    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getBuildingName() {
        return this.buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    // Constructor
    public DeviceInput(String deviceType, String buildingName, String partNumber,
            String deviceName) {
        this.deviceType = deviceType;
        this.buildingName = buildingName;
        this.partNumber = partNumber;
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "{ " + this.buildingName + " " + this.deviceName + " " + this.deviceType + " " + this.partNumber + " }";
    }
}