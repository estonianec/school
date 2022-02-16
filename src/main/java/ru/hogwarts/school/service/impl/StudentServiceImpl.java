package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private long counter = 0;
    HashMap<Long, Student> students = new HashMap<>();

    @Override
    public Student createStudent(Student student) {
        return students.put(counter++, student);
    }

    @Override
    public Student getStudent(long id) {
        if (students.containsKey(id)) {
            return students.get(id);
        }
        return null;
    }

    @Override
    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            return students.put(student.getId(), student);
        }
        return null;
    }

    @Override
    public Student deleteStudent(long id) {
        if (students.containsKey(id)) {
            return students.remove(id);
        }
        return null;
    }

    @Override
    public Collection<Student> filterByAge(int age) {
        return students.values().stream()
                .filter(e -> e.getAge() == age)
                .collect(Collectors.toList());
    }
}
