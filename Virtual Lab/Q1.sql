select distinct sailors.sid 
from sailors, reserves
where sailors.sid = reserves.sid