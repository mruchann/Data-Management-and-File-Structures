select *
from sailors s

where s.rating = (
    select max(s.rating)
    from sailors s
    )

order by s.sid, s.sname, s.rating, s.age asc