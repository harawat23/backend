package com.example.appspringdata.Utils;

import java.time.ZonedDateTime;

public class ShelfPositionOutput {
    private String shelfPosId;
    private ZonedDateTime createdAt;
    private String deviceId;
    private ZonedDateTime updatedAt;
    private ShelfOutput shelfOutput;

    public ShelfOutput getShelfOutput() {
        return this.shelfOutput;
    }

    public void setShelfOutput(ShelfOutput shelfOutput) {
        this.shelfOutput = shelfOutput;
    }

    public String getShelfPosId() {
        return this.shelfPosId;
    }

    public void setShelfPosId(String shelfPosId) {
        this.shelfPosId = shelfPosId;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ShelfPositionOutput() {
    }

    public ShelfPositionOutput(

    String shelfPosId,
    ZonedDateTime createdAt,
    String deviceId,
    ZonedDateTime updatedAt,
    ShelfOutput shelfOutput)
    {
        this.deviceId=deviceId;
        this.shelfPosId=shelfPosId;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.shelfOutput=shelfOutput;
    }
}
