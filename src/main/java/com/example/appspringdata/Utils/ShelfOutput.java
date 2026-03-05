package com.example.appspringdata.Utils;

import java.time.ZonedDateTime;

public class ShelfOutput {
    private String partNumber;
    private String shelfName;
    private String shelfId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getShelfId() {
        return this.shelfId;
    }

    public void setShelfId(String shelfId) {
        this.shelfId = shelfId;
    }

    public String getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getShelfName() {
        return this.shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public ShelfOutput(String partNumber, String shelfName) {
        this.partNumber = partNumber;
        this.shelfName = shelfName;
    }

    public ShelfOutput(String partNumber,
            String shelfName,
            String shelfId,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt) {
        this.partNumber = partNumber;
        this.shelfName = shelfName;
        this.shelfId = shelfId;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public ShelfOutput() {
    }
}
