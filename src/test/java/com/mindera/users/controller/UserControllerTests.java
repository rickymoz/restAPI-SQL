package com.mindera.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.users.entity.User;
import com.mindera.users.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    User newUser = User.builder()
            .username("test")
            .password("test123")
            .email("test@gmail.com")
            .build();
    User USER_1 = User.builder()
            .id(2L)
            .username("Ricardo")
            .password("ricardo123")
            .email("ricardo@gmail.com")
            .build();
    User USER_2 = User.builder()
            .id(3L)
            .username("Marco")
            .password("marco123")
            .email("marco@gmail.com")
            .build();
    User USER_3 = User.builder()
            .id(4L)
            .username("Rodrigo")
            .password("rodrigo123")
            .email("rodrigo@gmail.com")
            .build();

    @BeforeEach
    public void setup(){
        List<User> users = new LinkedList<>(Arrays.asList(USER_1, USER_2, USER_3));
        Mockito.mock(UserRepository.class);
    }

    @Test
    void testPostUser() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Mockito.when(userRepository.save(user)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is("user123")));
    }


    // If ID, username, password or email null or empty -> HttpStatus.BAD_REQUEST -> "User ID username, password and user email cannot be null or empty"
    @Test
    void testPostUserWithEmptyOrNullFieldsThrowsCannotBeEmptyOrNullException() throws Exception {
        // username > empty
        User user1 = User.builder()
                .id(1L)
                .username("")
                .password("password123")
                .email("user@gmail.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user1)))
                .andExpect(status().isBadRequest());

        // username > blank
        User user2 = User.builder()
                .id(2L)
                .username(" ")
                .password("password123")
                .email("user@gmail.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user2)))
                .andExpect(status().isBadRequest());


        // password > empty
        User user3 = User.builder()
                .id(3L)
                .username("user123")
                .password("")
                .email("user@gmail.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user3)))
                .andExpect(status().isBadRequest());

        // password > blank
        User user4 = User.builder()
                .id(4L)
                .username("user123")
                .password(" ")
                .email("user@gmail.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user4)))
                .andExpect(status().isBadRequest());

        // email > empty
        User user5 = User.builder()
                .id(3L)
                .username("user123")
                .password("password123")
                .email("")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user5)))
                .andExpect(status().isBadRequest());

        // email > blank
        User user6 = User.builder()
                .id(4L)
                .username("user123")
                .password("password123")
                .email(" ")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user6)))
                .andExpect(status().isBadRequest());
    }



    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk());
    }


    // If user updates successfully -> HttpStatus.OK
    @Test
    void testPutUserWhenFindByIdReturnsUserThrowsOk() throws Exception {
        User user = new User(1L, "user123", "password123", "user@gmail.com");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_1)))
                .andExpect(status().isBadRequest());
    }


    // If userId does not match user.getUserId-> HttpStatus.BAD_REQUEST -> "UserId and request body id do not match"
    @Test
    void testPutUserWhenUserIdDoesNotMatchReturnsUserThrowsNotMatchingException() throws Exception {
        User user = new User(1L, "user123", "password123", "user@gmail.com");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_1)))
                .andExpect(status().isBadRequest());
    }


    // If user does not exist -> HttpStatus.NOT_FOUND -> "UserId does not exist"
    @Test
    void testPutUserWhenFindByIdReturnsNullThrowsNotFound() throws Exception {
        User user = new User(1L, "user123", "password123", "user@gmail.com");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPutUserWithDifferentEmailThrowsUserCannotChangeException() throws Exception {
        User user = new User(1L, "user123", "password123", "user@gmail.com");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User updatedUser = new User(1L, "user123", "password123", "userUpdated@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedUser)))
                .andExpect(status().isConflict());
    }




    // --------------- \\
    //PATCH

    @Test
    void testPatchUserSuccess() throws Exception {
        User existingUser1 = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Mockito.when(userRepository.findById(existingUser1.getId())).thenReturn(Optional.of(existingUser1));

        User updatedUser1 = User.builder()
                .id(1L)
                .username("newUsername")
                .email("user@gmail.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedUser1)))
                .andExpect(status().isOk());

        User existingUser2 = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Mockito.when(userRepository.findById(existingUser2.getId())).thenReturn(Optional.of(existingUser2));

        User updatedUser2 = User.builder()
                .id(1L)
                .password("updatedPassword123")
                .email("user@gmail.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedUser2)))
                .andExpect(status().isOk());

    }

    @Test
    void testPatchUserUserIdNotMatching() throws Exception {
        User existingUser = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Mockito.when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        User updatedUser = User.builder()
                .id(2L) // Different ID
                .username("newUsername")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedUser)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testPatchUserUserNotFound() throws Exception {
        long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new User())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatchUserWithDifferentEmailThrowsUserCannotChangeException() throws Exception {
        User user = new User(1L, "user123", "password123", "user@gmail.com");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User updatedUser = User.builder()
                .id(1L)
                .email("updatedUser@gmail.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedUser)))
                .andExpect(status().isConflict());
    }

    // ---------------- \\

    @Test
    void testDeleteUserWhenFindByIdReturnsUserSuccess() {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Mockito.doNothing().when(userRepository).deleteById(user.getId());

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/user/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(user)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDeleteUserWhenFindByIdReturnsEmpty() {
        User existingUser = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("user@gmail.com")
                .build();

        Mockito.when(userRepository.findById(existingUser.getId())).thenReturn(Optional.empty());

        try {
            mockMvc.perform(MockMvcRequestBuilders.delete("/user/" + existingUser.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}