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
    Contact VARCHAR(10),
    Pic longblob
);

INSERT INTO users
(User_id, Username, Password, FName, Lname, NIC, Position, Contact, Pic)
VALUES

("U001","randil@gmail.com","randil@123", "John", "Dorie", "1234356V","Portfolio Manager", "0711234567", null),
("U002","vinod@gmail.com","vinod@123", "Vinod", "Kavinda", "1234356V","HR Manager", "0711254347",null),
("U003","dinuka@gmail.com","dinuka@123", "Dinuka", "Dulanjana", "1234356V","Accounting Manager", "0711234647",null),
("U004","deshani@gmail.com","deshani@123", "Deshani", "Bandara", "1234356V","Stock keeper", "0711254634",null);

CREATE TABLE if not exists customer (
  C_ID varchar(5) NOT NULL PRIMARY KEY,
  C_Name varchar(15) DEFAULT NULL,
  C_Location varchar(10) DEFAULT NULL,
  C_Contact VARCHAR(15) DEFAULT NULL
);

INSERT INTO customer
VALUES
('C001','Samanthi','Matara','715214560');

CREATE TABLE if not exists supplier (
  S_ID varchar(5) NOT NULL PRIMARY KEY,
  S_Name varchar(20) DEFAULT NULL,
  S_Contact varchar(15) DEFAULT NULL,
  Description varchar(25) DEFAULT NULL,
  S_Location varchar(15) DEFAULT NULL
);
  
INSERT INTO supplier
VALUES
('S001','Sammanee','076-3963385','books','Colobmo'),
('S002','Atlas','071-4563218','water bottle','rathnapura'),
('S003','Weerodara','076-7895412','pen, pencil','Anuradhapura'),
('S004','Promate','078-8521456','bags','Matara');

CREATE TABLE if not exists report (
  R_ID varchar(5) NOT NULL PRIMARY KEY,
  Date_ date,
  R_Name varchar(15) DEFAULT NULL,
  R_Description varchar(15) DEFAULT NULL
);


CREATE TABLE if not exists stock (
  P_ID varchar(5) NOT NULL PRIMARY KEY,
  P_Name varchar(20) DEFAULT NULL,
  Price_taken DECIMAL(5,2),
  Selling_price DECIMAL(5,2) DEFAULT NULL,
  Qty INT DEFAULT NULL,
  P_Description varchar(15) DEFAULT NULL,
  S_ID varchar(5) DEFAULT NULL,
  Total DECIMAL(10,2),
  FOREIGN KEY (S_ID) REFERENCES supplier (S_ID)
);

INSERT INTO stock
VALUES
('P001','CR pg120 SR',140.00,150.00,100,NULL,'S001',15000.00),
('P002','CR pg80 SR',90.00,100.00,200,NULL,'S002',20000.00),
('P003','CR pg80 SQR',92.00,100.00,200,NULL,'S003',20000),
('P004','Blue pens',35.00,40.00,200,NULL,'S004',8000.00),
('P005','Pencils',16.00,20.00,400,NULL,'S003',8000);

CREATE TABLE if not exists product_customer (
  C_ID varchar(5) DEFAULT NULL,
  P_ID varchar(5) DEFAULT NULL,
  FOREIGN KEY (C_ID) REFERENCES customer (C_ID),
  FOREIGN KEY (P_ID) REFERENCES stock (P_ID)
);


CREATE TABLE if not exists invoice (
  Invoice_id CHAR(6),
  Invoice_Type ENUM('Customer','Supplier'),
  Date_ DATE DEFAULT NULL,
  L_Name VARCHAR(10) DEFAULT NULL,
  Description VARCHAR(10) DEFAULT NULL,
  Qty INT,
  Price DECIMAL(10,2) DEFAULT NULL,
  S_ID VARCHAR(5) DEFAULT NULL,
  P_ID VARCHAR(5),
  PRIMARY KEY (Invoice_id),
  FOREIGN KEY (S_ID) REFERENCES supplier (S_ID),
  FOREIGN KEY (P_ID) REFERENCES stock (P_ID)
);

-- Set the initial Invoice_id value to 'I_001'
ALTER TABLE invoice AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER set_invoice_id
BEFORE INSERT ON invoice FOR EACH ROW
BEGIN
  SET NEW.Invoice_id = CONCAT('I_', LPAD(NEW.Invoice_id, 3, '0'));
END;
$$

DELIMITER ;



INSERT INTO invoice (Invoice_id,Invoice_Type, Date_, L_Name, Description, Qty, Price, S_ID, P_ID)
VALUES
('1','Supplier','2023-10-23',NULL,'CR pg80',100,15000.00,'S001','P001');

CREATE TABLE if not exists customer_invoice (
  C_ID varchar(5) NOT NULL,
  invoice_ID varchar(5) NOT NULL,
  PRIMARY KEY (C_ID,invoice_ID),
  FOREIGN KEY (C_ID) REFERENCES customer (C_ID),
  FOREIGN KEY (invoice_ID) REFERENCES invoice (Invoice_ID)
);

