
-- ###########
-- # Charset #
-- ###########

-- DEFAULT CHARACTER SET utf8;
-- DEFAULT COLLATE utf8_general_ci;

-- #########
-- # Items #
-- #########

CREATE TABLE item
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name		VARCHAR(255) NOT NULL,
	description	VARCHAR(255) NOT NULL,
	price		DECIMAL NOT NULL,
	type		INT NOT NULL
);

CREATE TABLE item_item
(
	item_id_dom		INT NOT NULL,
	item_id_sub		INT NOT NULL,
	PRIMARY KEY (item_id_dom, item_id_sub)
);

ALTER TABLE item_item ADD FOREIGN KEY (item_id_dom) REFERENCES item(id);
ALTER TABLE item_item ADD FOREIGN KEY (item_id_sub) REFERENCES item(id);

-- ########
-- # Menu #
-- ########

CREATE TABLE menu
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name		VARCHAR(255) NOT NULL,
	start_date	TIMESTAMP,
	stop_date	TIMESTAMP
);

CREATE TABLE menu_group
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	menu_id		INT NOT NULL,
	name		VARCHAR(255)
);

CREATE TABLE menu_group_item
(
	item_id			INT NOT NULL,
	menu_group_id	INT NOT NULL,
	PRIMARY KEY (item_id, menu_group_id)
);

--ALTER TABLE menu_group ADD FOREIGN KEY (menu_id) REFERENCES menu(id);
ALTER TABLE menu_group_item ADD FOREIGN KEY (item_id) REFERENCES item(id);
-- ALTER TABLE menu_group_item ADD FOREIGN KEY (menu_group_id) REFERENCES menu_group(id);

-- CASCADE DELETE (override)
ALTER TABLE menu_group ADD CONSTRAINT menu_group_cascade
	FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE;
ALTER TABLE menu_group_item ADD CONSTRAINT menu_group_item_cascade
	FOREIGN KEY (menu_group_id) REFERENCES menu_group(id) ON DELETE CASCADE;

-- ############
-- # Ordering #
-- ############

CREATE TABLE receipt
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	booth		INT NOT NULL
);

CREATE TABLE receipt_item
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	item_id		INT NOT NULL
);

CREATE TABLE receipt_item_item
(
	receipt_item_id_dom	INT NOT NULL,
	receipt_item_id_sub	INT NOT NULL,
	PRIMARY KEY (receipt_item_id_dom, receipt_item_id_sub)
);

CREATE TABLE receipt_group
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	receipt_id	INT NOT NULL,
	status		VARCHAR (255) NOT NULL
);

CREATE TABLE receipt_group_item
(
	receipt_item_id		INT NOT NULL,
	receipt_group_id	INT NOT NULL,
	PRIMARY KEY (receipt_item_id, receipt_group_id)
);

ALTER TABLE receipt_item ADD FOREIGN KEY (item_id) REFERENCES item(id);

ALTER TABLE receipt_item_item ADD FOREIGN KEY (receipt_item_id_dom) REFERENCES receipt_item(id);
ALTER TABLE receipt_item_item ADD FOREIGN KEY (receipt_item_id_sub) REFERENCES receipt_item(id);

ALTER TABLE receipt_group ADD FOREIGN KEY (receipt_id) REFERENCES receipt(id);

ALTER TABLE receipt_group_item ADD FOREIGN KEY (receipt_item_id) REFERENCES receipt_item(id);
ALTER TABLE receipt_group_item ADD FOREIGN KEY (receipt_group_id) REFERENCES receipt_group(id);

-- #########
-- # Notes #
-- #########

CREATE TABLE note
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	text		VARCHAR(255) NOT NULL
);

CREATE TABLE receipt_item_note
(
	note_id			INT NOT NULL,
	receipt_item_id	INT NOT NULL,
	PRIMARY KEY (note_id, receipt_item_id)
);

ALTER TABLE receipt_item_note ADD FOREIGN KEY (note_id) REFERENCES note(id);
ALTER TABLE receipt_item_note ADD FOREIGN KEY (receipt_item_id) REFERENCES receipt_item(id);

