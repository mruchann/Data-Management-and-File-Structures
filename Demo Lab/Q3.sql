select p.name, p.age 
from professor p
where p.professorid in 

(select p.professorid
from professor p
where deptid = 'CENG'

union

select t.professorid
from teaching t
where t.courseid like 'CENG%')

order by p.name