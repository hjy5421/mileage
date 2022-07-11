package com.triple.mileage.photo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhotoService {
    private static final Logger log = LoggerFactory.getLogger(PhotoService.class);
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    /**
     * 사진 생성
     * @param reviewId
     * @param photoIds
     */
    public void createPhotos(String reviewId, List<String> photoIds) {
        if (CollectionUtils.isEmpty(photoIds)) {
            return;
        }

        List<Photo> photos = photoIds.stream().map(photoId -> new Photo(photoId, reviewId)).collect(Collectors.toList());
        photoRepository.saveAll(photos);
    }

    /**
     * 리뷰에 사진 존재하는지 확인
     *
     * @param reviewId
     * @return
     */
    public boolean existsPhoto(String reviewId) {
        return photoRepository.existsByReviewId(reviewId);
    }

    /**
     * 리뷰 사진 수정
     *
     * @param reviewId
     * @param photoIds
     */
    public void updatePhotos(String reviewId, List<String> photoIds) {
        // 수정 대상 photoId가 없는 경우 전체 삭제
        if (CollectionUtils.isEmpty(photoIds)) {
            photoRepository.deleteAllByReviewId(reviewId);
            return;
        }

        List<Photo> originPhotos = photoRepository.findAllByReviewId(reviewId);
        List<String> originPhotoIds = originPhotos.stream().map(Photo::getPhotoId).collect(Collectors.toList());

        // 기존 - 신규 : 삭제
        Set<String> deletePhotoIds = new HashSet<>();
        deletePhotoIds.addAll(originPhotoIds);
        deletePhotoIds.removeAll(photoIds);
        Set<Photo> deletePhotos = deletePhotoIds.stream().map(photoId -> new Photo(photoId, reviewId)).collect(Collectors.toSet());
        photoRepository.deleteAll(deletePhotos);

        // 신규 - 기존 : 추가
        Set<String> addPhotoIds = new HashSet<>();
        addPhotoIds.addAll(photoIds);
        addPhotoIds.removeAll(originPhotoIds);
        Set<Photo> addPhotos = addPhotoIds.stream().map(photoId -> new Photo(photoId, reviewId)).collect(Collectors.toSet());
        photoRepository.saveAll(addPhotos);
    }

    /**
     * 사진 삭제
     * @param reviewId
     */
    public void deletePhotos(String reviewId) {
        List<Photo> originPhotos = photoRepository.findAllByReviewId(reviewId);
        if (!CollectionUtils.isEmpty(originPhotos)) {
            photoRepository.deleteAll(originPhotos);
        }
    }
}
