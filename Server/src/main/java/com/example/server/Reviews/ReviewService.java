package com.example.server.Reviews;

import com.example.server.Restaurents.RestaurantDAO;
import com.example.server.Restaurents.RestaurantRepo;
import com.example.server.Restaurents.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepo reviewRepo;
    private final RestaurantRepo restaurantRepo;

    public ReviewService(ReviewRepo reviewRepo, RestaurantRepo restaurantRepo) {
        super();
        this.reviewRepo = reviewRepo;
        this.restaurantRepo = restaurantRepo;
    }


    public List<Review> getReviews() {
        return reviewRepo.findAll();
    }

    public Review addReview(Review review, String restaurent_id) {

        if (review.getRestaurant() == null) {
            Optional<RestaurantDAO> restaurant = RestaurantService.getRestaurant(restaurent_id);
            if (restaurant.isPresent()) {
                review.setRestaurant(restaurant.get());
            }

        }

        return reviewRepo.save(review);

    }
    public Optional<Review> getReview(Long id) {
        return reviewRepo.findById(id);
    }



}

