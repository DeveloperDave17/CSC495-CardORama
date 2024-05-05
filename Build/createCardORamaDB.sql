CREATE DATABASE IF NOT EXISTS `CardORama`
CHARACTER SET = 'utf16';

USE `CardORama`;

CREATE TABLE IF NOT EXISTS `Users` (
   `email` VARCHAR(256) NOT NULL,
   `username` VARCHAR(60) NOT NULL,
   PRIMARY KEY(`email`)
);

CREATE TABLE IF NOT EXISTS `Friends` (
   `email` VARCHAR(256),
   `friendEmail` VARCHAR(256),
   PRIMARY KEY (`email`, `friendEmail`),
   FOREIGN KEY (`email`) REFERENCES `Users` (`email`),
   FOREIGN KEY (`friendEmail`) REFERENCES `Users` (`email`)
);

CREATE TABLE IF NOT EXISTS `FriendRequests` (
   `email` VARCHAR(256),
   `friendEmail` VARCHAR(256),
   `friendRequestStatus` ENUM('PENDING', 'DECLINED') NOT NULL,
   `requestCount` INT NOT NULL,
   PRIMARY KEY (`email`, `friendEmail`),
   FOREIGN KEY (`email`) REFERENCES `Users` (`email`),
   FOREIGN KEY (`friendEmail`) REFERENCES `Users` (`email`)
);

CREATE TABLE IF NOT EXISTS `FlashcardSets` (
   `setID` BIGINT AUTO_INCREMENT,
   `email` VARCHAR(256) NOT NULL,
   `setName` VARCHAR(128),
   `privacy` ENUM('PRIVATE', 'FRIENDS', 'PUBLIC'),
   `color` CHAR(6),
   priority INT,
   PRIMARY KEY (`setID`),
   FOREIGN KEY (`email`) REFERENCES `Users` (`email`)
);

CREATE TABLE IF NOT EXISTS `Flashcards` (
   `flashcardID` BIGINT AUTO_INCREMENT,
   `setID` BIGINT NOT NULL,
   `term` VARCHAR(250),
   `definition` VARCHAR(1000),
   `position` INT NOT NULL,
   PRIMARY KEY (`flashcardID`),
   FOREIGN KEY (`setID`) REFERENCES `FlashcardSets` (`setID`)
);

CREATE TABLE IF NOT EXISTS `FavoritedFlashcardSets` (
   `email` VARCHAR(256),
   `setID` BIGINT,
   PRIMARY KEY (`email`, `setID`)
);
