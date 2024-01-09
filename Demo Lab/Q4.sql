select s.name, s.gpa 
from student s
where s.gpa > (select max(s.gpa)
from student s
where s.status = 'Senior')
order by s.name asc