select distinct s.sname
from sailors s, reserves r
where r.bid = 103 and s.sid = r.sid
order by s.sname asc