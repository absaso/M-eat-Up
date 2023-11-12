package com.example.server.Restaurents;

import com.example.server.Reviews.Review;
import javax.persistence.*;
import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Restaurants")
public class RestaurantDAO {
    @Id
    public String id;
    public String alias;
    public String name;
    public String image_url;
    public String url;
    @Column(precision=16,scale=8)
    public BigDecimal x;
    @Column(precision=16,scale=8)
    public BigDecimal y;
    public String price;
    public String phone;
    @ElementCollection
    public List<String>  categories;
    public BigDecimal rating;
    public int review_count;
    public String sentiment ;
    
    @OneToMany(mappedBy = "Restaurant", cascade = CascadeType.ALL, orphanRemoval = false)
    public List<Review> review;

    public RestaurantDAO() {

    }

    public List<Review> getReview() {
        return review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }

    public RestaurantDAO(String id, String alias, String name, String image_url, String url, BigDecimal x, BigDecimal y,List<String>  categories, String price, String phone,BigDecimal rating, int review_count,String sentiment) {
        this.id = id;
        this.alias = alias;
        this.name = name;
        this.image_url = image_url;
        this.url = url;
        this.x = x;
        this.y = y;
        this.price = price;
        this.phone = phone;
        this.rating = rating;
        this.categories = categories;
        this.review_count = review_count;
        this.sentiment = sentiment ;
    }

    public RestaurantDAO(String id, String alias, String name, String image_url, String url, BigDecimal x, BigDecimal y, String price, String phone,BigDecimal rating, int review_count,String sentiment) {
        this.id = id;
        this.alias = alias;
        this.name = name;
        this.image_url = image_url;
        this.url = url;
        this.x = x;
        this.y = y;
        this.price = price;
        this.phone = phone;
        this.rating = rating;
        this.review_count = review_count;
        this.sentiment = sentiment ;
    }


    public int getReview_count() {
        return review_count;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(JSONArray categories) {
        List<String> cat = new ArrayList<>();
        for (int i=0; i<categories.length();i++){
            cat.add(categories.get(i).toString());
        }
        this.categories = cat;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
     public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
    public String getSentiment() {
        return sentiment;
    }
    public void setAlias(String alias) {
        this.alias = alias; }
   public String getAlias() {
        return alias;
   }
   public void setName(String name) {
        this.name = name;}
    public String getName() {
        return name;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public String getImage_url() {
        return image_url;
    }
    public void setUrl(String url) {
        this.url = url;}
    public String getUrl() {
        return url;
    }
    public void setX(BigDecimal x) {
        this.x = x;}
    public BigDecimal getX() {
        return x;
    }
    public void setY(BigDecimal y) {
        this.y = y;}
    public BigDecimal getY() {
        return y;
    }
    public void setPrice(String price) {
        this.price = price;}
    public String getPrice() {
        return price;
    }
    public void setPhone(String phone) {
        this.phone = phone;}
    public String getPhone() {
        return phone;
    }
    public RestaurantDAO(String id, String alias, String name, String image_url, String url, BigDecimal x, BigDecimal y, String price, String phone) {
        this.id = id;
        this.alias = alias;
        this.name = name;
        this.image_url = image_url;
        this.url = url;
        this.x = x;
        this.y = y;
        this.price = price;
        this.phone = phone;
    }
    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", alias:'" + alias + '\'' +
                ", name:'" + name + '\'' +
                ", image_url:'" + image_url + '\'' +
                ", url:'" + url + '\'' +
                ", x:" + x +
                ", y:" + y +
                ", price:'" + price + '\'' +
                ", phone:'" + phone + '\'' +
                ", rating:'"  + rating+'\''+       '}';}

}

