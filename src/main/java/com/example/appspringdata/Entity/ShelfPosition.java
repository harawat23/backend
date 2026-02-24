package com.example.appspringdata.Entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
public class ShelfPosition {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String shelfPosId;

    private ZonedDateTime createdAt;
    private boolean isDeleted;
    private String deviceId;
    private ZonedDateTime updatedAt;

    public ShelfPosition(){
    }

    public ShelfPosition(String shelfPosId,ZonedDateTime createdAt,boolean isDeleted,String deviceId,ZonedDateTime updatedAt){
        this.createdAt=createdAt;
        this.shelfPosId=shelfPosId;
        this.isDeleted=isDeleted;
        this.deviceId=deviceId;
        this.updatedAt=updatedAt;
    }

    @Relationship(value = "HAS" , direction = Relationship.Direction.OUTGOING)
    private Shelf shelf;

    public Shelf getShelf() {
        return this.shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
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

    public boolean isIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
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
}
