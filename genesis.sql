CREATE DATABASE  IF NOT EXISTS `genesis` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `genesis`;
-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: genesis
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `budget`
--

DROP TABLE IF EXISTS `budget`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `budget` (
  `idBudget` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `total` int(10) NOT NULL,
  `frozen` float DEFAULT '0',
  `spent` float DEFAULT '0',
  `holder_id` int(11) NOT NULL,
  `start_date` varchar(40) NOT NULL,
  PRIMARY KEY (`idBudget`),
  KEY `id_idx` (`holder_id`),
  CONSTRAINT `holder_id` FOREIGN KEY (`holder_id`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `budget`
--

LOCK TABLES `budget` WRITE;
/*!40000 ALTER TABLE `budget` DISABLE KEYS */;
INSERT INTO `budget` VALUES (6,'General',1000,34,200,12,'2018-12-10'),(7,'General',3000,0,0,13,'2018-12-10'),(11,'General',1000,0,50,28,'2018-12-10'),(12,'General',1000,0,100,31,'2018-12-11');
/*!40000 ALTER TABLE `budget` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `order` (
  `idOrder` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(45) NOT NULL,
  `price` float NOT NULL,
  `requisitioner_id` int(11) NOT NULL,
  `estimated_delivery` int(11) NOT NULL,
  `staff_name` varchar(45) NOT NULL,
  `rationale` varchar(500) DEFAULT 'Description',
  `status` varchar(45) DEFAULT 'Inserted',
  `message` varchar(500) DEFAULT 'Your order has successfully been forwarded. Awaiting response.',
  `budget_id` int(11) DEFAULT NULL,
  `budget_holder_id` int(11) NOT NULL,
  `insertion_date` varchar(45) DEFAULT NULL,
  `days_count` int(11) DEFAULT '0',
  `approval_date` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idOrder`),
  KEY `requisitioner_id_idx` (`requisitioner_id`),
  KEY `budget_id_idx` (`budget_id`),
  KEY `budget_holder_id_idx` (`budget_holder_id`),
  CONSTRAINT `budget_holder_id` FOREIGN KEY (`budget_holder_id`) REFERENCES `user` (`iduser`),
  CONSTRAINT `budget_id` FOREIGN KEY (`budget_id`) REFERENCES `budget` (`idbudget`),
  CONSTRAINT `requisitioner_id` FOREIGN KEY (`requisitioner_id`) REFERENCES `user` (`iduser`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (6,'White Board',-90,15,2,'jack','','Rejected','Cost is wrong, this is rejected!',NULL,12,'2018-12-10',0,NULL),(12,'Laptop',4000,15,7,'Joemon','Wants to buy Macbook','Inserted','Your order has successfully been forwarded. Awaiting response.',NULL,12,'2018-12-10',0,NULL),(16,'Projector',900,27,7,'Joe','For the room 404 in SAWB building','Rejected','It\'s expensive!',NULL,28,'2018-12-10',0,NULL),(17,'Whiteboard',100,27,5,'Helen','For the room 404 SAWB building, from XYZ company','Rejected','We don\'t do business with XYZ company according to Uni policy!',11,28,'2018-12-10',0,NULL),(18,'HDMI',50,27,4,'Joemon','for room 404a ','Received','',11,28,'2018-12-10',0,'2018-12-10'),(19,'Laptop',100,30,2,'John','ansda','Rejected','anu',NULL,31,'2018-12-11',0,NULL),(20,'sec',100,30,2,'john','akasd','Received','',12,31,'2018-12-11',0,'2018-12-11'),(21,'third',22,30,25,'saa','gy','Rejected','de',12,31,'2018-12-11',0,NULL),(22,'HDMI Cable',10,15,4,'Freddy','Only RGB cable in Room 404','Approved','',6,12,'2018-12-11',0,NULL),(23,'Microphone',24.99,15,7,'Alonso','For LT1.2','Approved','',6,12,'2018-12-11',0,NULL);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `number` varchar(45) DEFAULT 'N/A',
  `email` varchar(45) DEFAULT 'N/A',
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `idUser_UNIQUE` (`idUser`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'zain','123','Admin','Zain Sra','07365903256','zain@uog.uk'),(12,'sam','1234','BudgetHolder','Samuel','00-22-33-1-2','sam@uog.co.uk'),(13,'norah','1234','BudgetHolder','Norah','221-31-22-3','norah@uog.co.uk'),(14,'zimo','1234','PurchasingOfficer','Zimo','0021-3321-21','zimo@uog.co.uk'),(15,'yasser','1234','Requisitioner','Yasser','002-420-007','yasir@uog.co.uk'),(17,'anna','1234','PurchasingOfficer','Anna','221-31-212','anna@uog.co.uk'),(18,'jack','1234','PurchasingOfficer','Jack','22-1-33-456','jack@uog.co.uk'),(19,'Temp','1234','BudgetHolder','Asad','','asd@uog.co.uk'),(27,'john','john123','Requisitioner','John','211-22-33-2','john@uog.co.uk'),(28,'smith','1234','BudgetHolder','Smith','1221-33-22','smith@uog.co.uk'),(29,'jane','1234','PurchasingOfficer','Jane','2211-11-2-3','jane@uog.co.uk'),(30,'req','1234','Requisitioner','req','212213-1','req@co.uk'),(31,'bh','1234','BudgetHolder','bh','2312-123-123','bh@BH'),(32,'po','1234','PurchasingOfficer','po','1231-123-123','po@opo');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-17 20:23:39
