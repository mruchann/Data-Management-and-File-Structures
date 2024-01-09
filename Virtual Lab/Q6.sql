select *
from sailors s

where s.rating > (select min(s.rating)
    from sailors s
    where s.sname = 'Horatio')

order by s.sid, s.sname, s.rating, s.age asc