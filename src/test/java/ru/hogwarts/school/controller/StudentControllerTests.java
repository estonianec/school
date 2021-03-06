package ru.hogwarts.school.controller;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTests {

    @LocalServerPort
    private int port;

    private String resourceUrl;
    // Понимаю, что можно было бы работать со статичными id, но тогда не получилось бы реализовать замысел при котором
    // тесты проводятся в живой БД при этом на неё не влияют (за исключением увеличения счётчика ID после каждого теста).
    // Понимаю, что можно было работать в тестовой БД, но не сумел нагуглить, как использовать две БД в одном проекте.
    // После общение с Кириллом Качаловым в личке пришло понимание, что "задача проверить через тест рест темплейт,
    // что при запросе что-то возвращается", а не функциональное тестирование и что можно было обойтись условным .isNotNull()
    // на каждом этапе проверки. Скажете убрать лишнее - уберу, хоть мне это лишнее и нравится :)
    private long id;
    private Student student;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        resourceUrl = "http://localhost:" + port + "/student";

        student = new Student();
        student.setName("TestName");
        student.setAge(99);
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void createStudentTest() {
        Student studentInDb = restTemplate.postForObject(resourceUrl, student, Student.class);
        id = studentInDb.getId();
        student.setId(id);

        Assertions
                .assertThat(studentInDb)
                .isNotNull()
                .isEqualTo(student);
        studentController.deleteStudent(id);
    }

    @Test
    public void editStudentTest() {

        id = restTemplate.postForObject(resourceUrl, student, Student.class).getId();

        Student studentEdited = new Student();
        studentEdited.setId(id);
        studentEdited.setName("TestName2");
        studentEdited.setAge(111);

        restTemplate.put(resourceUrl, studentEdited);

        Assertions
                .assertThat(this.restTemplate.getForObject(resourceUrl + "/" + id, Student.class))
                .isNotNull()
                .isEqualTo(studentEdited);
        studentController.deleteStudent(id);

    }
    @Test
    public void getStudentTest() {
        id = restTemplate.postForObject(resourceUrl, student, Student.class).getId();
        student.setId(id);

        Assertions
                .assertThat(this.restTemplate.getForObject(resourceUrl + "/" + id, Student.class))
                .isNotNull()
                .isEqualTo(student);
        studentController.deleteStudent(id);
    }

    @Test
    public void deleteStudentTest() {
        id = restTemplate.postForObject(resourceUrl, student, Student.class).getId();
        restTemplate.delete(resourceUrl + "/" + id);

        Assertions
                //рабочий но странный вариант
                .assertThat(this.restTemplate.getForObject(resourceUrl + "/" + id, Student.class).getName())
                //не рабочий но логичный вариант. Почему тест для Actual создает пустой объект класса Student, а не просто null?
//                .assertThat(this.restTemplate.getForObject(resourceUrl + "/" + id, Student.class))
                .isNull();

    }

    @Test
    public void filterByAgeTest() {
        student.setAge(-1);
        id = restTemplate.postForObject(resourceUrl, student, Student.class).getId();
        student.setId(id);
        Assertions
                .assertThat(this.restTemplate.getForObject(resourceUrl + "/filterByAge?age=-1", Collection.class).size())
                .isEqualTo(1);
        studentController.deleteStudent(id);
    }

    @Test
    public void filterByAgeBetweenTest() {
        Student student2 = new Student();
        student2.setAge(98);
        student2.setName("Test2");
        Student student3 = new Student();
        student3.setAge(97);
        student3.setName("Test3");

        Student studentInDb = restTemplate.postForObject(resourceUrl, student, Student.class);
        student.setId(studentInDb.getId());
        Student studentInDb2 = restTemplate.postForObject(resourceUrl, student2, Student.class);
        student2.setId(studentInDb2.getId());
        Student studentInDb3 = restTemplate.postForObject(resourceUrl, student3, Student.class);
        student3.setId(studentInDb3.getId());

        Assertions
                .assertThat(this.restTemplate.getForObject(resourceUrl + "/findByAgeBetween?min=98&max=100", Collection.class).size())
                .isEqualTo(2);

        studentController.deleteStudent(student.getId());
        studentController.deleteStudent(student2.getId());
        studentController.deleteStudent(student3.getId());
    }

}
