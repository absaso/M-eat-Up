package com.cpe.meatup;

import java.math.BigDecimal;
import java.util.List;

public class Restaurant {

    private String id;
    private String name;
    private String image_url;
    private String url;
    private BigDecimal x;
    private BigDecimal y;
    private String price;
    private String phone;
    private List<String> reviews;
    private List<User> users;
    private String sentiment;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    private float rating;

    public Restaurant() {
        name = "unknown";
    }

    public Restaurant(String id, String name, String image_url, String url, BigDecimal x, BigDecimal y, String price, String phone,String sentiment) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.url = url;
        this.x = x;
        this.y = y;
        this.price = price;
        this.phone = phone;
        this.sentiment = sentiment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}
