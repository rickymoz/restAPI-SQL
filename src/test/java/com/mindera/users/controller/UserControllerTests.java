package com.mindera.users.controller;

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
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private List<User> users;

    User newUser = User.builder()
            .username("teste")
            .password("teste123")
            .email("teste@gmail.com")
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
        users = new LinkedList<>(Arrays.asList(USER_1, USER_2, USER_3));
        Mockito.mock(UserRepository.class);
    }

    @Test
    public void testPostUser() throws Exception {
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


    // If id, name or email null or empty -> HttpStatus.BAD_REQUEST -> "User Id user name and user email cannot be null or empty"
    @Test
    public void testPostUserWithEmptyUsernameThrowsUserCannotBeNullException() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("")
                .password("password123")
                .email("user@gmail.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }


    // If id, name or email null or empty -> HttpStatus.BAD_REQUEST -> "User Id user name and user email cannot be null or empty"
    @Test
    public void testPostUserWithEmptyPasswordThrowsUserCannotBeNullException() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("")
                .email("user@gmail.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }


    // If id, name or email null or empty -> HttpStatus.BAD_REQUEST -> "User Id user name and user email cannot be null or empty"
    @Test
    public void testPostUserWithEmptyEmailThrowsUserCannotBeNullException() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("user123")
                .password("password123")
                .email("")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk());
    }


    // If user updates successfully -> HttpStatus.OK
    @Test
    public void testPutUserWhenFindByIdReturnsUserThrowsOk() throws Exception {
        User user = new User(1L, "user123", "password123", "user@gmail.com");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_1)))
                .andExpect(status().isBadRequest());
    }


    // If userId does not match user.getUserId-> HttpStatus.BAD_REQUEST -> "UserId and request body id do not match"
    @Test
    public void testPutUserWhenUserIdDoesNotMatchReturnsUserThrowsNotMatchingException() throws Exception {
        User user = new User(1L, "user123", "password123", "user@gmail.com");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(USER_1)))
                .andExpect(status().isBadRequest());
    }


    // If user does not exist -> HttpStatus.NOT_FOUND -> "UserId does not exist"
    @Test
    public void testPutUserWhenFindByIdReturnsNullThrowsNotFound() throws Exception {
        User user = new User(1L, "user123", "password123", "user@gmail.com");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andExpect(status().isNotFound());
    }


    // If user already exists with different email (cannot change email) -> HttpStatus.CONFLICT "User email cannot be updated"
    @Test
    public void testDeleteUserWhenFindByIdReturnsUserSuccess() {
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

}