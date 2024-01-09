select s.name 
from student s
where s.studentid in (select s.studentid

from student s, transcript t

where s.studentid = t.studentid 
and
t.courseid = 'CENG230'

intersect

select s.studentid
from student s, transcript t
where s.studentid = t.studentid 
and 
t.courseid = 'EE213')
order by s.name asc;