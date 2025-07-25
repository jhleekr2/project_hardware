CREATE TABLE `nestedcomment_recommend` (
	`ID`	int	NOT NULL,
	`comment_comment_no`	int	not NULL,
	`recommend_num`	int	not NULL
);

CREATE TABLE `uploadfile` (
	`board_no`	int	NOT NULL,
	`filename_ori`	varchar(50) not null,
	`filename_saved` varchar(50) not null
);

CREATE TABLE `board` (
	`board_no`	int	NOT NULL,
	`user_num`	int	NOT NULL,
	`write_date`	date	not NULL,
	`title`	varchar(100)	NULL,
	`content`	text,
    `hit` int
);

CREATE TABLE `comment` (
	`comment_no`	int	NOT NULL,
	`board_no`	int	NOT NULL,
	`user_num`	int not	NULL,
	`write_date` date	not NULL,
	`content`	text
);

CREATE TABLE `nested_comment` (
	`nested_comment_no` int not null,
	`comment_no`	int	NOT NULL,
	`user_num`	int not	NULL,
	`write_date`	date not NULL,
	`content`	text
);

CREATE TABLE `category` (
	`board_no`	int	NOT NULL,
	`category`	varchar(2) not null
);

CREATE TABLE `users` (
	`user_num`	int	NOT NULL,
	`ID`	varchar(20) not NULL,
	`nick`	varchar(20) not NULL,
	`password`	varchar(255) not	NULL,
	`email`	 varchar(50) not NULL,
	`ban`	varchar(2),
	`ban_date`	date
);

CREATE TABLE `board_recommend` (
	`ID`	int	NOT NULL,
	`board_no`	int	NOT NULL,
	`recommend_num`	int	NULL
);

CREATE TABLE `comment_recommend` (
	`ID`	int	NOT NULL,
	`comment_no`	int	NOT NULL,
	`recommend_num`	int not NULL
);

ALTER TABLE users 
CHANGE COLUMN `user_num` `user_num` INT NOT NULL AUTO_INCREMENT ,
ADD PRIMARY KEY (`user_num`);
;
ALTER TABLE users
ADD COLUMN `role` VARCHAR(20);
ALTER TABLE usersuploadfileboard_recommend
MODIFY COLUMN `role` VARCHAR(20) DEFAULT 'MEMBER';
ALTER TABLE users
MODIFY COLUMN `password` VARCHAR(255);
// password는 일반적으로 VARCHAR(255)가 관례처럼 사용된다
// 이유는 BCrypt로 암호화하면 58-60바이트의 암호화된 문자열로 저장되어 50바이트로는 부족하고
// 향후 더 강한 암호화 알고리즘으로 바꾸면 용량이 더 커지기 떄문이다
;
ALTER TABLE board
MODIFY COLUMN board_no INT NOT NULL AUTO_INCREMENT PRIMARY KEY;
ALTER TABLE board
ADD COLUMN `hit` int;
ALTER TABLE board
MODIFY COLUMN write_date DATETIME;

INSERT into board(user_num, write_date, title, content, hit) VALUES (1, NOW(), "환영합니다", "첫번째 게시물입니다", 0);
select * from board;
INSERT into board(user_num, write_date, title, content, hit) values (1, NOW(), "테스트 중입니다", "두번째", 0);
delete from board where board_no = 1;

// 테이블 연관관계 걸기
ALTER TABLE `nestedcomment_recommend` ADD CONSTRAINT `PK_NESTEDCOMMENT_RECOMMEND` PRIMARY KEY (
	`ID`
);

ALTER TABLE `board` ADD CONSTRAINT `PK_BOARD` PRIMARY KEY (
	`board_no`
);

ALTER TABLE `comment` ADD CONSTRAINT `PK_COMMENT` PRIMARY KEY (
	`comment_no`
);

ALTER TABLE `nested_comment` ADD CONSTRAINT `PK_NESTED_COMMENT` PRIMARY KEY (
	`nested_comment_no`
);

ALTER TABLE `users` ADD CONSTRAINT `PK_USERS` PRIMARY KEY (
	`user_num`
);

ALTER TABLE `board_recommend` ADD CONSTRAINT `PK_BOARD_RECOMMEND` PRIMARY KEY (
	`ID`
);

ALTER TABLE `comment_recommend` ADD CONSTRAINT `PK_COMMENT_RECOMMEND` PRIMARY KEY (
	`ID`
);

ALTER TABLE `nestedcomment_recommend` ADD CONSTRAINT `FK_nested_comment_TO_nestedcomment_recommend_1` FOREIGN KEY (
	`comment_comment_no`
)
REFERENCES `nested_comment` (
	`nested_comment_no`
);

ALTER TABLE `uploadfile` ADD CONSTRAINT `FK_board_TO_uploadfile_1` FOREIGN KEY (
	`board_no`
)
REFERENCES `board` (
	`board_no`
);

ALTER TABLE `board` ADD CONSTRAINT `FK_users_TO_board_1` FOREIGN KEY (
	`user_num`
)
REFERENCES `users` (
	`user_num`
);

ALTER TABLE `comment` ADD CONSTRAINT `FK_board_TO_comment_1` FOREIGN KEY (
	`board_no`
)
REFERENCES `board` (
	`board_no`
);

ALTER TABLE `nested_comment` ADD CONSTRAINT `FK_comment_TO_nested_comment_1` FOREIGN KEY (
	`comment_no`
)
REFERENCES `comment` (
	`comment_no`
);

ALTER TABLE `category` ADD CONSTRAINT `FK_board_TO_category_1` FOREIGN KEY (
	`board_no`
)
REFERENCES `board` (
	`board_no`
);

ALTER TABLE `board_recommend` ADD CONSTRAINT `FK_board_TO_board_recommend_1` FOREIGN KEY (
	`board_no`
)
REFERENCES `board` (
	`board_no`
);

ALTER TABLE `comment_recommend` ADD CONSTRAINT `FK_comment_TO_comment_recommend_1` FOREIGN KEY (
	`comment_no`
)
REFERENCES `comment` (
	`comment_no`
);