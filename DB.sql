-- --------------------------------------------------------
-- 서버 버전:                        8.0.22 - MySQL Community Server - GPL
-- 서버 OS:                        Linux
-- --------------------------------------------------------
CREATE TABLE `members` (
                           `member_id` VARCHAR(255) NOT NULL AUTO_INCREMENT,
                           `email` VARCHAR(255) NOT NULL,
                           `password` VARCHAR(255) NOT NULL,
                           PRIMARY KEY (`member_id`),
                           UNIQUE KEY (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `posts` (
                         `post_id` BIGINT NOT NULL AUTO_INCREMENT,
                         `member_id` BIGINT NOT NULL,
                         `title` VARCHAR(255) NOT NULL,
                         PRIMARY KEY (`post_id`),
                         KEY (`member_id`),
                         FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
