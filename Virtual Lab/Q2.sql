select distinct s.sid 
from sailors s, reserves r, boats b
where s.sid = r.sid and b.bid = r.bid
and (b.color = 'red' or b.color = 'green')
order by s.sid asc