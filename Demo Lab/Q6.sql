select s.name
from student s
where not exists (

    select c.courseid
    from course c
    where c.deptid = 'EE'
    
    except
    
    select t.courseid 
    from transcript t
    where t.studentid = s.studentid
)

order by s.name asc;