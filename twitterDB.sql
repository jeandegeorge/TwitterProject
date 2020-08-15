USE twitter;

DROP TABLE IF EXISTS  tweet;
CREATE TABLE tweet (idTweet bigint,text longtext,date date,iduser bigint);

DROP TABLE IF EXISTS  user;
CREATE TABLE user(iduser bigint, numFollowers int, numFriends int, createdAt date);


SET NAMES 'utf8mb4';


select count(*) from user;

select count(*) from tweet;



show variables like 'char%';
set global character_set_server = utf8mb4;

