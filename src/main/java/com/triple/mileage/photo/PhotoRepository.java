package com.triple.mileage.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,String> {
    boolean existsByReviewId(String reviewId);
    List<Photo> findAllByReviewId(String reviewId);
    void deleteAllByReviewId(String reviewId);
}