-- ###########
-- # Account #
-- ###########

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
	start_date	TIMESTAMP NOT NULL,
	stop_date	TIMESTAMP NOT NULL
);

CREATE TABLE schedule
(
	shift_id	INT NOT NULL,
	account_id	INT NOT NULL,
	PRIMARY KEY	(account_id, shift_id)
);

ALTER TABLE employee ADD FOREIGN KEY (account_id) REFERENCES account(id);
ALTER TABLE schedule ADD FOREIGN KEY (account_id) REFERENCES account(id);
ALTER TABLE schedule ADD FOREIGN KEY (shift_id) REFERENCES shift(id);

-- ###########
-- # Storage #
-- ###########

CREATE TABLE article
(
	id			INT PRIMARY KEY,
	name		VARCHAR(255),
	image		LONGBLOB,
	amount		INT,
	category	VARCHAR(255)
);

-- #############
-- # Test Data #
-- #############

INSERT INTO account (username, userhash, role) VALUES 
('root', 'toor', 0);
INSERT INTO account (username, userhash, role) VALUES 
('boss', 'password', 1);
INSERT INTO account (username, userhash, role) VALUES 
('waitress', 'tipme', 2);

INSERT INTO employee (account_id, first_name, last_name) VALUES
(1, 'Gustav', 'Åström');
INSERT INTO employee (account_id, first_name, last_name) VALUES
(2, 'Sebastian', 'Persson');
INSERT INTO employee (account_id, first_name, last_name) VALUES
(3, 'Viktor', 'Spindler');

INSERT INTO menu (name, start_date, stop_date) VALUES ('lunch', NOW(), NOW());
-- INSERT INTO menu (name, startdate, stopdate) VALUES ('dinner');
-- INSERT INTO menu (name, startdate, stopdate) VALUES ('alacarte');

INSERT INTO menu_group (name, menu_id) VALUES ('Mat', 1);
INSERT INTO menu_group (name, menu_id) VALUES ('Dryck', 1);


INSERT INTO item (name, description, type, price) VALUES
('Beef Stew', 'mouth watering description', 0, 79.99);
INSERT INTO item (name, description, type, price) VALUES
('Smoked Rabbit', 'mouth watering description', 0, 59.99);
INSERT INTO item (name, description, type, price) VALUES
('Tartar Sauce', 'mouth watering description', 0, 4.99);
INSERT INTO item (name, description, type, price) VALUES
('Vitlökssås', 'gott', 0, 4.99);
INSERT INTO item (name, description, type, price) VALUES
('Kanelbulle', 'bäst', 0, 35);

INSERT INTO item_item VALUES (2, 4);

INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 1);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 2);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 3);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 4);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 5);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (2, 4);

INSERT INTO receipt (booth) VALUES (1);
INSERT INTO receipt (booth) VALUES (2);
INSERT INTO receipt (booth) VALUES (3);

INSERT INTO receipt_group (status, receipt_id) VALUES ('initial', 1);
INSERT INTO receipt_group (status, receipt_id) VALUES ('readyForKitchen', 2);
INSERT INTO receipt_group (status, receipt_id) VALUES ('done', 3);

INSERT INTO receipt_item (item_id) VALUES (1);
INSERT INTO receipt_item (item_id) VALUES (2);
INSERT INTO receipt_item (item_id) VALUES (2);
INSERT INTO receipt_item (item_id) VALUES (4);
INSERT INTO receipt_item (item_id) VALUES (1);


INSERT INTO receipt_item_item VALUES (2, 4);

INSERT INTO receipt_group_item (receipt_item_id, receipt_group_id) VALUES (1, 1);
INSERT INTO receipt_group_item (receipt_item_id, receipt_group_id) VALUES (2, 2);
INSERT INTO receipt_group_item (receipt_item_id, receipt_group_id) VALUES (3, 3);
INSERT INTO receipt_group_item (receipt_item_id, receipt_group_id) VALUES (5, 2);