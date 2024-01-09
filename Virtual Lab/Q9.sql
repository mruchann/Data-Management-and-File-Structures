select s.sname
from sailors s
where not exists (
        select b.bid 
        from boats b
        
        except
        
        select r.bid
        from reserves r
        where s.sid = r.sid
    )

order by s.sname asc