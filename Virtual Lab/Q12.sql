select b.bid, count(*)

from reserves r, boats b

where r.bid = b.bid and b.color = 'red'

group by b.bid

order by b.bid, count(*) asc