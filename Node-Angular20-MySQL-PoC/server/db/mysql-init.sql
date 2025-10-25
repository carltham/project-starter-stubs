CREATE TABLE `testdb`.`books` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `published` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `testdb`.`users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `testdb`.`books` (title, description,published) VALUES('Becky', 'She\'s learning fast', '1') ;
INSERT INTO `testdb`.`books` (title, description,published) VALUES('Carls fight', 'It\'s going on', '1') ;
INSERT INTO `testdb`.`books` (title, description,published) VALUES('Finally up', 'Now all it takes is hard work', '0') ;
  
  
INSERT INTO `testdb`.`users` (full_name, email) VALUES('Alice','alice@example.com') ;
INSERT INTO `testdb`.`users` (full_name, email) VALUES('Bob','bob@example.com');
INSERT INTO `testdb`.`users` (full_name, email) VALUES('Charlie','charlie@example.com');

