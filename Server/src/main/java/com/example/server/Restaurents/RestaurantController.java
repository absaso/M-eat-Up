package com.example.server.Restaurents;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@Controller
@CrossOrigin
@RestController
@RequestMapping(value = "/Restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @RequestMapping(method = RequestMethod.POST,value="/add")
    private void addRestaurant(@RequestBody JSONObject restaurant) {
        RestaurantDAO restaurantDAO = new RestaurantDAO();
        System.out.println(restaurant.toString());
        restaurantDAO.setId((String) restaurant.get("id"));
        restaurantDAO.setAlias((String) restaurant.get("alias"));
        restaurantDAO.setImage_url((String) restaurant.get("image_url"));
        restaurantDAO.setName((String) restaurant.get("name"));
        restaurantDAO.setPhone((String) restaurant.get("phone"));
        if (!restaurant.isNull("price")){
            restaurantDAO.setPrice((String) restaurant.get("price"));}
        restaurantDAO.setUrl((String) restaurant.get("url"));
        restaurantDAO.setX((BigDecimal) restaurant.get("x"));
        restaurantDAO.setY((BigDecimal) restaurant.get("y"));
        restaurantDAO.setReview_count((Integer) restaurant.get("review_count"));
        restaurantDAO.setCategories((JSONArray) restaurant.getJSONArray("categories"));
        restaurantDAO.setRating((BigDecimal) restaurant.get("rating"));
        restaurantService.addRestaurant(restaurantDAO);
    }


    @RequestMapping(method = RequestMethod.POST,value="/modify")
    private void modifyRestaurant(@RequestBody RestaurantDTO restaurantDTO){
         restaurantService.modify(restaurantDTO);

    }


    // on reçoit la postion actuelle de l'user
    @RequestMapping(method = RequestMethod.POST,value="/currentUserLocation")
    private List<RestaurantDTO> getCurrentUserLocation(@RequestBody Coords userLocation) {
        System.out.println("getting User current location " + userLocation.getX().toString() );
        System.out.println(restaurantService.getNearestRestos(userLocation) );
        return restaurantService.getNearestRestos(userLocation);
    }

    @RequestMapping(method = RequestMethod.GET)
    private List<RestaurantDAO> getRestaurants(){
        List<RestaurantDAO> restos = restaurantService.getRestaurants();
        System.out.println(restos.toString());
        return restaurantService.getRestaurants();
    }

    @RequestMapping(method = RequestMethod.GET,value="/{id}")
    private RestaurantDAO getSpecificResto (@PathVariable String id){
            return restaurantService.getRestaurants(id);

    }
    @RequestMapping(method = RequestMethod.GET,value="/offset/{offset}")
    private List<RestaurantDTO> getRestaurants2(@PathVariable int offset){
        List<RestaurantDTO> restos = restaurantService.getRestaurants2(offset);

        return restos;
    }

}
