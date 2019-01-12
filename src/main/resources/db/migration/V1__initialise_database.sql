
CREATE TABLE `taskmanager_db`.`application` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50),
  `created_at` TIMESTAMP NOT NULL DEFAULT NOW() ,
  `modified_at` TIMESTAMP NOT NULL DEFAULT NOW(),
  `version` VARCHAR(25),
  PRIMARY KEY (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO application(`name`, `created_at`, `modified_at`, `version`) VALUES('taskmanager', now(), now(), 'v1.00');