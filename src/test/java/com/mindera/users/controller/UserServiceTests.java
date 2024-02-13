package com.mindera.users.controller;

import com.mindera.users.entity.User;
import com.mindera.users.exceptions.CannotBeEmptyOrNullException;
import com.mindera.users.exceptions.UserCannotChangeException;
import com.mindera.users.exceptions.UserNotFoundException;
import com.mindera.users.repository.UserRepository;
import com.mindera.users.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        User createdUser = userService.addUser(user);

        Assertions.assertNotNull(createdUser);
    }

    @Test
    public void testAddUserWithEmptyOrNullFieldsThrowsCannotBeEmptyOrNullException() {
        // username > empty
        User user1 = User.builder()
                .id(1L)
                .username("")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(CannotBeEmptyOrNullException.class, () -> userService.addUser(user1));

        // username > blank
        User user2 = User.builder()
                .id(1L)
                .username(" ")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(CannotBeEmptyOrNullException.class, () -> userService.addUser(user2));

        // password > empty
        User user3 = User.builder()
                .id(3L)
                .username("user123")
                .password("")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(CannotBeEmptyOrNullException.class, () -> userService.addUser(user3));

        // password > blank
        User user4 = User.builder()
                .id(4L)
                .username("user123")
                .password(" ")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(CannotBeEmptyOrNullException.class, () -> userService.addUser(user4));

        // email > empty
        User user5 = User.builder()
                .id(3L)
                .username("user123")
                .password("password123")
                .email("")
                .build();

        Assertions.assertThrows(CannotBeEmptyOrNullException.class, () -> userService.addUser(user5));

        // email > blank
        User user6 = User.builder()
                .id(4L)
                .username("user123")
                .password("password123")
                .email(" ")
                .build();

        Assertions.assertThrows(CannotBeEmptyOrNullException.class, () -> userService.addUser(user6));
    }

    @Test
    public void testAddUserWithEmptyPasswordThrowsCannotBeNullException() {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(CannotBeEmptyOrNullException.class, () -> userService.addUser(user));
    }

    @Test
    public void testAddUserWithEmptyEmailThrowsCannotBeNullException() {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("")
                .build();

        Assertions.assertThrows(CannotBeEmptyOrNullException.class, () -> userService.addUser(user));
    }

    @Test
    public void testGetUserById() {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> retrievedUser = userService.getUserById(user.getId());

        Assertions.assertNotNull(retrievedUser);
    }

    @Test
    public void testGetUserByIdReturnsNullThrowsUserNotFoundException() {
        User user = User.builder()
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    public void testPutUserWhenFindById() {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User updatedUser = User.builder()
                .id(1L)
                .username("user")
                .password("password123")
                .email("user@gmail.com")
                .build();

        userService.putUser(user.getId(), updatedUser);
        assertEquals("user", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("user@gmail.com", user.getEmail());
    }

    @Test
    public void testPutUserReturnsNullThrowsUserNotFoundException() {
        User user = User.builder()
                .id(null)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.putUser(user.getId(), user));
    }

    @Test
    public void testPutUserWithDifferentEmailThrowsUserCannotChangeException() {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User updatedUser = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("updatedUser@gmail.com")
                .build();

        Assertions.assertThrows(UserCannotChangeException.class, () -> userService.putUser(user.getId(), updatedUser));
    }


    @Test
    public void testPatchUserWithDifferentEmailThrowsUserCannotChangeException() {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User updatedUser = User.builder()
                .id(1L)
                .email("updatedUser@gmail.com")
                .build();

        Assertions.assertThrows(UserCannotChangeException.class, () -> userService.patchUser(user.getId(), updatedUser));

    }


}