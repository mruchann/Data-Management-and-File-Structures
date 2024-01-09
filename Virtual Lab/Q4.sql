select distinct s.sname
from sailors s
where s.sid in

(select distinct s.sid
from sailors s, reserves r, boats b
where s.sid = r.sid and r.bid = b.bid
and b.color = 'red'

intersect

select distinct s.sid
from sailors s, reserves r, boats b
where s.sid = r.sid and r.bid = b.bid
and b.color = 'green')

order by s.sname asc