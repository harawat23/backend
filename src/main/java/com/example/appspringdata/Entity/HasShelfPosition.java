package com.example.appspringdata.Entity;

import java.time.ZonedDateTime;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class HasShelfPosition {
    @RelationshipId
    private String id;

    private ZonedDateTime createdAt;
    private boolean isDeleted;

    public boolean isIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @TargetNode
    private ShelfPosition shelfPosition;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ShelfPosition getShelfPosition() {
        return this.shelfPosition;
    }

    public void setShelfPosition(ShelfPosition shelfPosition) {
        this.shelfPosition = shelfPosition;
    }

}