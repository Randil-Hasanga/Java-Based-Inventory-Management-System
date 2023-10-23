-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: se
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
-- Table structure for table `accounting_manager`
--

DROP TABLE IF EXISTS `accounting_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounting_manager` (
  `User_ID` varchar(5) DEFAULT NULL,
  `A_ID` varchar(5) DEFAULT NULL,
  `A_Name` varchar(15) DEFAULT NULL,
  KEY `User_ID` (`User_ID`),
  CONSTRAINT `accounting_manager_ibfk_1` FOREIGN KEY (`User_ID`) REFERENCES `user` (`User_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounting_manager`
--

LOCK TABLES `accounting_manager` WRITE;
/*!40000 ALTER TABLE `accounting_manager` DISABLE KEYS */;
/*!40000 ALTER TABLE `accounting_manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `C_ID` varchar(5) NOT NULL,
  `C_Name` varchar(15) DEFAULT NULL,
  `C_Location` varchar(10) DEFAULT NULL,
  `C_Contact` int DEFAULT NULL,
  PRIMARY KEY (`C_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('C001','Samanthi','Matara',715214560);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_invoice`
--

DROP TABLE IF EXISTS `customer_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_invoice` (
  `C_ID` varchar(5) NOT NULL,
  `L_ID` varchar(5) NOT NULL,
  PRIMARY KEY (`C_ID`,`L_ID`),
  KEY `L_ID` (`L_ID`),
  CONSTRAINT `customer_invoice_ibfk_1` FOREIGN KEY (`C_ID`) REFERENCES `customer` (`C_ID`),
  CONSTRAINT `customer_invoice_ibfk_2` FOREIGN KEY (`L_ID`) REFERENCES `invoice` (`L_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_invoice`
--

LOCK TABLES `customer_invoice` WRITE;
/*!40000 ALTER TABLE `customer_invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hr_manager`
--

DROP TABLE IF EXISTS `hr_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hr_manager` (
  `User_ID` varchar(5) DEFAULT NULL,
  `HR_ID` varchar(5) DEFAULT NULL,
  `HR_Name` varchar(15) DEFAULT NULL,
  KEY `User_ID` (`User_ID`),
  CONSTRAINT `hr_manager_ibfk_1` FOREIGN KEY (`User_ID`) REFERENCES `user` (`User_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hr_manager`
--

LOCK TABLES `hr_manager` WRITE;
/*!40000 ALTER TABLE `hr_manager` DISABLE KEYS */;
/*!40000 ALTER TABLE `hr_manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `L_ID` varchar(5) NOT NULL,
  `L_Type` varchar(10) DEFAULT NULL,
  `Date` date DEFAULT NULL,
  `L_Name` varchar(10) DEFAULT NULL,
  `Description` varchar(10) DEFAULT NULL,
  `Price` double(10,2) DEFAULT NULL,
  `S_ID` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`L_ID`),
  KEY `S_ID` (`S_ID`),
  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`S_ID`) REFERENCES `supplier` (`S_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES ('L001','credit ','2023-10-23',NULL,'book 100',15000.00,'S001');
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portfolio_manager`
--

DROP TABLE IF EXISTS `portfolio_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `portfolio_manager` (
  `User_ID` varchar(5) DEFAULT NULL,
  `PM_ID` varchar(5) DEFAULT NULL,
  `PM_Name` varchar(15) DEFAULT NULL,
  KEY `User_ID` (`User_ID`),
  CONSTRAINT `portfolio_manager_ibfk_1` FOREIGN KEY (`User_ID`) REFERENCES `user` (`User_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portfolio_manager`
--

LOCK TABLES `portfolio_manager` WRITE;
/*!40000 ALTER TABLE `portfolio_manager` DISABLE KEYS */;
/*!40000 ALTER TABLE `portfolio_manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `P_ID` varchar(5) NOT NULL,
  `P_Name` varchar(20) DEFAULT NULL,
  `Price` double(10,2) DEFAULT NULL,
  `Qty` varchar(5) DEFAULT NULL,
  `P_Description` varchar(15) DEFAULT NULL,
  `S_ID` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`P_ID`),
  KEY `S_ID` (`S_ID`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`S_ID`) REFERENCES `supplier` (`S_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES ('P001','Books',150.00,'10',NULL,'S001');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_customer`
--

DROP TABLE IF EXISTS `product_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_customer` (
  `C_ID` varchar(5) DEFAULT NULL,
  `P_ID` varchar(5) DEFAULT NULL,
  KEY `C_ID` (`C_ID`),
  KEY `P_ID` (`P_ID`),
  CONSTRAINT `product_customer_ibfk_1` FOREIGN KEY (`C_ID`) REFERENCES `customer` (`C_ID`),
  CONSTRAINT `product_customer_ibfk_2` FOREIGN KEY (`P_ID`) REFERENCES `product` (`P_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_customer`
--

LOCK TABLES `product_customer` WRITE;
/*!40000 ALTER TABLE `product_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `R_ID` varchar(5) NOT NULL,
  `R_Name` varchar(15) DEFAULT NULL,
  `R_Description` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`R_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_keeper`
--

DROP TABLE IF EXISTS `stock_keeper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_keeper` (
  `User_ID` varchar(5) DEFAULT NULL,
  `STO_ID` varchar(5) DEFAULT NULL,
  `STO_Name` varchar(15) DEFAULT NULL,
  KEY `User_ID` (`User_ID`),
  CONSTRAINT `stock_keeper_ibfk_1` FOREIGN KEY (`User_ID`) REFERENCES `user` (`User_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_keeper`
--

LOCK TABLES `stock_keeper` WRITE;
/*!40000 ALTER TABLE `stock_keeper` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_keeper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `S_ID` varchar(5) NOT NULL,
  `S_Name` varchar(20) DEFAULT NULL,
  `S_Contact` varchar(15) DEFAULT NULL,
  `Description` varchar(25) DEFAULT NULL,
  `S_Location` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`S_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES ('S001','Sammanee','076-3963385','books','Colobmo'),('S002','Atlas','071-4563218','water bottle','rathnapura'),('S003','Weerodara','076-7895412','pen, pencil','Anuradhapura'),('S004','Promate','078-8521456','bags','Matara');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `User_ID` varchar(5) NOT NULL,
  `U_Name` varchar(10) DEFAULT NULL,
  `U_Type` varchar(10) DEFAULT NULL,
  `U_Contact` int DEFAULT NULL,
  PRIMARY KEY (`User_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('U001','Deshani','PM001',715735339);
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

-- Dump completed on 2023-10-23 16:19:12
