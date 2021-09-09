package com.example.carworkshop;

public class Owner_review {
    String appointmentNum, description, ownerId, rating, review, reviewId, userEmail, userId, userName, userPhone, workshopAddress, workshopName;

    public Owner_review(String appointmentNum, String description, String ownerId, String rating,
                        String review, String reviewId, String userEmail, String userId,
                        String userName, String userPhone, String workshopAddress, String workshopName) {
        this.appointmentNum = appointmentNum;
        this.description = description;
        this.ownerId = ownerId;
        this.rating = rating;
        this.review = review;
        this.reviewId = reviewId;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.workshopAddress = workshopAddress;
        this.workshopName = workshopName;
    }

    public Owner_review() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppointmentNum() {
        return appointmentNum;
    }

    public void setAppointmentNum(String appointmentNum) {
        this.appointmentNum = appointmentNum;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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
}
