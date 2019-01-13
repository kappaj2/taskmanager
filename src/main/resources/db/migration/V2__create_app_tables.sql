-- -----------------------------------------------------
-- Table `taskmanager_db`.`users`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `first_name` VARCHAR(255) NOT NULL,
  `last_name` VARCHAR(255) NOT NULL,
  `modified_date` DATETIME NULL,
  `created_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `taskmanager_db`.`task_status`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `task_status` (
  `status` VARCHAR(50) NOT NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`status`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `taskmanager_db`.`tasks`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `tasks` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `task` VARCHAR(255) NOT NULL,
  `description` VARCHAR(1024) NOT NULL,
  `date_time` DATETIME NOT NULL,
  `task_status` VARCHAR(50) NOT NULL,
  `users_id` INT NOT NULL,
  `created_date` DATETIME NOT NULL,
  `modified_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `task_UNIQUE` (`task` ASC),
  INDEX `fk_tasks_task_status1_idx` (`task_status` ASC),
  INDEX `fk_tasks_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_tasks_task_status1`
    FOREIGN KEY (`task_status`)
    REFERENCES `task_status` (`status`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tasks_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
