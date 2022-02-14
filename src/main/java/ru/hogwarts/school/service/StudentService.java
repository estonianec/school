package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {

    Student createStudent(Student student);

    Student getStudent(long id);

    Student editStudent(Student student);

    Student deleteStudent(long id);

    Collection<Student> filterByAge(int age);
}
