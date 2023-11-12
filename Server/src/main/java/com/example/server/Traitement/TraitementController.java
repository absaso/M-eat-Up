package com.example.server.Traitement;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.server.Restaurents.*;
import com.example.server.user.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.web.bind.annotation.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TraitementController{

    @Autowired
    TraitementService traitementService;
    RestaurantService restaurantService;
    UserService userService;


    public TraitementController(TraitementService traitementService,RestaurantService restaurantService,
            UserService userService) {

        this.traitementService = traitementService;
        this.restaurantService = restaurantService;
        this.userService= userService;
    }

    @RequestMapping(method= RequestMethod.GET,value="/sentiment")
    private void startPutSentiment(){
        TraitementService.startPutSentiment();
        }
    @RequestMapping(method=RequestMethod.PUT,value="/update")
    public void putRestaurant(@RequestBody String arrayRestaurant) throws JsonMappingException, JsonProcessingException{
        System.out.println("Objet reçu" + arrayRestaurant);

        JSONArray jsonArray =  new JSONArray(arrayRestaurant);
        JSONObject firstElement = jsonArray.getJSONObject(0);

        List <String> b = null;
        RestaurantDAO restaurantDAO = new RestaurantDAO(firstElement.get("id").toString(),
                firstElement.get("alias").toString(),firstElement.get("name").toString(),
                firstElement.get("image_url").toString(),
                (String) firstElement.get("url"), (BigDecimal) firstElement.get("x"),
                (BigDecimal) firstElement.get("y"), b ,firstElement.get("price").toString(),
                firstElement.getBigDecimal("phone").toString(),
                (BigDecimal) firstElement.get("rating"), (Integer) firstElement.get("review_count"),
                (String) firstElement.get("sentiment")
        );
        String categoriesString = firstElement.get("categories").toString();
        categoriesString = "[" + categoriesString.substring(1, categoriesString.length()-1) + "]";
        JSONArray cat = new JSONArray(categoriesString);
        restaurantDAO.setCategories(cat);
        restaurantService.putRestaurant(restaurantDAO);
    }
    @RequestMapping(method=RequestMethod.PUT,value="/suggestions/{user_login}")
    public void restaurantsPreference(@RequestBody String restaurant_id_s ,@PathVariable String user_login) {
        JSONObject jsonObject = new JSONObject(restaurant_id_s);
        String restaurant_id = jsonObject.get("id").toString();
        userService.addPreferences(user_login,restaurant_id);
    }


    @RequestMapping(method = RequestMethod.POST,value="/recommendation/{longitude}/{latitude}" )
    public List<RestaurantDTO> recommendation(@RequestBody UserDTO user,@PathVariable BigDecimal longitude,@PathVariable BigDecimal latitude){

        List<String> sugestions = new ArrayList<>();
        Coords coords = new Coords(longitude,latitude);
        User userdao = userService.getUserDAO(user);
        if (user.getHistorique_name().size()==0){
            System.out.println("on a rien das l'historique");
           // suggestions = restaurantService.listrestaurants(userdao.getRestaurants_suggere());
            sugestions =userdao.getRestaurants_suggere();
        }
        else if(user.getHistorique_name().size()>8) {
            System.out.println("on a 8  dans l'historique");

            String liste_restau  = postUserClassifier(user);
            sugestions =  traitementService.getlistStringId(liste_restau);

        }
        else {
            System.out.println("avec l'age");
            String liste_restau  = postUserClassifierAge(user);
            sugestions = traitementService.getlistStringId(liste_restau);

        }
        List<RestaurantDTO> rep = restaurantService.sendList(coords,sugestions,user);
        System.out.println("response" + rep);
        return rep;
    }



    @RequestMapping(method= RequestMethod.POST,value="/firstrecommendation")
    private void postUser(User userDTO){
        traitementService.postUser(userDTO);
        }

    //@RequestMapping(method= RequestMethod.POST,value="/classifierRecommendation")
    private String postUserClassifier(UserDTO userDTO){

        return traitementService.postUserClassifier(userDTO);
    }

    //@RequestMapping(method= RequestMethod.POST,value="/classifierRecommendationAge")
    private String postUserClassifierAge(UserDTO userDTO){
        String response =traitementService.postUserClassifierAge(userDTO);
        return response ;
    }

}
