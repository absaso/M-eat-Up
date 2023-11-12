package com.example.server.Reviews;

import com.example.server.Restaurents.RestaurantDAO;
import javax.persistence.*;

@Table(name="Reviews")
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10000)
    private String review;
    @ManyToOne
    @JoinColumn(name = "RestaurentId")
    private RestaurantDAO Restaurant;
    private float raiting;

    public Review(){

    }
    public Review(String review,float raiting){
        this.review = review;
        this.raiting = raiting;
    }
    public float getRaiting() {
        return raiting;
    }

    public void setRaiting(float raiting) {
        this.raiting = raiting;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;}

    public RestaurantDAO getRestaurant() {
        return Restaurant;
    }
    public void setRestaurant(RestaurantDAO Restaurant) {
        this.Restaurant = Restaurant;
    }

}
