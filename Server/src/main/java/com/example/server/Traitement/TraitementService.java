package com.example.server.Traitement;

import com.example.server.Restaurents.RestaurantDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.example.server.user.*;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TraitementService {

    public static UserRepository userRepository;


    public TraitementService() {
        super();
        this.userRepository = userRepository;




    }

    public static String startPutSentiment() {
        RestTemplate restTemplate = new RestTemplate();
        String restaurantDTO = restTemplate.getForObject("http://127.0.0.1:5000/sentiment", String.class);
        return restaurantDTO;
    }

    //recommendation
    public static String postUser(User userDTO) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject("http://127.0.0.1:5000/firstRecommendation",userDTO, String.class);
        return response;
    }
    public static String postUserClassifier(UserDTO userDTO) {
        /*
        String user_login = userDTO.getLogin();
        Optional<User> uOpt = userRepository.findByLogin(user_login);
        User user = uOpt.get();
        */
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject("http://127.0.0.1:5000/classifierRecommendation", userDTO, String.class);
        return  response;
    };


    public static String postUserClassifierAge(UserDTO userDTO) {
        //String user_login = userDTO.getLogin();
        //Optional<User> uOpt = userRepository.findByLogin(user_login);
        //User user = uOpt.get();
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject("http://127.0.0.1:5000/classifierRecommendationAge", userDTO, String.class);
        return  response;
    };

    public List<String> getlistStringId(String listeRestau) {
        List<String> list_restauId = new ArrayList<>();
        JSONArray jsonArray =  new JSONArray(listeRestau);
        for (int i=0  ; i<jsonArray.length() ; i++){
            JSONObject restau = jsonArray.getJSONObject(i);
            list_restauId.add(restau.get("id").toString());
        }

        return list_restauId ;

    }


}

