SELECT *
FROM student
WHERE age > 2
  AND age < 6;

SELECT name
FROM student;

SELECT *
FROM student
WHERE name like '%n%';

SELECT *
FROM student
WHERE age < id;

SELECT *
FROM student
ORDER BY age;

SELECT *
FROM student as s
         INNER JOIN faculty as f
                    ON f.id = s.faculty_id;

SELECT *
FROM student as s,
     faculty as f
WHERE f.id = s.faculty_id;


