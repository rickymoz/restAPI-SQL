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

    public User patchUser(Long userId, Map<String, String> toUpdate) {
        User updatedUser = userRepository.findById(userId).orElse(null);
        if (updatedUser != null) {
            if (toUpdate.containsKey("username")) {
                updatedUser.setUsername(toUpdate.get("username"));
            }
            if (toUpdate.containsKey("password")) {
                updatedUser.setPassword(toUpdate.get("password"));
            }
            userRepository.save(updatedUser);
        }
        return updatedUser;
    }

    public User putUser(Long userId, User user) {
        User updatedUser = userRepository.findById(userId).orElse(null);
        if(updatedUser != null) {
            updatedUser.setUsername(user.getUsername());
            updatedUser.setPassword(user.getPassword());
            userRepository.save(updatedUser);
        }
        return updatedUser;
    }
}
