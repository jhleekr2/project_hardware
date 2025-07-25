ALTER TABLE board AUTO_INCREMENT = 1;

DELIMITER //

DROP PROCEDURE IF EXISTS insertDummyData;

CREATE PROCEDURE insertDummyData()
BEGIN
	DECLARE i INT DEFAULT 1;
    
    WHILE i <= 1000 DO
		INSERT INTO board (user_num, write_date, title, content) VALUES (1, NOW(), CONCAT('제목', i), CONCAT('내용', i));
        SET i = i + 1;
	END WHILE;
END //
DELIMITER ;

CALL insertDummyData();


