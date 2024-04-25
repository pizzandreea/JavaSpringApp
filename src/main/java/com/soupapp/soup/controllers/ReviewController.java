package com.soupapp.soup.controllers;

import com.soupapp.soup.dtos.ReviewCreateDto;
import com.soupapp.soup.models.Review;
import com.soupapp.soup.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/Reviews")
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<Review> create(@RequestBody ReviewCreateDto request,
                                          @RequestParam Integer soupId,
                                          @RequestParam Integer userId) throws URISyntaxException {
        Review reviewSaved = reviewService.create(request, soupId, userId);

        if (reviewSaved != null) {
            return new ResponseEntity<>(reviewSaved, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable Integer reviewId) {
        try {
            reviewService.delete(reviewId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Review>> getAll() {
        try {
            List<Review> reviews = reviewService.getAll();
            if (reviews.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(reviews);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getBySoupId/{soupId}")
    public ResponseEntity<List<Review>> getBySoupId(@PathVariable Integer soupId) {
        try {
            List<Review> reviews = reviewService.getBySoupId(soupId);
            if (reviews.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(reviews);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Additional methods can be added as needed
}
