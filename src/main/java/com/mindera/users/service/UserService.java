package com.mindera.users.service;

import com.mindera.users.entity.User;
import com.mindera.users.exceptions.*;
import com.mindera.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new CannotBeEmptyOrNullException("User email cannot be null or empty");
        }

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with the same email already exists");
        }

        if (user.getUsername().isEmpty() || user.getUsername().isBlank() || user.getPassword().isEmpty() || user.getPassword().isBlank()) {
            throw new CannotBeEmptyOrNullException("User ID, username, and password cannot be null or empty");
        }

        return userRepository.save(user);
    }


    public Optional<User> getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User Id not found!");
        }
        return userOptional;
    }


    public void deleteUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) throw new UserNotFoundException("User not found!");

        userRepository.deleteById(userId);
    }

    public User patchUser(Long userId, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(userId);

        if (existingUserOptional.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        User userToUpdate = existingUserOptional.get();

        if (!userToUpdate.getId().equals(updatedUser.getId())) throw new NotMatchingException("User id not matching body request");

        if(!userToUpdate.getEmail().equals(updatedUser.getEmail())) throw new UserCannotChangeException("User email cannot be updated!");


        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty() && !updatedUser.getUsername().isBlank()) {
            userToUpdate.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty() && !updatedUser.getPassword().isBlank()) {
            userToUpdate.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty() && !updatedUser.getEmail().isBlank()) {
            userToUpdate.setEmail(updatedUser.getEmail());
        }

        userRepository.save(userToUpdate);

        return userToUpdate;
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
