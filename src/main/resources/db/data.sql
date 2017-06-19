-- MySQL dump 10.13  Distrib 5.7.18, for Linux (x86_64)
--
-- Host: localhost    Database: svk
-- ------------------------------------------------------
-- Server version	5.7.18-0ubuntu0.17.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `account`
--

/*!40000 ALTER TABLE account DISABLE KEYS */;
INSERT INTO account VALUES (1,false,'Tom','De Dobbeleer',NULL,'ADMIN',NULL,'tompels@gmail.com','$2a$10$/LycK.tWQCz66LvQxl.kHOQP867T5JmYF1BEgZqdN/gCLnK1aAwr6',false,false,1,NULL,NULL,NULL),(2,false,'fréderic','baervoets',NULL,'USER',NULL,'test@test.com','$2a$10$sGGmP8vCPQihhzNFzGbNNO.v9XwYMh/JtlB59iO5h9gCKp2zESNmK','\0','\0',5,NULL,NULL,NULL);
/*!40000 ALTER TABLE account ENABLE KEYS */;

--
-- Dumping data for table `account_profile`
--

/*!40000 ALTER TABLE account_profile DISABLE KEYS */;
INSERT INTO account_profile VALUES (1,NULL,'MIDFIELDER','0032478304124','016123123',5,8,NULL,NULL),(2,NULL,'DEFENDER','','',6,6,NULL,NULL),(3,NULL,'MIDFIELDER','','',NULL,NULL,NULL,NULL),(4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,NULL,NULL,NULL,NULL,NULL,NULL,'2016-11-17 13:52:45','2016-11-17 13:52:45'),(10,NULL,NULL,NULL,NULL,NULL,NULL,'2017-06-09 20:13:09','2017-06-09 20:13:09');
/*!40000 ALTER TABLE account_profile ENABLE KEYS */;

--
-- Dumping data for table `address`
--

/*!40000 ALTER TABLE address DISABLE KEYS */;
INSERT INTO address VALUES (1,'Geldenaaksebaan 119','Oud-Heverlee','https://maps.google.be/maps?q=Geldenaaksebaan 119, +3050+Oud-Heverlee&output=embed',3050,NULL,NULL),(2,'Geldenaaksebaan 2','Heverlee',NULL,3001,NULL,NULL),(3,'TestStraat 1','Halle',NULL,1500,NULL,NULL),(4,'TestStraat 1','Halle',NULL,1500,NULL,NULL),(5,'Teststraat 123','Halle',NULL,1500,NULL,NULL),(6,'Teststraat 123','Halle',NULL,1500,NULL,NULL),(7,'Teststreet 1','Belgium','https://maps.google.be/maps?q=Teststreet 1, +9999+Belgium&output=embed',9999,'2017-06-09 20:11:13','2017-06-09 20:11:13'),(8,'Teststreet 1','New York',NULL,9999,'2017-06-09 20:12:04','2017-06-09 20:12:04');
/*!40000 ALTER TABLE address ENABLE KEYS */;

--
-- Dumping data for table `comment`
--

/*!40000 ALTER TABLE comment DISABLE KEYS */;
/*!40000 ALTER TABLE comment ENABLE KEYS */;

--
-- Dumping data for table `comment_news`
--

/*!40000 ALTER TABLE comment_news DISABLE KEYS */;
/*!40000 ALTER TABLE comment_news ENABLE KEYS */;

--
-- Dumping data for table `doodle`
--

/*!40000 ALTER TABLE doodle DISABLE KEYS */;
INSERT INTO doodle VALUES (294912),(425984);
/*!40000 ALTER TABLE doodle ENABLE KEYS */;

--
-- Dumping data for table `goals`
--

/*!40000 ALTER TABLE goals DISABLE KEYS */;
INSERT INTO goals VALUES (7,0,NULL,NULL,NULL,NULL,NULL),(8,1,NULL,NULL,NULL,NULL,NULL),(16,0,2,1,28,'2017-06-09 20:32:24','2017-06-09 20:32:24');
/*!40000 ALTER TABLE goals ENABLE KEYS */;

