package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateUser() throws Exception {
        // Создание пользователя
        User user = new User();
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setLogin("johndoe");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        // Создание пользователя для обновления
        User userToUpdate = new User();
        userToUpdate.setId(1);
        userToUpdate.setName("John Doe");
        userToUpdate.setEmail("johndoe@example.com");
        userToUpdate.setLogin("johndoe");
        userToUpdate.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk());

        // Обновление пользователя
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updateduser@example.com");
        updatedUser.setLogin("updateduser");
        updatedUser.setBirthday(LocalDate.of(1995, 2, 2));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        // Создание пользователя
        User user = new User();
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setLogin("johndoe");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        // Получение списка всех пользователей
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequest() throws Exception {
        // Попытка создать пользователя с датой рождения в будущем
        User userWithFutureBirthday = new User();
        userWithFutureBirthday.setName("John Doe");
        userWithFutureBirthday.setEmail("johndoe@example.com");
        userWithFutureBirthday.setLogin("johndoe");
        userWithFutureBirthday.setBirthday(LocalDate.of(2024, 12, 12));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithFutureBirthday)))
                .andExpect(status().isBadRequest());

        // Попытка создать пользователя с пустым email
        User userWithEmptyEmail = new User();
        userWithEmptyEmail.setName("John Doe");
        userWithEmptyEmail.setEmail("");
        userWithEmptyEmail.setLogin("johndoe");
        userWithEmptyEmail.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithEmptyEmail)))
                .andExpect(status().isBadRequest());

        // Попытка создать пользователя с некорректным email
        User userWithInvalidEmail = new User();
        userWithInvalidEmail.setName("John Doe");
        userWithInvalidEmail.setEmail("johndoe.example.com");
        userWithInvalidEmail.setLogin("johndoe");
        userWithInvalidEmail.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithInvalidEmail)))
                .andExpect(status().isBadRequest());

        // Попытка создать пользователя с пустым логином
        User userWithEmptyLogin = new User();
        userWithEmptyLogin.setName("John Doe");
        userWithEmptyLogin.setEmail("johndoe@example.com");
        userWithEmptyLogin.setLogin("");
        userWithEmptyLogin.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithEmptyLogin)))
                .andExpect(status().isBadRequest());

        // Попытка создать пользователя с логином, содержащим пробелы
        User userWithWhitespaceLogin = new User();
        userWithWhitespaceLogin.setName("John Doe");
        userWithWhitespaceLogin.setEmail("johndoe@example.com");
        userWithWhitespaceLogin.setLogin("john doe");
        userWithWhitespaceLogin.setBirthday(LocalDate.of(1990, 1, 1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithWhitespaceLogin)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundForNonExistentUserUpdate() throws Exception {
        // Попытка обновить несуществующего пользователя
        User updatedUser = new User();
        updatedUser.setId(999);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updateduser@example.com");
        updatedUser.setLogin("updateduser");
        updatedUser.setBirthday(LocalDate.of(1995, 2, 2));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }
}
