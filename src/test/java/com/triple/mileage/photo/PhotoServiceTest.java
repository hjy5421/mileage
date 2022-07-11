package com.triple.mileage.photo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class PhotoServiceTest {
    @InjectMocks
    private PhotoService photoService;
    @Mock
    private PhotoRepository photoRepository;
    @Captor
    private ArgumentCaptor<List<Photo>> photoListArgumentCaptor;
    @Captor
    private ArgumentCaptor<Set<Photo>> photoSetArgumentCaptor;


    private final String reviewId = "reviewId";
    private final List<String> photoIds = List.of("photoId1", "photoId2");

    private Photo makePhoto(String photoId) {
        return new Photo(photoId, reviewId);
    }

    @Test
    public void createPhotos() {
        //given
        List<Photo> photos = List.of(makePhoto("photoId1"), makePhoto("photoId2"));

        //when
        photoService.createPhotos(reviewId, photoIds);

        //then
        then(photoRepository).should().saveAll(photoListArgumentCaptor.capture());
        List<Photo> actual = photoListArgumentCaptor.getValue();
        assertEquals(2, actual.size());
    }

    @Test
    public void createPhotos_PhotoIdsSizeNull() {
        //given

        //when
        photoService.createPhotos(reviewId, Collections.emptyList());

        //then
        then(photoRepository).shouldHaveNoInteractions();
    }

    @Test
    public void existsPhoto() {
        //given
        given(photoRepository.existsByReviewId(reviewId)).willReturn(true);

        //when
        boolean actual = photoService.existsPhoto(reviewId);

        //then
        assertTrue(actual);
    }

    @Test
    public void updatePhotos_emptyPhotoIds() {
        //given

        //when
        photoService.updatePhotos(reviewId, Collections.emptyList());

        //then
        then(photoRepository).should().deleteAllByReviewId(reviewId);
    }


    @Test
    public void updatePhotos() {
        //given
        List<String> photoIds = List.of("photoId1", "photoId2");
        List<Photo> originPhotos = List.of(makePhoto("photoId1"), makePhoto("photoId3"));
        given(photoRepository.findAllByReviewId(reviewId)).willReturn(originPhotos);

        //when
        photoService.updatePhotos(reviewId, photoIds);

        //then
        then(photoRepository).should().deleteAll(photoSetArgumentCaptor.capture());
        Set<Photo> actualDeletePhotos = photoSetArgumentCaptor.getValue();
        then(photoRepository).should().saveAll(photoSetArgumentCaptor.capture());
        Set<Photo> actualAddPhotos = photoSetArgumentCaptor.getValue();

        assertEquals(1, actualDeletePhotos.size());
        assertEquals("photoId3", actualDeletePhotos.iterator().next().getPhotoId());
        assertEquals(1, actualAddPhotos.size());
        assertEquals("photoId2", actualAddPhotos.iterator().next().getPhotoId());
    }

    @Test
    public void deletePhotos() {
        //given
        List<Photo> photos=List.of(makePhoto("photoId1"), makePhoto("photoId2"));
        given(photoRepository.findAllByReviewId(reviewId)).willReturn(photos);

        //when
        photoService.deletePhotos(reviewId);

        //then
        then(photoRepository).should().deleteAll(photoListArgumentCaptor.capture());
        List<Photo> actual = photoListArgumentCaptor.getValue();
        assertEquals(2, actual.size());
    }
}