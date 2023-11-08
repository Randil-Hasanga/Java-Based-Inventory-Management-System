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

ALTER TABLE users AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER set_user_id
BEFORE INSERT ON users FOR EACH ROW
BEGIN
  SET NEW.User_Id = CONCAT('U', LPAD(NEW.User_Id, 3, '0'));
END;
$$

DELIMITER ;

INSERT INTO users
(User_id, Username, Password, FName, Lname, NIC, Position, Contact, Pic)
VALUES

("1","randil@gmail.com","Randil@123", "Randil", "Hasanga", "364852648V","Stock keeper", "0711234567", null),
("2","vinod@gmail.com","Vinod@123", "Vinod", "Kavinda", "364826374V","HR Manager", "0711254347",null),
("3","dinuka@gmail.com","Dinuka@123", "Dinuka", "Dulanjana", "1234356789345","Accounting Manager", "0711234647",null),
("4","deshani@gmail.com","Deshani@123", "Deshani", "Bandara", "1234356453v","Portfolio Manager", "0711254634",null);


CREATE TABLE if not exists customer (
  C_ID varchar(5) NOT NULL PRIMARY KEY,
  C_Name varchar(15) DEFAULT NULL,
  C_Location varchar(10) DEFAULT NULL,
  C_Contact VARCHAR(15) DEFAULT NULL
);

ALTER TABLE customer AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER set_Cus_id
BEFORE INSERT ON customer FOR EACH ROW
BEGIN
  SET NEW.C_ID = CONCAT('C_', LPAD(NEW.C_ID, 3, '0'));
END;
$$

DELIMITER ;

INSERT INTO customer
VALUES
('1','Samanthi','Matara','715214560');

CREATE TABLE if not exists supplier (
  S_ID varchar(5) NOT NULL PRIMARY KEY,
  S_Name varchar(20) DEFAULT NULL,
  S_Contact varchar(15) DEFAULT NULL,
  Description varchar(25) DEFAULT NULL,
  S_Location varchar(15) DEFAULT NULL
);

ALTER TABLE supplier AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER set_Sup_id
BEFORE INSERT ON supplier FOR EACH ROW
BEGIN
  SET NEW.S_ID = CONCAT('S', LPAD(NEW.S_ID, 3, '0'));
END;
$$

DELIMITER ;
  
INSERT INTO supplier
VALUES
('1','Sammanee','076-3963385','books','Colobmo'),
('2','Atlas','071-4563218','water bottle','rathnapura'),
('3','Weerodara','076-7895412','pen, pencil','Anuradhapura'),
('4','Promate','078-8521456','bags','Matara');

CREATE TABLE if not exists report (
  R_ID varchar(5) NOT NULL PRIMARY KEY,
  Date_ date,
  pdf longblob default null
);
ALTER TABLE report AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER R_ID
BEFORE INSERT ON report FOR EACH ROW
BEGIN
  SET NEW.R_ID = CONCAT('R_', LPAD(NEW.R_ID, 3, '0'));
END;
$$

DELIMITER ;


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
('1','CR pg120 SR',140.00,150.00,100,NULL,'S001',15000.00),
('2','CR pg80 SR',90.00,100.00,200,NULL,'S002',20000.00),
('3','CR pg80 SQR',92.00,100.00,200,NULL,'S003',20000),
('4','Blue pens',35.00,40.00,200,NULL,'S004',8000.00),
('5','Pencils',16.00,20.00,400,NULL,'S003',8000);


ALTER TABLE stock AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER P_ID
BEFORE INSERT ON stock FOR EACH ROW
BEGIN
  SET NEW.P_ID = CONCAT('P', LPAD(NEW.P_ID, 3, '0'));
END;
$$

DELIMITER ;

