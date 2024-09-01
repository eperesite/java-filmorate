package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User user;

    @Test
    void shouldCreateUser() throws Exception {
        when(userService.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        when(userService.update(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        when(userService.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].name").value(user.getName()));
    }

    @Test
    void shouldAddFriend() throws Exception {
        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());
        verify(userService, times(1)).addFriend(1L, 2L);
    }

    @Test
    void shouldDeleteFriend() throws Exception {
        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteFriend(1L, 2L);
    }

    @Test
    void shouldReturnFriends() throws Exception {
        when(userService.getFriends(1L)).thenReturn(List.of(user));

        mockMvc.perform(get("/users/1/friends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].name").value(user.getName()));
    }

    @Test
    void shouldReturnCommonFriends() throws Exception {
        when(userService.getCommonFriends(1L, 2L)).thenReturn(List.of(user));

        mockMvc.perform(get("/users/1/friends/common/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].name").value(user.getName()));
    }
}
