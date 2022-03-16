select f.name, s.name, s.age
from student as s
         INNER JOIN faculty as f on s.faculty_id = f.id
order by f.name;
select s.*
from student as s
         RIGHT JOIN avatar as a on a.student_id = s.id;
