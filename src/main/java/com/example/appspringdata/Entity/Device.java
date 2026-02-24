package com.example.appspringdata.Entity;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node
public class Device {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String deviceId;  

    private String deviceType;
    private String buildingName;
    private ZonedDateTime createdAt;
    private boolean isDeleted;
    private Long numShelfPositions;
    private String partNumber;
    private String deviceName;
    private ZonedDateTime updatedAt;

    @Relationship(value="HAS",direction=Relationship.Direction.OUTGOING)
    private List<ShelfPosition> shelfPositions;
    
    public List<ShelfPosition> getShelfPosition(){
        return this.shelfPositions;
    }

    public void setShelfPositions(List<ShelfPosition> shelfPositions){
        this.shelfPositions=shelfPositions;
    }

    public String getDeviceType(){
        return this.deviceType;
    }

    public void setDeviceType(String name){
        this.deviceType=name;
    }

    public String getBuildingName(){
        return this.buildingName;
    }

    public void setBuildingName(String name){
        this.buildingName=name;
    }

    public String getPartNumber(){
        return this.partNumber;
    }

    public void setPartNumber(String number){
        this.partNumber=number;
    }

    public String getDeviceName(){
        return this.deviceName;
    }

    public void setDeviceName(String deviceName){
        this.deviceName=deviceName;
    }

    public String getDeviceId(){
        return this.deviceId;
    }

    public void setDeviceId(String deviceId){
        this.deviceId=deviceId;
    }

    public boolean getIsDeleted(){
        return this.isDeleted;
    }

    public void setIsDeleted(boolean d){
        this.isDeleted=d;
    }

    public Long getNumShelfPosition(){
        return this.numShelfPositions;
    }

    public void setNumShelfPositions(Long num){
        this.numShelfPositions=num;
    }

    public ZonedDateTime getCreatedAt(){
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime t){
        this.createdAt=t;
    }

    public ZonedDateTime getUpdatedAt(){
        return this.updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime t){
        this.updatedAt=t;
    }
}
