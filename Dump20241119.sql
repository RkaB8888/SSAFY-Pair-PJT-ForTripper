DROP DATABASE IF EXISTS trip;
CREATE DATABASE trip;
USE trip;

-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: trip
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `password_reset_tokens`
--

DROP TABLE IF EXISTS `password_reset_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_tokens` (
  `token` varchar(255) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `expiry_date` datetime NOT NULL,
  PRIMARY KEY (`token`),
  KEY `user_email` (`user_email`),
  CONSTRAINT `password_reset_tokens_ibfk_1` FOREIGN KEY (`user_email`) REFERENCES `users` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_tokens`
--

LOCK TABLES `password_reset_tokens` WRITE;
/*!40000 ALTER TABLE `password_reset_tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plans`
--

DROP TABLE IF EXISTS `plans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plans`
--
--
-- Table structure for table `share_board`
--

DROP TABLE IF EXISTS `share_board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
  CONSTRAINT `fk_share_board_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='공유 게시글 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share_board`
--
--
-- Table structure for table `share_place`
--

DROP TABLE IF EXISTS `share_place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `share_place` (
  `plan_id` bigint NOT NULL COMMENT '플랜 ID',
  `visit_google_id` varchar(255) NOT NULL COMMENT '방문 장소 구글ID',
  `display_name` varchar(255) NOT NULL COMMENT '방문 장소명',
  `latitude` decimal(20,17) NOT NULL COMMENT '방문 장소 위도',
  `longitude` decimal(20,17) NOT NULL COMMENT '방문 장소 경도',
  `address` varchar(255) NOT NULL COMMENT '방문 장소 주소',
  `phone_number` varchar(255) DEFAULT NULL COMMENT '방문 장소 전화번호',
  `visit_date` date NOT NULL COMMENT '방문 날짜',
  `visit_order` int NOT NULL COMMENT '순서',
  KEY `fk_share_place_plan` (`plan_id`),
  CONSTRAINT `fk_share_place_plan` FOREIGN KEY (`plan_id`) REFERENCES `share_plan` (`plan_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='공유 방문 장소 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share_place`
--
--
-- Table structure for table `share_plan`
--

DROP TABLE IF EXISTS `share_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `share_plan` (
  `plan_id` bigint NOT NULL COMMENT '플랜 ID',
  `user_id` bigint NOT NULL COMMENT '작성 회원 ID',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `total_date` int NOT NULL COMMENT '여행지속일',
  PRIMARY KEY (`plan_id`),
  KEY `fk_share_plan_user` (`user_id`),
  CONSTRAINT `fk_share_plan_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='공유 플랜 테이블';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share_plan`
--
--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'$2a$10$cFFs1kgIPYkB14IbpuEyqefdqhkHvNPa9qpHqKalUDI8bqWmZfeVy','홍길동','경로당소드마스터','01012345678','0',3,'NORMAL','2024-11-15 08:39:03','2024-11-15 08:39:03',1,0,NULL),
(2,'$2a$10$z..j4zAHM.X5ozOK4i02vusgHYgjbBZup3.Jk4YhRXrv1iYxucEtG','이은선','모르는개산책','01012345678','98silverline@gmail.com',3,'NORMAL','2024-11-15 08:41:24','2024-11-25 08:45:50',1,1,NULL),
(3,'$2a$10$qi1cuxavbLPzo7XthD/NgON/Pt55wVjCPN5VmcPySvkwuDqz3a6ti','김철수','손흥민의야구교실','01012345678','abc@gmail.com',3,'NORMAL','2024-11-15 08:43:21','2024-11-15 08:43:21',1,0,NULL),
(5,'$2a$10$e7594FBPaySt4RvzZQ/u3OBK6rQsaEznddq8gUYEXObsNQNs6BTlO','김유저','집가고싶다','01098746212','user123@gmail.com',3,'NORMAL','2024-11-18 02:53:46','2024-11-18 02:53:46',1,0,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_tokens`
--

DROP TABLE IF EXISTS `verification_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verification_tokens` (
  `token` varchar(100) NOT NULL,
  `user_email` varchar(254) NOT NULL,
  `expiry_date` datetime NOT NULL,
  PRIMARY KEY (`token`),
  KEY `user_email` (`user_email`),
  CONSTRAINT `verification_tokens_ibfk_1` FOREIGN KEY (`user_email`) REFERENCES `users` (`email`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_tokens`
--

LOCK TABLES `verification_tokens` WRITE;
/*!40000 ALTER TABLE `verification_tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `verification_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visit_places`
--

DROP TABLE IF EXISTS `visit_places`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
  CONSTRAINT `visit_places_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visit_places`
--

LOCK TABLES `visit_places` WRITE;
/*!40000 ALTER TABLE `visit_places` DISABLE KEYS */;
INSERT INTO `visit_places` VALUES (10,2,'ChIJ-5DfghYTDTUR8RaKuI4FID4','섭지코지',33.42420949999999600,126.93111100000002000,'대한민국 제주특별자치도 서귀포시 성산읍 섭지코지로','+82 64-782-0080','2024-12-19',3),(7,2,'ChIJ34sCtrbaaDURM0FNJpWpXH4','고향집',35.11231910000000000,128.83657270000000000,'대한민국 경상남도 창원시 진해구 가주동 336-1','+82 55-552-3105','2024-11-21',3),(7,2,'ChIJ3wqwDT_baDURfpnW_SMWvWE','물레방아식당',35.08514119999999500,128.88175450000000000,'대한민국 부산광역시','+82 51-831-6677','2024-11-21',2),(10,2,'ChIJ83lq-MBgDDURc64bn4fHZe8','한림공원',33.38952790000000000,126.23928839999998000,'대한민국 제주특별자치도 제주시 한림읍 한림로 300','+82 64-796-0001','2024-12-20',1),(10,2,'ChIJ8afI-XNTDDURC7OZzyBO8KA','정방폭포',33.24485209999999600,126.57180319999999000,'대한민국 제주특별자치도 서귀포시 칠십리로214번길 37','+82 64-733-1530','2024-12-19',1),(7,2,'ChIJ9eNRAefaaDURqQFgH52eEj8','알콩달콩순두부',35.09047030000000000,128.85481560000000000,'대한민국 부산광역시 강서구 송정동 1631-6','+82 51-831-6664','2024-11-21',4),(7,2,'ChIJ9eNRAefaaDURxHSr5Aqm4gw','녹산불백',35.09047030000000000,128.85481560000000000,'대한민국 부산광역시',NULL,'2024-11-21',6),(10,2,'ChIJDYjcLFurDTUR-fF9Te-Wt7w','쇠소깍',33.25246939999999500,126.62353330000000000,'대한민국 제주특별자치도 서귀포시 쇠소깍로 138','+82 64-732-1562','2024-12-20',2),(7,2,'ChIJh__NysDaaDURkeHOkGh_hbg','풍광',35.10093039999999600,128.84321610000000000,'대한민국 부산광역시','+82 51-831-3756','2024-11-21',1),(7,2,'ChIJs_bwDCzbaDURv1FUP9m_bIQ','하누리한우참소국밥',35.11163020000000000,128.88076210000000000,'대한민국 부산광역시 강서구 화전동 555-22','+82 51-972-0124','2024-11-21',5),(10,2,'ChIJSYAmsDQJDTURbfgFFHplRQc','제주민속촌',33.32251100000000000,126.84186800000000000,'대한민국 제주특별자치도 서귀포시 표선면 민속해안로 631-34','+82 64-787-4501','2024-12-19',2),(2,2,'ChIJ85Mt99EZDTURwVDY56YZxzs','김녕미로공원',33.53687680000000000,126.77253710000001000,'대한민국 제주특별자치도 제주시 구좌읍 만장굴길 122','+82 64-782-9266','2024-11-28',1),(2,2,'ChIJ-5DfghYTDTUR8RaKuI4FID4','섭지코지',33.42420949999999600,126.93111100000002000,'대한민국 제주특별자치도 서귀포시 성산읍 섭지코지로','+82 64-782-0080','2024-11-28',2),(2,2,'ChIJFUMOgbLkDDURsHuqz9O_RDA','용두암',33.51623230000000000,126.51206370000000000,'대한민국 제주특별자치도 제주시 용담2동 용두암길 15','+82 64-728-3918','2024-11-28',3),(2,2,'ChIJc9enKK5aDDURxqGjfTFYCVA','주상절리대',33.23775490000000000,126.42506710000000000,'대한민국 제주특별자치도 서귀포시 중문동','+82 64-738-1521','2024-11-28',4),(2,2,'ChIJ83lq-MBgDDURc64bn4fHZe8','한림공원',33.38952790000000000,126.23928839999998000,'대한민국 제주특별자치도 제주시 한림읍 한림로 300','+82 64-796-0001','2024-11-29',1),(2,2,'ChIJQUJUt8z3DDURLsRx2n5VCr4','테지움 제주',33.41227100000000000,126.39360890000000000,'대한민국 제주특별자치도 제주시 애월읍 평화로 2159','+82 64-799-4820','2024-11-29',2),(2,2,'ChIJK1UCDqrkDDURwsYol08j-xY','제주목 관아',33.51362299999999500,126.52203240000000000,'대한민국 제주특별자치도 제주시 관덕로7길 13','+82 64-710-6717','2024-11-29',3),(2,2,'ChIJSYAmsDQJDTURbfgFFHplRQc','제주민속촌',33.32251100000000000,126.84186800000000000,'대한민국 제주특별자치도 서귀포시 표선면 민속해안로 631-34','+82 64-787-4501','2024-11-30',1),(2,2,'ChIJVSkvCznjDDURxEceY37fW2A','국립제주박물관',33.51354620000000000,126.54889010000001000,'대한민국 제주특별자치도 제주시 일주동로 17','+82 64-720-8000','2024-11-30',2),(2,2,'ChIJ2zfVTyIDDTUR7FoT6693Bm8','절물오름',33.43312380000000000,126.62778459999998000,'대한민국 제주특별자치도 제주시 봉개동',NULL,'2024-11-30',3),(2,2,'ChIJA4qQV4oCDTURbZKi3viGOMY','제주절물자연휴양림',33.43936220000000000,126.62990999999998000,'대한민국 제주특별자치도 제주시 명림로 584','+82 64-728-1510','2024-12-01',1),(2,2,'ChIJQUJUt8z3DDURLsRx2n5VCr4','테지움 제주',33.41227100000000000,126.39360890000000000,'대한민국 제주특별자치도 제주시 애월읍 평화로 2159','+82 64-799-4820','2024-12-01',2),(3,2,'ChIJ-3yVeqPmYTURr_LRZ5xpRu0','루이식당',37.80034390000000000,128.91270250000000000,'대한민국 강원특별자치도 강릉시 강문동 창해로 439','+82 33-644-0439','2024-12-22',1),(3,2,'ChIJH7lHjHTlYTUR9nWTjRPBVRc','강릉 아라스파 펜션',37.81182409999999500,128.89863130000000000,'대한민국 강원특별자치도 강릉시 안현동 188-12',NULL,'2024-12-22',2),(3,2,'ChIJx9XhjvXCaDURrMhp9Mou4pw','하단통영식당',35.10706130000000000,128.96341530000000000,'대한민국 부산광역시 사하구 하단제2동 517-28','+82 51-205-8500','2024-12-27',1),(3,2,'ChIJ2VZtQ9zaaDURxv8fkQjDA20','삼성전기 부산사업장',35.09532650000000000,128.85566810000000000,'대한민국 부산광역시 강서구 녹산산업중로 333','+82 31-210-5114','2024-12-27',2),(3,2,'ChIJNwrkxFjCaDURHQU012Ke9cI','소문난흥부네집',35.11010489999999600,128.96075260000000000,'대한민국 부산광역시 사하구 하단동 503-4','+82 51-208-4763','2024-12-27',3),(3,2,'ChIJr0QQgfbCaDUROIdXK8wsFsI','노적봉 숯불구이',35.10567520000001000,128.96130660000000000,'대한민국 부산광역시 사하구 낙동남로 1366-3','+82 51-291-6689','2024-12-27',4),(3,2,'ChIJha26awrDaDURWg3mhwSUt4w','수림식당',35.10978150000000000,128.96469450000000000,'대한민국 부산광역시 사하구 하단동 781-1','+82 70-7758-3226','2024-12-27',5),(3,2,'ChIJb-yMkF3CaDURE79gTQNAwm4','은화수식당',35.11265030000000000,128.96621140000000000,'대한민국 부산광역시 사하구 낙동대로516번길 48','+82 51-292-8250','2024-12-27',6),(3,2,'ChIJ4d9T2emYfDURGK_RrkTeN0o','홍대입구역',37.55752700000000000,126.92446690000000000,'대한민국 서울특별시',NULL,'2024-12-26',1),(3,2,'ChIJvc4SpiXkYTUR1Vd7FjxfXDo','경포대',37.79507729999999600,128.89663439999998000,'대한민국 강원특별자치도 강릉시 경포로 365','+82 33-640-4471','2024-12-25',1),(3,2,'ChIJeX0RpiXkYTUResqZFyoPnhU','경포호(鏡浦湖)',37.79669829999999600,128.90205480000000000,'대한민국 강원특별자치도 강릉시 저동 94',NULL,'2024-12-25',2),(3,2,'ChIJH67aXS7DYTURxzsawRACDMg','모래시계공원',37.68738529999999500,129.03770989999998000,'대한민국 강원특별자치도 강릉시 강동면 헌화로 990','+82 33-640-4533','2024-12-25',3),(3,2,'ChIJU0YnfpfmYTUR_ctGmp92a4A','오월에 초당',37.79297270000000000,128.91461310000000000,'대한민국 강원특별자치도 강릉시 초당동','+82 33-651-0187','2024-12-24',1),(3,2,'ChIJm7moHPDlYTURZ3BFpb9MP8I','원조 강릉 교동반점 본점',37.75835100000000000,128.89304280000000000,'대한민국 강원특별자치도 강릉시 강릉대로 205','+82 33-646-3833','2024-12-24',2),(3,2,'ChIJkbI0FPnlYTURZ8wB4BuVfuo','넘버25호텔 강릉 경포점',37.80724209999999600,128.90268790000000000,'대한민국 강원특별자치도 강릉시 경포동 해안로 545',NULL,'2024-12-24',3),(3,2,'ChIJK0DY2pHlYTUR0toLkBRHthY','강릉꼬막집 독도네본점',37.75934990000000000,128.90039350000000000,'대한민국 강원특별자치도 강릉시 옥천로 47','+82 33-653-9658','2024-12-23',1),(3,2,'ChIJT8CBQQPnYTUR0pODETNVCYU','강릉맛집 피쉬맨대게횟집 경포대본점',37.80129290000000000,128.91151019999998000,'대한민국 강원특별자치도 강릉시 경포대, 해수욕장 강문동 260-26','+82 10-9644-0409','2024-12-23',2),(6,2,'ChIJeX0RpiXkYTUResqZFyoPnhU','경포호(鏡浦湖)',37.79669829999999600,128.90205480000000000,'대한민국 강원특별자치도 강릉시 저동 94',NULL,'2024-08-15',1),(6,2,'ChIJswhSpUTdYTURqGwblGbf_vU','강릉통일공원',37.72119280000000000,128.99876880000000000,'대한민국 강원특별자치도 강릉시 강동면 율곡로 1715-38','+82 33-640-4470','2024-08-15',2);
/*!40000 ALTER TABLE `visit_places` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

CREATE TABLE friends (
  from_user_email VARCHAR(255) NOT NULL,
  to_user_email VARCHAR(255) NOT NULL,
  status INT NOT NULL DEFAULT 1,
  PRIMARY KEY (from_user_email, to_user_email),
  FOREIGN KEY (from_user_email) REFERENCES users(email) ON DELETE CASCADE,
  FOREIGN KEY (to_user_email) REFERENCES users(email) ON DELETE CASCADE
);


-- Dump completed on 2024-11-25 17:53:15
