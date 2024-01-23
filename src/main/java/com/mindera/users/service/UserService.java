package com.mindera.users.service;

import com.mindera.users.entity.User;
import com.mindera.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User updateUserById(Long userId, Map<String, String> toUpdate) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            if (toUpdate.containsKey("username")) {
                user.setUsername(toUpdate.get("username"));
            }
            if (toUpdate.containsKey("password")) {
                user.setPassword(toUpdate.get("password"));
            }
            userRepository.save(user);
        }
        return user;
    }
}