CREATE TABLE if not exists transactions_sup (
  transaction_id CHAR(6),
  Date_ DATE DEFAULT NULL,
  L_Name VARCHAR(10) DEFAULT NULL,
  Description VARCHAR(10) DEFAULT NULL,
  Qty INT,
  Price DECIMAL(10,2) DEFAULT NULL,
  Total DECIMAL(10,2),
  S_ID VARCHAR(5) DEFAULT NULL,
  P_ID VARCHAR(5),
  PRIMARY KEY (transaction_id),
  FOREIGN KEY (S_ID) REFERENCES supplier (S_ID),
  FOREIGN KEY (P_ID) REFERENCES stock (P_ID)
);

-- Set the initial Invoice_id value to 'I_001'
ALTER TABLE transactions_sup AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER set_transaction_id_sup
BEFORE INSERT ON transactions_sup FOR EACH ROW
BEGIN
  SET NEW.transaction_id = CONCAT('T_', LPAD(NEW.transaction_id, 3, '0'));
END;
$$

DELIMITER ;

CREATE TABLE if not exists transactions_cus (
  transaction_id CHAR(6),
  Date_ DATE DEFAULT NULL,
  Description VARCHAR(10) DEFAULT NULL,
  Qty INT,
  Price DECIMAL(10,2) DEFAULT NULL,
  Total DECIMAL(10,2),
  C_ID VARCHAR(5) DEFAULT NULL,
  P_ID VARCHAR(5),
  PRIMARY KEY (transaction_id),
  FOREIGN KEY (C_ID) REFERENCES customer (C_ID),
  FOREIGN KEY (P_ID) REFERENCES stock (P_ID)
);


-- Set the initial Invoice_id value to 'I_001'
ALTER TABLE transactions_cus AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER set_transaction_id_cus
BEFORE INSERT ON transactions_cus FOR EACH ROW
BEGIN
  SET NEW.transaction_id = CONCAT('T_', LPAD(NEW.transaction_id, 3, '0'));
END;
$$

DELIMITER ;

CREATE TABLE if not exists temp_invoice (
  transaction_id CHAR(6),
  Date_ DATE DEFAULT NULL,
  Description VARCHAR(10) DEFAULT NULL,
  Qty INT,
  Price DECIMAL(10,2) DEFAULT NULL,
  Total DECIMAL(10,2),
  C_ID VARCHAR(5) DEFAULT NULL,
  P_ID VARCHAR(5),
  PRIMARY KEY (transaction_id),
  FOREIGN KEY (C_ID) REFERENCES customer (C_ID),
  FOREIGN KEY (P_ID) REFERENCES stock (P_ID)
);

ALTER TABLE temp_invoice AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER set_transaction_id_temp
BEFORE INSERT ON temp_invoice FOR EACH ROW
BEGIN
  SET NEW.transaction_id = CONCAT('T_', LPAD(NEW.transaction_id, 3, '0'));
END;
$$

DELIMITER ;

CREATE TABLE PDF_invoices (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    date_ DATE,
    C_ID VARCHAR(5),
    pdf LONGBLOB
);

CREATE TABLE if not exists temp_invoice_sup (
  transaction_id CHAR(6),
  Date_ DATE DEFAULT NULL,
  Description VARCHAR(10) DEFAULT NULL,
  Qty INT,
  Price DECIMAL(10,2) DEFAULT NULL,
  Total DECIMAL(10,2),
  S_ID VARCHAR(5) DEFAULT NULL,
  P_ID VARCHAR(5),
  PRIMARY KEY (transaction_id),
  FOREIGN KEY (S_ID) REFERENCES supplier (S_ID),
  FOREIGN KEY (P_ID) REFERENCES stock (P_ID)
);

ALTER TABLE temp_invoice_sup AUTO_INCREMENT = 1;

DELIMITER $$

CREATE TRIGGER set_transaction_id_temp_sup
BEFORE INSERT ON temp_invoice_sup FOR EACH ROW
BEGIN
  SET NEW.transaction_id = CONCAT('T_', LPAD(NEW.transaction_id, 3, '0'));
END;
$$

DELIMITER ;



