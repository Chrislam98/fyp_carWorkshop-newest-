package com.example.carworkshop;

public class Vehicle {
    private String brand, color, plateNum, userId;
    private boolean isSelected;

    public Vehicle(String brand, String color, String plateNum, String userId){
        this.brand = brand;
        this.color = color;
        this.plateNum = plateNum;
        this.userId = userId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Vehicle(){
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
