select s.sname, s.age
from sailors s

where s.age = (
        select max(s.age)
        from sailors s
    )

order by s.sname, s.age asc