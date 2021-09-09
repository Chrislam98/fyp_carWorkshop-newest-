package com.example.carworkshop;

public class UsersData {
    private String userId;
    private String username;
    private String email;
    private String gender;
    private String mobile;
    private String imageUrl;
    private String role;
    private String OwnerId;
    boolean permission;
    private String service;
    private String price;
    private String min;
    private String serdesc;
    private String status;




    public UsersData(String userId, String username, String email, String gender, String mobile, String imageURL, String role, String OwnerId, String service, String price, String min, boolean permission, String serdesc, String status) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.imageUrl = imageURL;
        this.role = role;
        this.permission = permission;
        this.OwnerId = OwnerId;
        this.service = service;
        this.price = price;
        this.min = min;
        this.serdesc = serdesc;
        this.status =  status;
    }

    public UsersData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImageURL() {
        return imageUrl;
    }

    public void setImageURL(String imageURL) {
        this.imageUrl = imageURL;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public String getSerdesc() {
        return serdesc;
    }

    public void setSerdesc(String serdesc) {
        this.serdesc = serdesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
