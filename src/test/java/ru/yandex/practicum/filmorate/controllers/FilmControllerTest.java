package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateFilm() throws Exception {
        // Создание корректного фильма
        Film validFilm = new Film();
        validFilm.setName("Valid Film");
        validFilm.setDescription("A valid film description.");
        validFilm.setReleaseDate(LocalDate.of(2023, 1, 1));
        validFilm.setDuration(120);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateFilm() throws Exception {
        // Создание фильма для обновления
        Film filmToUpdate = new Film();
        filmToUpdate.setId(1);
        filmToUpdate.setName("Film to Update");
        filmToUpdate.setDescription("A film to update.");
        filmToUpdate.setReleaseDate(LocalDate.of(2022, 1, 1));
        filmToUpdate.setDuration(90);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmToUpdate)))
                .andExpect(status().isOk());

        // Обновление фильма
        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("An updated film description.");
        updatedFilm.setReleaseDate(LocalDate.of(2023, 2, 2));
        updatedFilm.setDuration(120);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFilm)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetAllFilms() throws Exception {
        // Создание фильма
        Film film = new Film();
        film.setName("Film");
        film.setDescription("A film.");
        film.setReleaseDate(LocalDate.of(2021, 1, 1));
        film.setDuration(100);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());

        // Получение списка всех фильмов
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequest() throws Exception {
        // Попытка создать фильм с пустым названием
        Film filmWithEmptyName = new Film();
        filmWithEmptyName.setName("");
        filmWithEmptyName.setDescription("This description is way too long and should not pass validation.");
        filmWithEmptyName.setReleaseDate(LocalDate.of(2022, 1, 1));
        filmWithEmptyName.setDuration(90);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithEmptyName)))
                .andExpect(status().isBadRequest());

        // Попытка создать фильм с описанием длиннее 200 символов
        Film filmWithLongDescription = new Film();
        filmWithLongDescription.setName("Film");
        filmWithLongDescription.setDescription("This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation. This description is way too long and should not pass validation.");
        filmWithLongDescription.setReleaseDate(LocalDate.of(2022, 1, 1));
        filmWithLongDescription.setDuration(90);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithLongDescription)))
                .andExpect(status().isBadRequest());

        // Попытка создать фильм с некорректной датой релиза
        Film filmWithInvalidReleaseDate = new Film();
        filmWithInvalidReleaseDate.setName("Film");
        filmWithInvalidReleaseDate.setDescription("A film.");
        filmWithInvalidReleaseDate.setReleaseDate(LocalDate.of(1800, 1, 1));
        filmWithInvalidReleaseDate.setDuration(90);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithInvalidReleaseDate)))
                .andExpect(status().isBadRequest());

        // Попытка создать фильм с отрицательной продолжительностью
        Film filmWithNegativeDuration = new Film();
        filmWithNegativeDuration.setName("Film");
        filmWithNegativeDuration.setDescription("A film.");
        filmWithNegativeDuration.setReleaseDate(LocalDate.of(2022, 1, 1));
        filmWithNegativeDuration.setDuration(-5);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithNegativeDuration)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundForNonExistentFilmUpdate() throws Exception {
        // Попытка обновить несуществующий фильм
        Film validFilm = new Film();
        validFilm.setName("Valid Film");
        validFilm.setDescription("A valid film description.");
        validFilm.setReleaseDate(LocalDate.of(2023, 1, 1));
        validFilm.setDuration(120);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestForEmptyRequestBody() throws Exception {
        // Попытка отправить пустой запрос
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}