--
-- Dumping data for table `hibernate_sequences`
--

/*!40000 ALTER TABLE hibernate_sequences DISABLE KEYS */;
INSERT INTO hibernate_sequences VALUES ('doodle',14);
/*!40000 ALTER TABLE hibernate_sequences ENABLE KEYS */;

--
-- Dumping data for table `identity_option`
--

/*!40000 ALTER TABLE identity_option DISABLE KEYS */;
INSERT INTO identity_option VALUES (52,1,3,'2017-06-09 20:33:14','2017-06-09 20:33:14'),(53,2,3,'2017-06-09 20:33:14','2017-06-09 20:33:14');
/*!40000 ALTER TABLE identity_option ENABLE KEYS */;

--
-- Dumping data for table `image`
--

/*!40000 ALTER TABLE image DISABLE KEYS */;
INSERT INTO image VALUES (1,'profile/faui8vo9poldtjbieuc7','http://res.cloudinary.com/dtwkkwtee/image/upload/v1449925384/profile/faui8vo9poldtjbieuc7.jpg',NULL,NULL),(2,'profile/bzw9reabjr5yxzfcv6tm','http://res.cloudinary.com/dtwkkwtee/image/upload/v1449925649/profile/bzw9reabjr5yxzfcv6tm.jpg',NULL,NULL),(3,'profile/gsyd84nlav8nanyrghhl','http://res.cloudinary.com/dtwkkwtee/image/upload/v1449925712/profile/gsyd84nlav8nanyrghhl.jpg',NULL,NULL),(4,'profile/a2r4k7rebt0itou2fxbj','http://res.cloudinary.com/dtwkkwtee/image/upload/v1449925790/profile/a2r4k7rebt0itou2fxbj.jpg',NULL,NULL),(5,'profile/my9yufrx7lbgjio8fnv1','http://res.cloudinary.com/dtwkkwtee/image/upload/v1449928213/profile/my9yufrx7lbgjio8fnv1.jpg',NULL,NULL),(6,'profile/tblbgugabkh16pf4g0ec','http://res.cloudinary.com/dtwkkwtee/image/upload/v1449928280/profile/tblbgugabkh16pf4g0ec.jpg',NULL,NULL),(7,'profile/h2a4hymgayiss1yqcurd','http://res.cloudinary.com/dtwkkwtee/image/upload/v1450018688/profile/h2a4hymgayiss1yqcurd.jpg',NULL,NULL),(8,'profile/pwyhyxvjfjrhvwakyutk','https://res.cloudinary.com/dtwkkwtee/image/upload/v1450093800/profile/pwyhyxvjfjrhvwakyutk.jpg',NULL,NULL);
/*!40000 ALTER TABLE image ENABLE KEYS */;

--
-- Dumping data for table `matches`
--

/*!40000 ALTER TABLE matches DISABLE KEYS */;
INSERT INTO matches VALUES (1,3,'2014-12-20 20:30:00',2,2,1,NULL,1,294912,'NOT_PLAYED',NULL,NULL,'2017-06-09 20:31:01','2017-06-09 20:31:01',NULL,NULL),(28,1,'2017-06-09 20:30:00',1,1,2,NULL,1,425984,'PLAYED',NULL,3,'2017-06-09 20:31:24','2017-06-09 20:32:24',NULL,NULL);
/*!40000 ALTER TABLE matches ENABLE KEYS */;

--
-- Dumping data for table `matches_goals`
--

/*!40000 ALTER TABLE matches_goals DISABLE KEYS */;
INSERT INTO matches_goals VALUES (1,7),(1,8);
/*!40000 ALTER TABLE matches_goals ENABLE KEYS */;

--
-- Dumping data for table `multiple_choice_player_vote`
--

