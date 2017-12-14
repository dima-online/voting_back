-- for in-memory db because they don't have drop schema xxx cascade
-- http://www.h2database.com/html/grammar.html#drop_schema
--DROP SCHEMA IF EXISTS core;

--for other dbs
DROP SCHEMA IF EXISTS core CASCADE;



CREATE SCHEMA core;

DROP SCHEMA IF EXISTS external CASCADE;



CREATE SCHEMA external;

