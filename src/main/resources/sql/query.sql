select * from users;
UPDATE users set role='ADMIN' where user_num=1;
//MySql의 보호 기능 떄문에 UPDATE 할때 PK가 아닌 키를 where 하면 에러가 발생함
SHOW DATABASES;
UPDATE board set hit = hit + 1 where board_no = 2;
select * from board;
select id, nick from users where user_num=1;
SELECT a.board_no as boardNo, a.user_num as userNum, a.write_date as writeDate, a.title, a.content, a.hit, b.id, b.nick FROM board as A LEFT OUTER JOIN users as B on a.user_num = b.user_num WHERE board_no = 2;

