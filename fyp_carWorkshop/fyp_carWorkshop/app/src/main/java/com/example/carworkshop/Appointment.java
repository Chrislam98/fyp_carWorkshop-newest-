package com.example.carworkshop;

public class Appointment {
    String orderNumId, ownerId, appointmentDate,
            appointmentNum, appointmentTime, email,
            ownerName, userId, workshopAddress, workshopName, status, username, userPhone,
            carBrand, carPlateNum, imageUrl;
    public Appointment(String orderNumId, String ownerId, String appointmentDate, String appointmentNum, String appointmentTime,
                       String ownerName, String userId, String workshopAddress, String workshopName, String status, String username,
                        String carBrand, String carPlateNum, String imageUrl, String email, String userPhone) {
        this.orderNumId = orderNumId;
        this.ownerId = ownerId;
        this.appointmentDate = appointmentDate;
        this.appointmentNum = appointmentNum;
        this.appointmentTime = appointmentTime;
        this.ownerName = ownerName;
        this.userId = userId;
        this.workshopAddress = workshopAddress;
        this.workshopName = workshopName;
        this.status = status;
        this.username = username;
        this.carBrand = carBrand;
        this.carPlateNum = carPlateNum;
        this.imageUrl = imageUrl;
        this.email = email;
        this.userPhone = userPhone;
    }

    public Appointment() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarPlateNum() {
        return carPlateNum;
    }

    public void setCarPlateNum(String carPlateNum) {
        this.carPlateNum = carPlateNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNumId() {
        return orderNumId;
    }

    public void setOrderNumId(String orderNumId) {
        this.orderNumId = orderNumId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentNum() {
        return appointmentNum;
    }

    public void setAppointmentNum(String appointmentNum) {
        this.appointmentNum = appointmentNum;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkshopAddress() {
        return workshopAddress;
    }

    public void setWorkshopAddress(String workshopAddress) {
        this.workshopAddress = workshopAddress;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
