package ru.hogwarts.school.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = FacultyController.class)
class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final Faculty faculty = new Faculty();

    private final JSONObject facultyObject = new JSONObject();

    private Long id;
    private String name;
    private String color;

    //Концептуальный вопрос - почему мы мокаем именно репозиторий? Мы ведь тестируем именно контроллеры, а они ссылаются
    //на методы из сервисов, а получается, что мы при проверке контроллеров зачем-то полагаемся на работоспособность
    //сервисов, хотя логичнее было бы мокать именно сервисы.
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private FacultyServiceImpl facultyService;
    @InjectMocks
    private FacultyController facultyController;

    @BeforeEach
    void setUp() throws JSONException {
        id = 1L;
        name = "NameFacultyTest";
        color = "ColorFacultyTest";

        facultyObject.put("name", name);
        facultyObject.put("color", color);

        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
    }

    @Test
    void createFacultyTest() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void editFacultyTest() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void getFacultyTest() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void filterByColorTest() throws Exception {
        Faculty facultyForColorTest = new Faculty();
        facultyForColorTest.setId(2);
        facultyForColorTest.setName("NameFacultyTest");
        facultyForColorTest.setColor("ColorFacultyTest");
        Collection<Faculty> filteredFaculties = List.of(faculty, facultyForColorTest);
        when(facultyRepository.findByColorIgnoreCase(any(String.class))).thenReturn(filteredFaculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/?filterByColor=ColorFacultyTest")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").isArray());
    }

    @Test
    void findByColorIgnoreCaseOrNameIgnoreCaseTest() throws Exception {
        Faculty facultyForColorTest = new Faculty();
        facultyForColorTest.setId(2);
        facultyForColorTest.setName("NameFacultyTest");
        facultyForColorTest.setColor("ColorFacultyTest");
        Collection<Faculty> filteredFaculties = List.of(faculty, facultyForColorTest);
        when(facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(anyString(), anyString())).thenReturn(filteredFaculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/?colorOrName=green")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").isArray());
    }

    @Test
    void filterByColorAndFindByColorIgnoreCaseOrNameIgnoreCaseTest() throws Exception {
        Faculty facultyForColorTest = new Faculty();
        facultyForColorTest.setId(2);
        facultyForColorTest.setName("NameFacultyTest");
        facultyForColorTest.setColor("ColorFacultyTest");
        Collection<Faculty> filteredFaculties = List.of(faculty, facultyForColorTest);
        when(facultyRepository.findByColorIgnoreCase(any(String.class))).thenReturn(filteredFaculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/?filterByColor=ColorFacultyTest&colorOrName=ColorFacultyTest")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").isArray());
    }
}