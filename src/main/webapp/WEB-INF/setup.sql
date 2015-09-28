CREATE TABLE account
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	username	VARCHAR(255) NOT NULL,
	userhash	VARCHAR(255) NOT NULL,
	role		INT NOT NULL
);

CREATE TABLE employee 
(
	account_id	INT NOT NULL PRIMARY KEY NOT NULL,
	first_name	VARCHAR(255) NOT NULL,
	last_name	VARCHAR(255) NOT NULL
);

CREATE TABLE shift
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	minimum		INT NOT NULL,
	maximum		INT NOT NULL,
	startdate	DATE NOT NULL,
	stopdate	DATE NOT NULL
);

CREATE TABLE schedule
(
	shift_id	INT NOT NULL,
	account_id	INT NOT NULL,
	PRIMARY KEY	(account_id, shift_id)
);

CREATE TABLE menu
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name		VARCHAR(255) NOT NULL
);

CREATE TABLE item
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name		VARCHAR(255) NOT NULL,
	description	VARCHAR(255) NOT NULL,
	price		DECIMAL NOT NULL,
	foodtype	INT NOT NULL
);

CREATE TABLE menu_item
(
	menu_id		INT NOT NULL,
	item_id		INT NOT NULL,
	PRIMARY KEY (menu_id, item_id)
);

CREATE TABLE note
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	text		VARCHAR(255) NOT NULL
);

CREATE TABLE receipt
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE receipt_item
(
	id			INT NOT NULL AUTO_INCREMENT,
	item_id		INT NOT NULL,
	receipt_id	INT NOT NULL,
	note_id		INT,
	receipt_item_group_id	INT NOT NULL,

	PRIMARY KEY (id, item_id, receipt_id)
);

CREATE TABLE receipt_item_group
(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	status VARCHAR(255) NOT NULL
);

ALTER TABLE employee ADD FOREIGN KEY (account_id) REFERENCES account(id);
ALTER TABLE schedule ADD FOREIGN KEY (account_id) REFERENCES account(id);
ALTER TABLE schedule ADD FOREIGN KEY (shift_id) REFERENCES shift(id);
ALTER TABLE menu_item ADD FOREIGN KEY (menu_id) REFERENCES menu(id);
ALTER TABLE menu_item ADD FOREIGN KEY (item_id) REFERENCES item(id);
ALTER TABLE receipt_item ADD FOREIGN KEY (item_id) REFERENCES item(id);
ALTER TABLE receipt_item ADD FOREIGN KEY (receipt_id) REFERENCES receipt(id);
ALTER TABLE receipt_item ADD FOREIGN KEY (note_id) REFERENCES note(id);
ALTER TABLE receipt_item ADD FOREIGN KEY (receipt_item_group_id) REFERENCES receipt_item_group(id);

INSERT INTO account (username, userhash, role) VALUES 
('root', 'toor', 0);
INSERT INTO account (username, userhash, role) VALUES 
('boss', 'password', 1);
INSERT INTO account (username, userhash, role) VALUES 
('waitress', 'tipme', 2);

INSERT INTO menu (name) VALUES ('lunch');
INSERT INTO menu (name) VALUES ('dinner');
INSERT INTO menu (name) VALUES ('alacarte');

INSERT INTO item (name, description, foodtype, price) VALUES 
('Beef Stew', 'mouth watering description', 0, 79.99);
INSERT INTO item (name, description, foodtype, price) VALUES
('Smoked Rabbit', 'mouth watering description', 0, 59.99);
INSERT INTO item (name, description, foodtype, price) VALUES 
('Tartar Sauce', 'mouth watering description', 0, 4.99);
INSERT INTO item (name, description, foodtype, price) VALUES 
('Vitlökssås', 'gott', 0, 4.99);



INSERT INTO receipt () VALUES ();
INSERT INTO receipt () VALUES ();
INSERT INTO receipt () VALUES ();

INSERT INTO receipt_item_group (status) VALUES ('initial');
INSERT INTO receipt_item_group (status) VALUES ('readyForKitchen');
INSERT INTO receipt_item_group (status) VALUES ('done');

INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) VALUES (1, 1, NULL, 1);
INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) VALUES (2, 2, NULL, 1);
INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) VALUES (4, 1, NULL, 1);
INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) VALUES (1, 2, NULL, 2);
INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) VALUES (2, 1, NULL, 2);
INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) VALUES (3, 1, NULL, 2);
INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) VALUES (4, 2, NULL, 3);
INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) VALUES (2, 1, NULL, 3);
INSERT INTO receipt_item (item_id, receipt_id, note_id, receipt_item_group_id) VALUES (4, 3, NULL, 3);

