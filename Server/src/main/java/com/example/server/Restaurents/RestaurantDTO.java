package com.example.server.Restaurents;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDTO {
    public String id;
    public String alias;
    public String name;
    public String image_url;
    public String url;
    public BigDecimal x;
    public BigDecimal y;
    public String price;
    public String phone;
    public BigDecimal distance;
    public List<String> reviews;
    private float rating;
    public String sentiment;

    public RestaurantDTO() {

    }

    public RestaurantDTO(String id, String alias, String name, String image_url, String url, float x, float y, String price, String phone,String sentiment,float rating) {
        this.id = id;
        this.alias = alias;
        this.name = name;
        this.image_url = image_url;
        this.url = url;
        this.x = BigDecimal.valueOf(x);
        this.y = BigDecimal.valueOf(y);
        this.price = price;
        this.phone = phone;
        this.sentiment = sentiment;
        this.rating = rating;
    }
    public RestaurantDTO(String id, String alias, String name, String image_url, String url, float x, float y, String price, String phone,float rating) {
        this.id = id;
        this.alias = alias;
        this.name = name;
        this.image_url = image_url;
        this.url = url;
        this.x = BigDecimal.valueOf(x);
        this.y = BigDecimal.valueOf(y);
        this.price = price;
        this.phone = phone;
        this.rating = rating;
    }
    public RestaurantDTO(RestaurantDAO restaurant){
        this.id = restaurant.getId();
        this.alias = restaurant.getAlias();
        this.name = restaurant.getName();
        this.image_url = restaurant.getImage_url();
        this.url = restaurant.getUrl();
        this.x = restaurant.getX();
        this.y = restaurant.getY();
        this.price = restaurant.getPrice();
        this.phone = restaurant.getPhone();
        this.reviews = new ArrayList<>();
        this.rating = (restaurant.getRating()).floatValue();
        this.sentiment = restaurant.getSentiment();


    }


    public RestaurantDTO(String json) {
        JSONObject jsonObject = new JSONObject(json);
        this.id = jsonObject.getString("id");
        this.alias = jsonObject.getString("alias");
        this.name = jsonObject.getString("name");
        this.image_url = jsonObject.getString("image_url");
        this.url = jsonObject.getString("url");
        this.x = BigDecimal.valueOf((float) jsonObject.getDouble("x"));
        this.y = BigDecimal.valueOf((float) jsonObject.getDouble("y"));
        this.price = jsonObject.getString("price");
        this.phone = jsonObject.getString("phone");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;}

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
        this.url = url;}

    public BigDecimal getX() {
        return x;
    }

    public void setX(float x) {
        this.x = BigDecimal.valueOf(x);}

    public BigDecimal getY() {
        return y;
    }

    public void setY(float y) {
        this.y = BigDecimal.valueOf(y);}

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;}

    public void setDistance(@NotNull Coords coords) {
        BigDecimal xUser = coords.getX();
        BigDecimal yUser = coords.getY();
        BigDecimal xResto = this.x;
        BigDecimal yResto = this.y;


        BigDecimal dx = xResto.subtract(xUser);
        BigDecimal dy = yResto.subtract(yUser);
        BigDecimal dxSquared = dx.multiply(dx);
        BigDecimal dySquared = dy.multiply(dy);
        BigDecimal sum = dxSquared.add(dySquared);

        this.distance = sum;
    }
    public BigDecimal getDistance() {
        return this.distance;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
    public String getSentiment() {
        return sentiment;
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





