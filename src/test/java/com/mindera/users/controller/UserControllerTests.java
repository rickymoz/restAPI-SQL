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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
            .username("user")
            .password("pass")
            .build();

    @BeforeEach
    public void setup(){
        User USER_1 = User.builder().id(2L).username("Ricardo").password("ricardo123").build();
        User USER_2 = User.builder().id(3L).username("Marco").password("marco123").build();
        User USER_3 = User.builder().id(4L).username("Rodrigo").password("rodrigo123").build();

        users = new LinkedList<>(Arrays.asList(USER_1, USER_2, USER_3));
        Mockito.mock(UserRepository.class);

        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);
    }

    @Test
    public void testPostUser() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is("user")));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(users.size())))
                .andExpect(jsonPath("$[0].username", Matchers.is("Ricardo")))
                .andExpect(jsonPath("$[0].password", Matchers.is("ricardo123")));
    }
}