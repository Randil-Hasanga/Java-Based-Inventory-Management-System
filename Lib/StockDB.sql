CREATE DATABASE if not exists StockDB;

USE StockDB;

CREATE TABLE if not exists users
(
	User_Id VARCHAR(10) NOT NULL PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    Password VARCHAR(20),
    FName VARCHAR(50),
    Lname VARCHAR(50),
    NIC VARCHAR(20),
    Position VARCHAR(20),
    Contact VARCHAR(10)
);

INSERT INTO users
(User_id, FName, Lname, NIC, Position, Contact)
VALUES
("U001","randil@gmail.com","randil@123", "John", "Dorie", "1234356V","Manager", "0711234567");
