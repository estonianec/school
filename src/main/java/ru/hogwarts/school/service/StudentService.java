package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {

    Student createStudent(Student student);

    Student getStudent(long id);

    Student editStudent(Student student);

    void deleteStudent(long id);

    Collection<Student> filterByAge(int age);

    Collection<Student> findByAgeBetween(int min, int max);

    int countOfStudents();

    float averageAgeOfStudents();

    Collection<Student> showLastFiveStudents();

    Collection<String> showSortedStudents();

    double averageAgeOfStudentsFromStream();
}
