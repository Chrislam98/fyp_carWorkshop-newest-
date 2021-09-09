package com.example.carworkshop;

public class Appointment_service {
    String category, orderNumId, ownerId, price, servicename, time;

    public Appointment_service(String category, String orderNumId, String ownerId, String price, String servicename, String time) {
        this.category = category;
        this.orderNumId = orderNumId;
        this.ownerId = ownerId;
        this.price = price;
        this.servicename = servicename;
        this.time = time;
    }

    public Appointment_service() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
