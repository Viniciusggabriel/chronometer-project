CREATE TABLE TELEMETRY_TIME (
	ID_TIME INT PRIMARY KEY AUTO_INCREMENT,
	milliseconds INT NOT NULL,
	seconds INT NOT NULL,
	minutes INT NOT NULL,
	hours INT NOT NULL
);

DELIMITER //
CREATE PROCEDURE P_TELEMETRY_TIME (
	IN p_milliseconds INT,
	IN p_seconds INT,
	IN p_minutes INT,
	IN p_hours INT
)
BEGIN
	INSERT INTO TELEMETRY_TIME (milliseconds,seconds, minutes,hours) VALUES (p_milliseconds,p_seconds,p_minutes,p_hours);
END //
DELIMITER ;

CALL P_TELEMETRY_TIME (100,10,10,10);

SELECT * FROM TELEMETRY_TIME;