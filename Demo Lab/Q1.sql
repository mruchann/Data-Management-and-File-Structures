select distinct p.name
from professor p, teaching t, course c
where p.professorid = t.professorid and
t.courseid = c.courseid and
c.deptid = 'CENG' and
p.deptid <> 'CENG'
order by p.name asc;