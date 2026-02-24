package com.example.appspringdata.Utils;

public class ShelfInput {
    private String partNumber;
    private String shelfName;

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

    public ShelfInput(String partNumber,String shelfName) {
        this.partNumber=partNumber;
        this.shelfName=shelfName;
    }

    public ShelfInput(){}
}
