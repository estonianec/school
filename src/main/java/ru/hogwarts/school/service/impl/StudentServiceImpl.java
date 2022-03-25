package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@Service
public class StudentServiceImpl implements StudentService {

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    @Override
    public Student getStudent(long id) {
        logger.info("Was invoked method for find student with id = " + id);
        return studentRepository.findById(id).get();
    }

    @Override
    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student with id = " + student.getId());
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student with id = " + id);
        studentRepository.deleteById(id);
    }

    @Override
    public Collection<Student> filterByAge(int age) {
        logger.info("Was invoked method for find students with age = " + age);
        return studentRepository.findByAge(age);
    }

    @Override
    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find students by age between {} and {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public int countOfStudents() {
        logger.info("Was invoked method for show count of students");
        return studentRepository.countOfStudents();
    }
    @Override
    public float averageAgeOfStudents() {
        logger.info("Was invoked method for show average age of students");
        return studentRepository.averageAgeOfStudents();
    }
    @Override
    public Collection<Student> showLastFiveStudents() {
        logger.info("Was invoked method for show last five students");
        return studentRepository.showLastFiveStudents();
    }
}
