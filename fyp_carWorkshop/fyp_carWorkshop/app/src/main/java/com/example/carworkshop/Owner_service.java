package com.example.carworkshop;

public class Owner_service {

    private String servicename;
    private String price;
    private String time;
    boolean permission;
    private String OwnerId;
    private String category;
    private String ser_desc;
    boolean isChecked;

    public Owner_service() {
    }

    public Owner_service(String servicename, String price, String time, boolean permission, String ownerId, String category, String ser_desc, boolean isChecked) {
        this.servicename = servicename;
        this.price = price;
        this.time = time;
        this.permission = permission;
        OwnerId = ownerId;
        this.category = category;
        this.ser_desc = ser_desc;
        this.isChecked = isChecked;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSer_desc() {
        return ser_desc;
    }

    public void setSer_desc(String ser_desc) {
        this.ser_desc = ser_desc;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}


