package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Collection<String> showSortedStudents() {
        logger.info("Was invoked method for show sorted students");
        return studentRepository.findAll().stream()
                .filter(i -> i.getName().startsWith("a") || i.getName().startsWith("A"))
                .map(i -> i.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public double averageAgeOfStudentsFromStream() {
        logger.info("Was invoked method for show average age of students from stream");
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge).average().getAsDouble();

    }

    @Override
    public void getStudentsNamesFromThread() {
        logger.info("Was invoked method for show students names from thread");
        List<String> listOfStudentsNames = studentRepository.findAll().stream()
                .sorted(Comparator.comparing(Student::getId))
                .map(Student::getName)
                .collect(Collectors.toList());

        System.out.println(listOfStudentsNames.get(0));
        System.out.println(listOfStudentsNames.get(1));

        Thread thread1 = new Thread(() -> {
            logger.info("Was created thread one");
            System.out.println(listOfStudentsNames.get(2));
//            String s = "";
//            for (int i = 0; i < 100_000; i++) {
//                s += i;
//            }
            System.out.println(listOfStudentsNames.get(3));
        });
        Thread thread2 = new Thread(() -> {
            logger.info("Was created thread two");
            System.out.println(listOfStudentsNames.get(4));
            System.out.println(listOfStudentsNames.get(5));
        });
        thread1.start();
        thread2.start();
    }

    @Override
    public void getStudentsNamesFromSyncThread() {
        List<String> listOfStudentsNames = studentRepository.findAll().stream()
                .sorted(Comparator.comparing(Student::getId))
                .map(Student::getName)
                .collect(Collectors.toList());
        printStudentName(listOfStudentsNames, 0);
        printStudentName((listOfStudentsNames), 1);
        Thread thread1 = new Thread(() -> {
            printStudentName(listOfStudentsNames, 2);
            printStudentName(listOfStudentsNames, 3);
        });
        Thread thread2 = new Thread(() -> {
            printStudentName(listOfStudentsNames, 4);
            printStudentName(listOfStudentsNames, 5);
        });
        thread1.start();
        thread2.start();
    }

    private synchronized void printStudentName(List<String> listOfStudentsNames, int num) {
        System.out.println(listOfStudentsNames.get(num));
    }

}
