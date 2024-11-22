CREATE DATABASE  IF NOT EXISTS `trip` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `trip`;
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

LOCK TABLES `plans` WRITE;
/*!40000 ALTER TABLE `plans` DISABLE KEYS */;
INSERT INTO `plans` VALUES (1,2,'여행1','2024-11-19 05:38:37','2024-11-22','2024-11-23',2,'가족여행~'),(2,2,'제주도 가족여행','2024-11-19 05:43:01','2024-11-28','2024-12-01',4,'제주도 겨울 가족여행 갑니다~'),(3,2,'크리스마스 강릉여행','2024-11-19 07:33:12','2024-12-25','2024-12-27',3,'바다 보러 갑니다~'),(4,2,'여행입니다~','2024-11-19 07:33:42','2024-11-28','2024-11-30',3,'설명입니다~'),(5,2,'ㅇ','2024-11-19 07:34:27','2024-11-19','2024-11-26',8,'ㅇ'),(6,2,'여름 여행','2024-11-19 07:35:26','2024-08-15','2024-08-18',4,'여름이다~'),(7,2,'여행','2024-11-19 07:36:35','2024-11-20','2024-11-21',2,'여행이다!'),(8,2,'ㅇ','2024-11-19 07:36:50','2024-11-19','2024-11-26',8,'ㅇㄴ'),(9,2,'여행간다~~~','2024-11-19 07:39:18','2025-01-15','2025-01-17',3,'여행을 갑니다요');
/*!40000 ALTER TABLE `plans` ENABLE KEYS */;
UNLOCK TABLES;

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
  `phone` varchar(15),
  `email` varchar(254) NOT NULL,
  `grade` tinyint NOT NULL DEFAULT '3',
  `role` varchar(100) NOT NULL DEFAULT 'NORMAL',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifyDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `is_verified` TINYINT(1) NOT NULL DEFAULT '0',
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
INSERT INTO `users` VALUES (1,'$2a$10$cFFs1kgIPYkB14IbpuEyqefdqhkHvNPa9qpHqKalUDI8bqWmZfeVy','홍길동','경로당소드마스터','01012345678','0',3,'NORMAL','2024-11-15 08:39:03','2024-11-15 08:39:03',1,0),(2,'$2a$10$z..j4zAHM.X5ozOK4i02vusgHYgjbBZup3.Jk4YhRXrv1iYxucEtG','이은선','모르는개산책','01012345678','98silverline@gmail.com',3,'NORMAL','2024-11-15 08:41:24','2024-11-15 08:41:24',1,0),(3,'$2a$10$qi1cuxavbLPzo7XthD/NgON/Pt55wVjCPN5VmcPySvkwuDqz3a6ti','김철수','손흥민의야구교실','01012345678','abc@gmail.com',3,'NORMAL','2024-11-15 08:43:21','2024-11-15 08:43:21',1,0),(5,'$2a$10$e7594FBPaySt4RvzZQ/u3OBK6rQsaEznddq8gUYEXObsNQNs6BTlO','김유저','집가고싶다','01098746212','user123@gmail.com',3,'NORMAL','2024-11-18 02:53:46','2024-11-18 02:53:46',1,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

DROP TABLE IF EXISTS `verification_tokens`;
CREATE TABLE `verification_tokens` (
    token VARCHAR(100) NOT NULL,
    user_email VARCHAR(254) NOT NULL,
    expiry_date DATETIME NOT NULL,
    PRIMARY KEY (token),
    FOREIGN KEY (user_email) REFERENCES users(email) ON DELETE CASCADE
);
DROP TABLE IF EXISTS `password_reset_tokens`;
CREATE TABLE password_reset_tokens (
    token VARCHAR(255) PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    expiry_date DATETIME NOT NULL,
    FOREIGN KEY (user_email) REFERENCES users(email) ON DELETE CASCADE
);
-- Dump completed on 2024-11-19 17:35:21
