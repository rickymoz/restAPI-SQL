package com.mindera.users.controller;

import com.mindera.users.entity.User;
import com.mindera.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

    @PatchMapping("/{userId}")
    public User patchUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        return userService.patchUser(userId, updatedUser);
    }


    @PutMapping("/{userId}")
    public User putUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        return userService.putUser(userId, updatedUser);
    }
}