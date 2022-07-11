package com.triple.mileage.review;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    Review findFirstByPlaceIdOrderByCreateDateAsc(String placeId);
    Review findByReviewId(String reviewId);
    boolean existsByReviewId(String reviewId);
    boolean existsByUserIdAndPlaceId(String userId,String placeId);
}
