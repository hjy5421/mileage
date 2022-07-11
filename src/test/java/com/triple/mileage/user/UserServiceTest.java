package com.triple.mileage.user;

import com.triple.mileage.photo.Photo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private final String userId="userId";

    private User makeUser(int userPoint){
        return new User(userId, userPoint);

    }
    @Test
    public void updateUserPoint() {
        //given
        User user=makeUser(100);
        given(userRepository.findByUserId(userId)).willReturn(user);
        User expected=makeUser(200);
        given(userRepository.save(any())).willReturn(expected);

        //when
        User actual=userService.updateUserPoint(userId,100);

        //then
        assertEquals(expected.getUserPoint(),actual.getUserPoint());
    }

    @Test
    public void getUser() {
        //given
        User expected=makeUser(100);
        given(userRepository.findByUserId(userId)).willReturn(expected);

        //when
        User actual=userService.getUser(userId);

        //then
        assertEquals(actual.getUserId(),expected.getUserId());
        assertEquals(actual.getUserPoint(),expected.getUserPoint());
    }

    @Test
    public void existsByUserId() {
        //given
        given(userRepository.existsByUserId(userId)).willReturn(true);

        //when
        boolean actual=userService.existsByUserId(userId);

        //then
        assertTrue(actual);
    }
}