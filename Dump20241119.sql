DROP DATABASE IF EXISTS trip;
CREATE DATABASE trip;
USE trip;

-- Table structure for table `users`

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `nickname` varchar(100) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(254) NOT NULL,
  `grade` tinyint NOT NULL DEFAULT '3',
  `role` varchar(100) NOT NULL DEFAULT 'NORMAL',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifyDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `is_verified` tinyint(1) NOT NULL DEFAULT '0',
  `profile_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `nickname` (`nickname`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `password_reset_tokens`

DROP TABLE IF EXISTS `password_reset_tokens`;
CREATE TABLE `password_reset_tokens` (
  `token` varchar(255) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `expiry_date` datetime NOT NULL,
  PRIMARY KEY (`token`),
  KEY `user_email` (`user_email`),
  CONSTRAINT `password_reset_tokens_ibfk_1` FOREIGN KEY (`user_email`) REFERENCES `users` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `plans`

DROP TABLE IF EXISTS `plans`;
CREATE TABLE `plans` (
  `plan_id` bigint NOT NULL AUTO_INCREMENT COMMENT '플랜 ID (PK)',
  `user_id` bigint NOT NULL COMMENT '작성 회원 ID (FK)',
  `plan_title` varchar(255) NOT NULL COMMENT '타이틀',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `start_date` date NOT NULL COMMENT '시작일',
  `end_date` date NOT NULL COMMENT '종료일',
  `total_date` int NOT NULL COMMENT '여행지속일',
  `description` varchar(255) NOT NULL COMMENT '여행 설명',
  PRIMARY KEY (`plan_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `plans_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='플랜 테이블';

-- Table structure for table `share_plan`

DROP TABLE IF EXISTS `share_plan`;
CREATE TABLE `share_plan` (
  `plan_id` bigint NOT NULL COMMENT '플랜 ID',
  `user_id` bigint NOT NULL COMMENT '작성 회원 ID',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `total_date` int NOT NULL COMMENT '여행지속일',
  PRIMARY KEY (`plan_id`),
  KEY `fk_share_plan_user` (`user_id`),
  CONSTRAINT `fk_share_plan_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='공유 플랜 테이블';

-- Table structure for table `share_board`

DROP TABLE IF EXISTS `share_board`;
CREATE TABLE `share_board` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '게시글 ID',
  `user_id` bigint NOT NULL COMMENT '작성 회원 ID',
  `plan_id` bigint NOT NULL COMMENT '플랜 ID',
  `title` varchar(255) NOT NULL COMMENT '게시글 이름',
  `content` text NOT NULL COMMENT '내용',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
  `image_name` varchar(255) DEFAULT NULL COMMENT '이미지 이름',
  PRIMARY KEY (`post_id`),
  KEY `fk_share_board_user` (`user_id`),
  KEY `fk_share_board_plan` (`plan_id`),
  CONSTRAINT `fk_share_board_plan` FOREIGN KEY (`plan_id`) REFERENCES `share_plan` (`plan_id`),
  CONSTRAINT `fk_share_board_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='공유 게시글 테이블';

-- Table structure for table `verification_tokens`

DROP TABLE IF EXISTS `verification_tokens`;
CREATE TABLE `verification_tokens` (
  `token` varchar(100) NOT NULL,
  `user_email` varchar(254) NOT NULL,
  `expiry_date` datetime NOT NULL,
  PRIMARY KEY (`token`),
  KEY `user_email` (`user_email`),
  CONSTRAINT `verification_tokens_ibfk_1` FOREIGN KEY (`user_email`) REFERENCES `users` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `visit_places`

DROP TABLE IF EXISTS `visit_places`;
CREATE TABLE `visit_places` (
  `plan_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `visit_google_id` varchar(255) NOT NULL,
  `display_name` varchar(255) NOT NULL,
  `latitude` decimal(20,17) NOT NULL,
  `longitude` decimal(20,17) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `visit_date` date NOT NULL,
  `visit_order` int NOT NULL,
  KEY `plan_id` (`plan_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `visit_places_ibfk_1` FOREIGN KEY (`plan_id`) REFERENCES `plans` (`plan_id`),
  CONSTRAINT `visit_places_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `friends`

DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `from_user_email` VARCHAR(255) NOT NULL,
  `to_user_email` VARCHAR(255) NOT NULL,
  `status` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`from_user_email`, `to_user_email`),
  FOREIGN KEY (`from_user_email`) REFERENCES `users` (`email`) ON DELETE CASCADE,
  FOREIGN KEY (`to_user_email`) REFERENCES `users` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 나머지 테이블과 데이터는 동일하게 유지됩니다.
