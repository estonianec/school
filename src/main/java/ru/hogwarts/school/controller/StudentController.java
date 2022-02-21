package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

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

    @GetMapping("/filter-by-age") //решил создать два эндпоинта с разными url, так как условие явно не оговорено в ДЗ.
    // При этом в Faculty использовал перегрузку метода ради интереса. Можно было бы ещё сделать приём трёх параметров в
    // рамках одного метода и логикой внутри метода их разбирать, но это бы нарушило явное требование ДЗ "добавить эндпоинт".
    public ResponseEntity<Collection<Student>> filterByAge(@RequestParam(value = "age") int age) {
        Collection<Student> filteredStudents = studentService.filterByAge(age);
        if (filteredStudents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(filteredStudents);
    }
    @GetMapping("/find-by-age-between")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam(value = "min") int min,@RequestParam(value = "max") int max) {
        Collection<Student> foundStudents = studentService.findByAgeBetween(min, max);
        if (foundStudents.isEmpty()) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudents);
    }
}
