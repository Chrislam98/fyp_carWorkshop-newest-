package com.example.carworkshop;

import java.io.Serializable;

public class Owner implements Serializable{
    private String appointmentNum, email, ownerId, role, username, workshopAddress, workshopName,status,mobile;
    private String imageUrl;

    public Owner() {
    }

    public Owner(String appointmentNum, String email, String imageUrl , String ownerId, String role, String username, String workshopAddress, String workshopName, String status, String mobile) {
        this.appointmentNum = appointmentNum;
        this.email = email;
        this.ownerId = ownerId;
        this.role = role;
        this.username = username;
        this.workshopAddress = workshopAddress;
        this.workshopName = workshopName;
        this.imageUrl = imageUrl;
        this.status = status;
        this.mobile = mobile;
    }

    public String getAppointmentNum() {
        return appointmentNum;
    }

    public void setAppointmentNum(String appointmentNum) {
        this.appointmentNum = appointmentNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkshopAddress() {
        return workshopAddress;
    }

    public void setWorkshopAddress(String workshopAddress) {
        this.workshopAddress = workshopAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUri(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
