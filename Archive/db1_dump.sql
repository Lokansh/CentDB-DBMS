--------------------------------------
-- SQL DATA EXPORT FOR DATABASE: `db1`
--------------------------------------

USE DATABASE db1;
--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
INSERT INTO `courses` VALUES ('c2','CSCI1000','2');
INSERT INTO `courses` VALUES ('c1','CSCI1000','3');
UNLOCK TABLES;

--
-- Dumping data for table `db1_schema`
--

LOCK TABLES `db1_schema` WRITE;
INSERT INTO `db1_schema` VALUES ('OrderID int','');
INSERT INTO `db1_schema` VALUES ('OrderNumber int','');
INSERT INTO `db1_schema` VALUES ('FKPersonId int','');
INSERT INTO `db1_schema` VALUES ('PRIMARY KEY (OrderID)','');
INSERT INTO `db1_schema` VALUES ('FOREIGN KEY (FKPersonId) REFERENCES testTable11(PersonID)','');
INSERT INTO `db1_schema` VALUES ('FOREIGN KEY (FKPersonId2) REFERENCES testTable12(PersonID);');
INSERT INTO `db1_schema` VALUES ('');
INSERT INTO `db1_schema` VALUES ('[table11]');
INSERT INTO `db1_schema` VALUES ('PersonID int','');
INSERT INTO `db1_schema` VALUES ('Personname varchar','');
INSERT INTO `db1_schema` VALUES ('PRIMARY KEY (OrderID);');
INSERT INTO `db1_schema` VALUES ('');
INSERT INTO `db1_schema` VALUES ('[table12]');
INSERT INTO `db1_schema` VALUES ('PersonID int','');
INSERT INTO `db1_schema` VALUES ('Personname varchar','');
INSERT INTO `db1_schema` VALUES ('PRIMARY KEY (OrderID);');
UNLOCK TABLES;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
INSERT INTO `students` VALUES ('1','Team21');
INSERT INTO `students` VALUES ('2','Shelja');
INSERT INTO `students` VALUES ('3','Lokansh');
INSERT INTO `students` VALUES ('22','hello');
INSERT INTO `students` VALUES ('7','hello');
INSERT INTO `students` VALUES ('8','yrbhdff');
INSERT INTO `students` VALUES ('3','hello');
INSERT INTO `students` VALUES ('5','dfsfs');
INSERT INTO `students` VALUES ('null5','null','null','dbshfsfds','');
INSERT INTO `students` VALUES ('null5','null','null','dbshfsfds','');
UNLOCK TABLES;

