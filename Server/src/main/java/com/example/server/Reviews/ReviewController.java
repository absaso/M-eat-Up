package com.example.server.Reviews;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
@RestController
@RequestMapping(value = "/Reviews")
public class ReviewController {
    public final ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    @RequestMapping(method = RequestMethod.POST,value="/add")
    private void addReview(@RequestBody ReviewDTO reviewDTO) {
        System.out.println("addReview: " + reviewDTO);
        Review review = new Review(reviewDTO.getReview(),reviewDTO.getRaiting());
        reviewService.addReview(review,reviewDTO.getRestaurant_id());

    }
    @RequestMapping(method=RequestMethod.GET)
    private List<ReviewDTO>getReviews(){
        List<Review> reviews = reviewService.getReviews();
        List<ReviewDTO> reviewDTOS;
        reviewDTOS = reviews.stream().map(review -> new ReviewDTO(review.getReview(), review.getRestaurant().getId())).collect(Collectors.toList());
        return reviewDTOS;


    }


}
