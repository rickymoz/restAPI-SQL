package com.mindera.users.service;

import com.mindera.users.entity.User;
import com.mindera.users.exceptions.CannotBeNullException;
import com.mindera.users.exceptions.NotMatchingException;
import com.mindera.users.exceptions.UserCannotChangeException;
import com.mindera.users.exceptions.UserNotFoundException;
import com.mindera.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        if(user.getUsername().isEmpty() || user.getUsername().isBlank() || user.getPassword().isEmpty() || user.getPassword().isBlank() || user.getEmail().isEmpty() || user.getEmail().isBlank())
            throw new CannotBeNullException("User Id user name and user email cannot be null or empty");

        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User Id not found!");
        }
        return userRepository.findById(userId);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User patchUser(Long userId, Map<String, String> toUpdate) {
        Optional<User> updatedUserOptional = userRepository.findById(userId);
        if (updatedUserOptional.isEmpty()) {
            System.out.println("User not found!");
            throw new UserNotFoundException("User not found!");
        }

        User updatedUser = updatedUserOptional.get();

        if (toUpdate.containsKey("username")) {
            updatedUser.setUsername(toUpdate.get("username"));
        }
        if (toUpdate.containsKey("password")) {
            updatedUser.setPassword(toUpdate.get("password"));
        }
        if (toUpdate.containsKey("email")) {
            updatedUser.setEmail(toUpdate.get("email"));
        }
        userRepository.save(updatedUser);

        return updatedUser;
    }

    public User putUser(Long userId, User user) {
        Optional<User> updatedUserOptional = userRepository.findById(userId);
        if(updatedUserOptional.isEmpty()) throw new UserNotFoundException("User not found!");

        User updatedUser = updatedUserOptional.get();
        if(!updatedUser.getId().equals(user.getId())) throw new NotMatchingException("UserId and request body id do not match");
        if(!updatedUser.getEmail().equals(user.getEmail())) throw new UserCannotChangeException("User email cannot be updated!");
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setEmail(user.getEmail());
        userRepository.save(updatedUser);

        return updatedUser;
    }
}