/*!40000 ALTER TABLE multiple_choice_player_vote DISABLE KEYS */;
INSERT INTO multiple_choice_player_vote VALUES (2,2);
/*!40000 ALTER TABLE multiple_choice_player_vote ENABLE KEYS */;

--
-- Dumping data for table `news`
--

/*!40000 ALTER TABLE news DISABLE KEYS */;
INSERT INTO news VALUES (1,'<h2>TestHeading</h2>\r\n\r\n<p>TestMessage</p>\r\n\r\n<p><strong>Test</strong>\r\nTëst</p>\r\n\r\n<p><a href=\"http://link.com\" rel=\"nofollow\">Link</a></p>\r\n\r\n<blockquote>\r\n  <p>Quote</p>\r\n</blockquote>\r\n\r\n<p><code>Code</code></p>\r\n\r\n<ol><li>List1</li><li>List2</li></ol>','TestMessage','2014-09-13 15:58:41',1,'2017-06-09 20:10:25','2017-06-09 20:10:25');
/*!40000 ALTER TABLE news ENABLE KEYS */;

--
-- Dumping data for table `persistent_logins`
--

/*!40000 ALTER TABLE persistent_logins DISABLE KEYS */;
/*!40000 ALTER TABLE persistent_logins ENABLE KEYS */;

--
-- Dumping data for table `players_poll`
--

/*!40000 ALTER TABLE players_poll DISABLE KEYS */;
INSERT INTO players_poll VALUES (3);
/*!40000 ALTER TABLE players_poll ENABLE KEYS */;

--
-- Dumping data for table `poll_options`
--

/*!40000 ALTER TABLE poll_options DISABLE KEYS */;
/*!40000 ALTER TABLE poll_options ENABLE KEYS */;

--
-- Dumping data for table `polls`
--

/*!40000 ALTER TABLE polls DISABLE KEYS */;
INSERT INTO polls VALUES (2,'2016-11-15 06:13:31','Automatically generated: Who will be man of the match?','2016-11-12 06:13:31','CLOSED','2016-11-12 06:13:31','2017-04-09 13:01:18'),(3,'2017-06-12 20:32:24','Automatically generated: Who will be man of the match?','2017-06-09 20:32:24','OPEN','2017-06-09 20:32:24','2017-06-09 20:32:24');
/*!40000 ALTER TABLE polls ENABLE KEYS */;

--
-- Dumping data for table `presences`
--

/*!40000 ALTER TABLE presences DISABLE KEYS */;
INSERT INTO presences VALUES (30,0,1,294912,NULL,NULL),(33,0,2,294912,NULL,NULL),(51,0,1,425984,'2017-06-09 20:32:47','2017-06-09 20:32:47'),(52,0,2,425984,'2017-06-09 20:32:52','2017-06-09 20:32:52');
/*!40000 ALTER TABLE presences ENABLE KEYS */;

--
-- Dumping data for table `season`
--

/*!40000 ALTER TABLE season DISABLE KEYS */;
INSERT INTO season VALUES (1,'2014-2015',NULL,NULL);
/*!40000 ALTER TABLE season ENABLE KEYS */;

--
-- Dumping data for table `teams`
--

/*!40000 ALTER TABLE teams DISABLE KEYS */;
INSERT INTO teams VALUES (1,'Test Team 1',8,'2017-06-09 20:11:13','2017-06-09 20:12:14'),(2,'Test Team 2',8,'2017-06-09 20:12:04','2017-06-09 20:12:04');
/*!40000 ALTER TABLE teams ENABLE KEYS */;

--
-- Dumping data for table `vote`
--

/*!40000 ALTER TABLE vote DISABLE KEYS */;
INSERT INTO vote VALUES (2,3,1,'2017-06-09 20:33:18','2017-06-09 20:33:18');
/*!40000 ALTER TABLE vote ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-09 20:39:31