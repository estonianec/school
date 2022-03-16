package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAgeBetween(int min, int max);

    Collection<Student> findByAge(int age);

    @Query(value = "select count(*) from student", nativeQuery = true)
    int countOfStudents();
    @Query(value = "select avg(age) from student", nativeQuery = true)
    float averageAgeOfStudents();
    @Query(value = "select * from student order by id desc LIMIT 5", nativeQuery = true)
    Collection<Student> showLastFiveStudents();
}
