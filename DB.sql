CREATE TABLE `Members` (
                           `member_id` VARCHAR(255) NOT NULL,
                           `email` VARCHAR(255) NOT NULL,
                           `encrypted_password` VARCHAR(255) NOT NULL,
                           PRIMARY KEY (`member_id`)
);

CREATE TABLE `Posts` (
                         `post_id` VARCHAR(255) NOT NULL,
                         `member_id` VARCHAR(255) NOT NULL,
                         `title` VARCHAR(100) NOT NULL,
                         PRIMARY KEY (`post_id`, `member_id`),
                         FOREIGN KEY (`member_id`) REFERENCES `Members` (`member_id`)
);
