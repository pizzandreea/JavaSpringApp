package com.soupapp.soup.services;

import com.soupapp.soup.dtos.ReviewCreateDto;
import com.soupapp.soup.models.Review;
import com.soupapp.soup.models.Soup;
import com.soupapp.soup.models.User;
import com.soupapp.soup.repositories.ReviewRepository;
import com.soupapp.soup.repositories.SoupRepository;
import com.soupapp.soup.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final SoupRepository soupRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, SoupRepository soupRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.soupRepository = soupRepository;
        this.userRepository = userRepository;
    }

    public Review create(ReviewCreateDto reviewCreateDto, int soupId, int userId) {
        Soup soup = soupRepository.findById(soupId)
                .orElseThrow(() -> new RuntimeException("invalid soup id"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("invalid user id"));

        Review review = reviewCreateDto.toReview(new Review());
        review.setSoup(soup);
        review.setUser(user);

        return reviewRepository.save(review);
    }


    public void delete(Integer reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
        } else {
            throw new IllegalArgumentException("Review with ID " + reviewId + " not found");
        }
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public List<Review> getBySoupId(Integer soupId) {
        return reviewRepository.findBySoupId(soupId);
    }
}
