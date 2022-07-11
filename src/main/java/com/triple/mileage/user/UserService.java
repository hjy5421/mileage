package com.triple.mileage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
    사용자 포인트 수정
     */
    public User updateUserPoint(String userId, int changePoint) {
        User origin = userRepository.findByUserId(userId);
        origin.setUserPoint(origin.getUserPoint() + changePoint);
        return userRepository.save(origin);
    }

    /*
    사용자 포인트 조회
    */
    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    public boolean existsByUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }
}
