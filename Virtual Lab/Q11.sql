select s.rating, min(s.age)
from sailors s
where s.age >= 18

group by s.rating
having count(*) > 1

order by s.rating, s.age asc