package com.example.server.Reviews;



public class ReviewDTO {
    private String review;
    private String Restaurant_id;
    private float raiting;

    public ReviewDTO(String review, String Restaurant_id) {
        this.review = review;
        this.Restaurant_id = Restaurant_id;
    }
    public  String getReview() {
        return review;
    }
    public void setReview(String review){
        this.review=review;
    }

    public float getRaiting() {
        return raiting;
    }

    public void setRaiting(float raiting) {
        this.raiting = raiting;
    }

    public String getRestaurant_id() {
        return Restaurant_id;}
    public void setRestaurant_id(String Restaurant_id) {
        this.Restaurant_id = Restaurant_id;
    }
}
