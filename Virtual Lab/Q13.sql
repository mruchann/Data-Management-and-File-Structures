select s.rating, min(s.age)

from sailors s

where s.age >= 18

group by s.rating
having (
    select count(*) 
    from sailors s2 
    where s2.rating = s.rating) > 1

order by s.rating, min(s.age) asc