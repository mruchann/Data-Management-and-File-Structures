select t.studentid, avg(t.score) as score

from transcript t

where (t.semester = 'F2013' or t.semester = 'S2014')

group by t.studentid

having avg(t.score) > 50

order by t.studentid asc