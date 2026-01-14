CREATE TABLE `nestedcomment_recommend` (
	`ID`	int	NOT NULL,
	`comment_comment_no`	int	not NULL,
	`recommend_num`	int	not NULL
);

CREATE TABLE `uploadfile` (
	`board_no`	int,
	`filename_ori`	varchar(50),
	`filename_saved` varchar(50)
);

CREATE TABLE `board` (
	`board_no`	int	NOT NULL,
	`user_num`	int	NOT NULL,
	`write_date`	date	not NULL,
	`title`	varchar(100)	NULL,
	`content`	text
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
	`password`	varchar(50) not	NULL,
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

CREATE TABLE `uploadimg` (
    `board_no` int,
    `filename_ori` varchar(50) not null,
    `filename_saved` varchar(50) not null
);

ALTER TABLE `project_hardware`.`uploadimg`
    CHANGE COLUMN `filename_saved` `filename_saved` VARCHAR(50) NOT NULL ,
    ADD PRIMARY KEY (`filename_saved`);
;

ALTER TABLE `project_hardware`.`comment`
    CHANGE COLUMN `comment_no` `comment_no` INT NOT NULL AUTO_INCREMENT ,
    ADD PRIMARY KEY (`comment_no`);
;

ALTER TABLE `project_hardware`.`comment`
    CHANGE COLUMN `write_date` `write_date` DATETIME NOT NULL ;

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
