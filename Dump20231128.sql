CREATE DATABASE  IF NOT EXISTS `swdtest` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `swdtest`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: swdtest
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `address` varchar(255) DEFAULT NULL,
  `balance` double NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `FKra7xoi9wtlcq07tmoxxe5jrh4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('8 Kingsford Center',429321,'Orbadiah','Curness','836 960 3286',1),('15 Ruskin Circle',388060,'Asher','Thying','574 610 1779',2),('477 Glacier Hill Center',345018,'Northrup','Town','609 428 5388',3),('4 Elka Park',433503,'Anya','Dugget','377 975 3706',4),('1389 Schurz Center',384518,'Jobina','Kadar','785 725 4749',5),('67324 Longview Lane',470157,'Jessamine','Ledbury','301 906 9825',6),('6 Johnson Drive',382356,'Fancy','Parkman','518 841 3044',7),('0495 Dawn Road',180246,'Celesta','Scorthorne','501 811 0670',8),('9025 Doe Crossing Lane',438879,'Brandy','McCrum','629 324 8171',9),('4702 Summit Point',387607,'Theo','Tarbatt','812 362 5004',10),('9925 Xioa Lame',438879,'John','McCrummy','629 324 8171',11),('7672 Pho Moi',400000,'Do','Quang','124 785 8374',12),('123 Main St',0,'John','Doe','5551234666',13),('125 Sub St',0,'Jane','Doe','5551234888',14),('125 Alabama St',0,'James','Doe','5551234777',15),('125 Alabama St',0,'John','Smith','5551239999',16),('Dekon 124 St',0,'Anna','Leki','5551236231',17),('Dong Nguyen',0,'Đỗ','Quang','0378077486',18);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_USER'),(2,'ROLE_MODERATOR'),(3,'ROLE_ADMIN');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_done` tinyint(1) NOT NULL DEFAULT '0',
  `position` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbhwpp8tr117vvbxhf5sbkdkc9` (`user_id`),
  CONSTRAINT `FKbhwpp8tr117vvbxhf5sbkdkc9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,0,3,'Task 10',9),(2,0,2,'Task 2',9),(3,0,6,'Task 3',9),(4,0,5,'Task 4',9),(5,0,1,'Task 5',9),(6,0,4,'Task 6',9),(7,0,1,'Task 7',8),(8,0,2,'Task 8',8);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(4,2),(5,2),(6,2),(16,2),(17,2),(18,2),(1,3),(2,3),(3,3);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(60) DEFAULT NULL,
  `enable` tinyint NOT NULL,
  `password` varchar(120) NOT NULL,
  `username` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin1@gmail.com',1,'$2a$10$eX4WUa5hCp2lUhy7Antl3O50bc1hq.i23LPh/JPZuHNu0kmTd6tte','admin'),(2,'admin2@gmail.com',1,'$2a$10$cgDa4ko2/leELufK556jsuSnWz7qLylzn.SzFW2T4Q9E6lCp6Bfo.','admin2'),(3,'admin3@gmail.com',1,'$2a$10$G4zw.HBRf1JvCFZjkU4vieJWtIdvp8P29bYAksa/wVDrGoVGpknDW','admin3'),(4,'mod12@gmail.com',1,'$2a$10$OvRb//kPYpOJl.pN2fsl0OiCve5J1vMoGrP3g7WUV7RkGcNWrn2HG','mod12'),(5,'mod22@gmail.com',1,'$2a$10$K1SF7cA02uS8gwgn9V2a3.2THPFUqudrS0k2F7hCojF4noM3eIL32','mod22'),(6,'mod3@gmail.com',1,'$2a$10$YgaAzwGJHo3Insqf5cstL.kDPudkBiKR09nnQCG/aJqQYOBKfvr4q','mod3'),(7,'user@gmail.com',1,'$2a$10$oQZxIY.CT9cfoyyYhXGzOuu2iDYvYiPzmGk1DzV.wN7LV4FEEzID.','user'),(8,'kuandx42@gmail.com',0,'$2a$10$NhRnq/deAy9exibeQ3UNc.gwMhyEy5yyQYZfCKZK06ODe8Uxt/59u','user1'),(9,'user2@gmail.com',1,'$2a$10$j0gv4Ijig75IlchFObo.dev7zoLzbkZfKUcnYNgSGa2qeerqjI8sO','user2'),(10,'test@gmail.com',0,'$2a$10$J.y78rjGjJ91mQdB5Gxbte/EJhcfdRnmMsDHPBCmyg5KEKJWgJ0wC','test'),(11,'user3@gmail.com',0,'$2a$10$j0gv4Ijig75IlchFObo.dev7zoLzbkZfKUcnYNgSGa2qeerqjI8sO','user3'),(12,'doquang@gmail.com',0,'$2a$10$j0gv4Ijig75IlchFObo.dev7zoLzbkZfKUcnYNgSGa2qeerqjI8sO','quangdx'),(13,'johndoe@gmail.com',1,'$2a$10$VrwK6WbhmZXGNljoLgdXDeU8NobgV2EWKX6qucQywk7rfnIe6pEPu','john_doe'),(14,'janedoe@gmail.com',1,'$2a$10$e2Q/zT3Eucz/kJi4ZZLcGudgv3NmfxS7/CKQ5DxURUF/jmD//txbC','jane_doe'),(15,'jamesdoe@gmail.com',0,'$2a$10$PySCDS9zesBtjoepsSrDw.44URUwsA/ITv.QHX071DQGKDvJfVMDy','james_doe'),(16,'Unknown@gmail.com',1,'$2a$10$O6xZPsgK0usnTPYR17LWq.hSvKmoiy./z0ITzSCgWkUGV8EB6Q7ue','John Smith'),(17,'annabelle@gmail.com',1,'$2a$10$Z9deZXKrMgVRgAex8Ayz4OtoQguPq89t67/ILWl8x.S/5InAUX3le','Anna Leki'),(18,'dienhuynhgpt@gmail.com',0,'$2a$10$BID.okKvP5bK/jrLOmiDKulhQ1Tya2b.cGxe4wkdqH7Rh1gp4pHBS','Dummy');
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

-- Dump completed on 2023-11-28 14:51:13
