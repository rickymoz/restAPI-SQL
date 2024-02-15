package com.mindera.users.service;

import com.mindera.users.entity.User;
import com.mindera.users.exceptions.*;
import com.mindera.users.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTests {

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
        assertEquals("user123", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }


    @Test
    public void testAddUserWithEmptyOrNullFieldsThrowsCannotBeEmptyOrNullException() {

        // user > null
        Assertions.assertThrows(UserCannotBeNullException.class, () -> userService.addUser(null));

        // username > null
        User user1 = User.builder()
                .id(1L)
                .username(null)
                .password("password123")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(UserPropertiesNullEmptyOrBlankException.class, () -> userService.addUser(user1));

        // username > empty
        User user2 = User.builder()
                .id(1L)
                .username("")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(UserPropertiesNullEmptyOrBlankException.class, () -> userService.addUser(user2));

        // username > blank
        User user3 = User.builder()
                .id(1L)
                .username(" ")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(UserPropertiesNullEmptyOrBlankException.class, () -> userService.addUser(user3));

        // password > null
        User user4 = User.builder()
                .id(3L)
                .username("user123")
                .password(null)
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(UserPropertiesNullEmptyOrBlankException.class, () -> userService.addUser(user4));

        // password > empty
        User user5 = User.builder()
                .id(3L)
                .username("user123")
                .password("")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(UserPropertiesNullEmptyOrBlankException.class, () -> userService.addUser(user5));

        // password > blank
        User user6 = User.builder()
                .id(4L)
                .username("user123")
                .password(" ")
                .email("user@gmail.com")
                .build();

        Assertions.assertThrows(UserPropertiesNullEmptyOrBlankException.class, () -> userService.addUser(user6));

        // email > null
        User user7 = User.builder()
                .id(3L)
                .username("user123")
                .password("password123")
                .email(null)
                .build();

        Assertions.assertThrows(UserPropertiesNullEmptyOrBlankException.class, () -> userService.addUser(user7));

        // email > empty
        User user8 = User.builder()
                .id(3L)
                .username("user123")
                .password("password123")
                .email("")
                .build();

        Assertions.assertThrows(UserPropertiesNullEmptyOrBlankException.class, () -> userService.addUser(user8));

        // email > blank
        User user9 = User.builder()
                .id(4L)
                .username("user123")
                .password("password123")
                .email(" ")
                .build();

        Assertions.assertThrows(UserPropertiesNullEmptyOrBlankException.class, () -> userService.addUser(user9));
    }

    @Test
    void testAddUserWithExistingEmail() {
        User existingUser = new User(2L, "existingUser", "password456", "existingUser@gmail.com");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(existingUser));
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

        Assertions.assertTrue(retrievedUser.isPresent());
        Assertions.assertNotNull(retrievedUser);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void testGetUserByIdReturnsNullThrowsUserNotFoundException() {
        User user = User.builder()
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(user.getId());

            verify(userRepository, times(1)).findById(user.getId());
        });
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

        verify(userRepository, times(1)).findById(user.getId());

        verify(userRepository, times(1)).save(updatedUser);
    }


    @Test
    public void testPutUserReturnsNullThrowsUserNotFoundException() {
        User user = User.builder()
                .id(null)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.putUser(user.getId(), user);

            verify(userRepository, times(1)).findById(user.getId());

            verify(userRepository, times(0)).save(any(User.class));
        });
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

        Assertions.assertThrows(UserCannotChangeException.class, () -> {
            userService.putUser(user.getId(), updatedUser);

            verify(userRepository, times(1)).findById(user.getId());

            verify(userRepository, times(0)).save(any(User.class));
        });
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

        Assertions.assertThrows(UserCannotChangeException.class, () -> {
            userService.patchUser(user.getId(), updatedUser);

            verify(userRepository, times(1)).findById(user.getId());

            verify(userRepository, times(0)).save(any(User.class));
        });
    }

}