package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student entity = studentService.editStudent(student);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(entity);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") long id) {
        Student entity = studentService.getStudent(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable("id") long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filterByAge")
    public ResponseEntity<Collection<Student>> filterByAge(@RequestParam(value = "age") int age) {
        Collection<Student> filteredStudents = studentService.filterByAge(age);
        if (filteredStudents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(filteredStudents);
    }
    @GetMapping("/findByAgeBetween")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam(value = "min") int min,
                                                                @RequestParam(value = "max") int max) {
        Collection<Student> foundStudents = studentService.findByAgeBetween(min, max);
        if (foundStudents.isEmpty()) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudents);
    }
    @GetMapping("/count-of-students")
    public int countOfStudents() {
        return studentService.countOfStudents();
    }
    @GetMapping("/average-age-of-students")
    public float averageAgeOfStudents() {
        return studentService.averageAgeOfStudents();
    }
    @GetMapping("/show-last-five-students")
    public ResponseEntity<Collection<Student>> showLastFiveStudents() {
        Collection<Student> lastFiveStudents = studentService.showLastFiveStudents();
        if (lastFiveStudents.isEmpty()) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lastFiveStudents);
    }

    @GetMapping("/show-sorted-students")
    public ResponseEntity<Collection<String>> showSortedStudents() {
        long time = System.currentTimeMillis();
        Collection<String> allStudents = studentService.showSortedStudents();
        time = System.currentTimeMillis() - time;
        logger.info("List of names completed in " + time + " ms");
        if (allStudents.isEmpty()) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allStudents);
    }
    @GetMapping("average-age-of-students-from-stream")
    public double showAverageAgeOfStudentsFromStream() {
        return studentService.averageAgeOfStudentsFromStream();
    }

    @GetMapping("get-some-num")
    public int getSomeNum() {
        long timeBefore = System.currentTimeMillis();
        int sum = Stream.iterate(1, a -> a +1) .limit(100_000_000) .reduce(0, (a, b) -> a + b );
        timeBefore = System.currentTimeMillis() - timeBefore;
        logger.info("Sum calculated in before " + timeBefore + " ms");
        long timeAfter = System.currentTimeMillis();
        List<Integer> listOfInt = Stream.iterate(1, a -> a + 1)
                .limit(100_000_000)
                .collect(Collectors.toList());
        sum = listOfInt.stream()
                .parallel()
                .mapToInt(a->a)
//                .reduce(0, (a, b) -> a + b );
                .sum();
        timeAfter = System.currentTimeMillis() - timeAfter;
        logger.info("Sum calculated in after " + timeAfter + " ms");
        return sum;
    }
}
