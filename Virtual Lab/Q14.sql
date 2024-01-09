select s.rating, avg(s.age)
from sailors s

group by s.rating
having avg(s.age) = (select min(avg_age) 
                        from (select avg(s.age) as avg_age
                                from sailors s
                                group by s.rating))

order by s.rating, avg(s.age) asc

