package com.example.appspringdata.Utils;

import java.time.ZonedDateTime;
import java.util.List;

public class DeviceOutput {
    private String deviceId;
    private String deviceType;
    private String buildingName;
    private ZonedDateTime createdAt;
    private String partNumber;
    private String deviceName;
    private ZonedDateTime updatedAt;
    private Long numberOfShelfPositions;

    public Long getNumberOfShelfPositions() {
        return this.numberOfShelfPositions;
    }

    public void setNumberOfShelfPositions(Long numberOfShelfPositions) {
        this.numberOfShelfPositions = numberOfShelfPositions;
    }

    private List<ShelfPositionOutput> shelfPosition;

    public List<ShelfPositionOutput> getShelfPosition() {
        return this.shelfPosition;
    }

    public void setShelfPosition(List<ShelfPositionOutput> shelfPosition) {
        this.shelfPosition = shelfPosition;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

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

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
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

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DeviceOutput(){}

    public DeviceOutput(String deviceId,String deviceType,String buildingName,ZonedDateTime createdAt,String partNumber,String deviceName,ZonedDateTime updatedAt){
        this.deviceId=deviceId;
        this.deviceType=deviceType;
        this.buildingName=buildingName;
        this.createdAt=createdAt;
        this.partNumber=partNumber;
        this.deviceName=deviceName;
        this.updatedAt=updatedAt;
    }
}
