package com.example.server.Restaurents;

import com.example.server.Reviews.Review;
import com.example.server.user.UserDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private static RestaurantRepo restaurantRepo;



    public RestaurantService(RestaurantRepo restaurantRepo){
        super();
        this.restaurantRepo = restaurantRepo;
    }
    public static Optional<RestaurantDAO> getRestaurant(String id){
        return restaurantRepo.findById(id);
    }

    public RestaurantDAO getRestaurants(String id) {
        return restaurantRepo.getById(id);
    }

    public void putRestaurant(RestaurantDAO restaurantDAO){
        restaurantRepo.save(restaurantDAO);
    }


    public List<RestaurantDAO> getRestaurants() {
        return restaurantRepo.findAll();
    }
    public void  addRestaurant(RestaurantDAO restaurant) {
        if (restaurantRepo.existsById(restaurant.getId())) {
            System.out.println("Restaurant already exists");
        }
        else{
                System.out.println("Saving restaurant");
            restaurantRepo.save(restaurant);
        }

    }
    public List<RestaurantDTO> getRestaurants2(int offset) {
        List<RestaurantDAO> resto = restaurantRepo.findAll();
        List<RestaurantDTO> resto_final =new ArrayList<>();
        for(int i=offset;i<50+offset;i++){
           resto_final.add(new RestaurantDTO(resto.get(i)));        }
        return resto_final;

    }

    //receiving current location of the user

    
    public List<RestaurantDTO> getNearestRestos (Coords coords){

        List<RestaurantDAO> listRestos = getRestaurants();
        List<RestaurantDTO> listRestosDTO = new ArrayList<>();
        for (int i=0; i<listRestos.size(); i++){
            RestaurantDTO resto = new RestaurantDTO(listRestos.get(i));
            resto.setDistance(coords);
            listRestosDTO.add(resto);
            Collections.sort(listRestosDTO, new Comparator<RestaurantDTO>() {
                        @Override
                        public int compare(RestaurantDTO o1, RestaurantDTO o2) {
                            BigDecimal distance1 = o1.getDistance();
                            BigDecimal distance2 = o2.getDistance();
                            return distance1.compareTo(distance2);
                        }
        });}
        //System.out.println(listRestosDTO.size());

        List<RestaurantDTO> list_final = listRestosDTO.stream().limit(15).collect(Collectors.toList());

        for (RestaurantDTO rest:list_final){
            RestaurantDAO r = (RestaurantDAO) getRestaurants(rest.getId());
            List<Review> reviews = r.getReview();
            if (reviews.size()>3){
                int numberOfElements = 3;
                Random rand = new Random();

                for (int i = 0; i < numberOfElements; i++) {
                    int randomIndex = rand.nextInt(reviews.size());
                    Review randomElement = reviews.get(randomIndex);
                    reviews.remove(randomIndex);
                    rest.reviews.add(randomElement.getReview());
                         }

            }
            else{
                for (Review review : r.getReview()) {
                    rest.reviews.add(review.getReview());
                     }
                }

            }

        return list_final;
    }
    public void modify(RestaurantDTO restaurantDTO) {
        RestaurantDAO  restaurantDAO = getRestaurants(restaurantDTO.getId());
        restaurantDAO.setX(restaurantDTO.getX());
        restaurantDAO.setY(restaurantDTO.getY());
        restaurantRepo.save(restaurantDAO);


    }


    public List<RestaurantDTO> getlist (List<String> list_resto_id, UserDTO user) {
        List <RestaurantDTO> listRecommandes = new ArrayList<>();
        for (int i=0; i<list_resto_id.size(); i++){
            RestaurantDAO resto = getRestaurant(list_resto_id.get(i)).get();
            RestaurantDTO restoDto = new RestaurantDTO(resto);
            listRecommandes.add(restoDto);
        }
        List<RestaurantDTO> Listlist_final = new ArrayList<>();
        for (RestaurantDTO r:listRecommandes){
            if (!(Listlist_final.contains(r)) ){
                Listlist_final.add(r);
            }
        }
        

        return Listlist_final;

    }
    public List<RestaurantDTO>sendList(Coords coords,List <String> list,UserDTO user){
        List<RestaurantDTO> listRestos = getlist ( list,  user);
        List<RestaurantDTO> listRestos_toreturn = new ArrayList<>();
        for (int i=0; i<listRestos.size(); i++){
            RestaurantDTO resto = listRestos.get(i);
            resto.setDistance(coords);
            listRestos_toreturn.add(resto);
            Collections.sort(listRestos_toreturn, new Comparator<RestaurantDTO>() {
                @Override
                public int compare(RestaurantDTO o1, RestaurantDTO o2) {
                    BigDecimal distance1 = o1.getDistance();
                    BigDecimal distance2 = o2.getDistance();
                    return distance1.compareTo(distance2);
                }
            });}
        //System.out.println(listRestosDTO.size());

        List<RestaurantDTO> list_final = listRestos_toreturn.stream().limit(5).collect(Collectors.toList());

        return list_final;



    }

    public String listrestaurants (List listid){
        List <String> maliste = new ArrayList<>();
        for (int i =0 ;i<listid.size(); i++ ){
            RestaurantDAO restaurantDAO = getRestaurants(listid.get(i).toString());

            RestaurantDTO restaurantDTO = new RestaurantDTO(restaurantDAO);
            System.out.println(restaurantDTO.toString());
            maliste.add(restaurantDTO.toString());
        }
        return maliste.toString();
    }
}
