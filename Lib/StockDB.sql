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

INSERT INTO users
(User_id, FName, Lname, NIC, Position, Contact)
VALUES
("U002","vinod@gmail.com","vinod@123", "Vinod", "Kavinda", "1234356V","HR Manager", "0711254347"),
("U003","dinuka@gmail.com","dinuka@123", "Dinuka", "Dulanjana", "1234356V","Accounting Manager", "0711234647"),
("U00","deshani@gmail.com","deshani@123", "Deshani", "Bandara", "1234356V","Stock keeper", "0711254634");
