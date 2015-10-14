
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

CREATE TABLE receipt_group
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	receipt_id	INT NOT NULL,
	status		VARCHAR (255) NOT NULL
);

CREATE TABLE receipt_group_item
(
	id					INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	receipt_group_id	INT NOT NULL,
	item_id				INT NOT NULL
);

CREATE TABLE receipt_group_sub_item
(
	id									INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	receipt_group_item_id				INT NOT NULL,
	item_id								INT NOT NULL
);


ALTER TABLE receipt_group ADD FOREIGN KEY (receipt_id) REFERENCES receipt(id) ON DELETE CASCADE;

ALTER TABLE receipt_group_item ADD FOREIGN KEY (item_id) REFERENCES item(id);
ALTER TABLE receipt_group_item ADD FOREIGN KEY (receipt_group_id) REFERENCES receipt_group(id) ON DELETE CASCADE;

ALTER TABLE receipt_group_sub_item ADD FOREIGN KEY (item_id) REFERENCES item(id);
ALTER TABLE receipt_group_sub_item ADD FOREIGN KEY (receipt_group_item_id) REFERENCES receipt_group_item(id) ON DELETE CASCADE;

-- #########
-- # Notes #
-- #########

CREATE TABLE note
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	text		VARCHAR(255) NOT NULL
);

CREATE TABLE receipt_group_item_note
(
	note_id					INT NOT NULL,
	receipt_group_item_id	INT NOT NULL,
	PRIMARY KEY (note_id, receipt_group_item_id)
);

CREATE TABLE receipt_group_sub_item_note
(
	note_id						INT NOT NULL,
	receipt_group_sub_item_id	INT NOT NULL,
	PRIMARY KEY (note_id, receipt_group_sub_item_id)
);

ALTER TABLE receipt_group_item_note ADD FOREIGN KEY (note_id) REFERENCES note(id);
ALTER TABLE receipt_group_item_note ADD FOREIGN KEY (receipt_group_item_id) REFERENCES receipt_group_item(id);

ALTER TABLE receipt_group_sub_item_note ADD FOREIGN KEY (note_id) REFERENCES note(id);
ALTER TABLE receipt_group_sub_item_note ADD FOREIGN KEY (receipt_group_sub_item_id) REFERENCES receipt_group_sub_item(id);

-- ###########
-- # Account #
-- ###########

CREATE TABLE account
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	username	VARCHAR(255) NOT NULL,
	userhash	VARCHAR(255) NOT NULL,
	role		INT NOT NULL,
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

ALTER TABLE schedule ADD FOREIGN KEY (account_id) REFERENCES account(id);
ALTER TABLE schedule ADD FOREIGN KEY (shift_id) REFERENCES shift(id);

-- ###########
-- # Storage #
-- ###########

CREATE TABLE article
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name		VARCHAR(255),
	image		LONGBLOB,
	category	VARCHAR(255),
	amount		DOUBLE,
	unit		VARCHAR(255),
	exp_date 	VARCHAR(255)
);

-- #############
-- # Test Data #
-- #############

INSERT INTO article (name, category, amount, unit, exp_date) VALUES
('Citron', 'Grönsak', 650, 'gram', '2015-08-28');
INSERT INTO article (name, category, amount, unit, exp_date) VALUES
('Kyckling', 'Kött', 0.5, 'kg', '2015-09-15');
INSERT INTO article (name, category, amount, unit, exp_date) VALUES
('Potatis', 'Tillbehör', 7, 'kg', '2015-11-13');

INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('root', 'toor', 1, 'Gustav', 'Åström');
INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('cook', 'password', 1, 'Sebastian', 'Persson');
INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('waitress', 'tipme', 2, 'Viktor', 'Spindler');

INSERT INTO menu (name, start_date, stop_date) VALUES ('Lunch', NOW(), '2038-01-19 03:14:07');
-- INSERT INTO menu (name, startdate, stopdate) VALUES ('dinner');
-- INSERT INTO menu (name, startdate, stopdate) VALUES ('alacarte');

INSERT INTO menu_group (name, menu_id) VALUES ('Mat', 1);
INSERT INTO menu_group (name, menu_id) VALUES ('Dryck', 1);

INSERT INTO item (name, description, type, price) VALUES
('Beef Stew', 'mouth watering description', 0, 79.99);
INSERT INTO item (name, description, type, price) VALUES
('Smoked Rabbit', 'mouth watering description', 2, 59.99);
INSERT INTO item (name, description, type, price) VALUES
('Tartar Sauce', 'mouth watering description', 0, 4.99);
INSERT INTO item (name, description, type, price) VALUES
('Vitlökssås', 'gott', 0, 4.99);
INSERT INTO item (name, description, type, price) VALUES
('Kanelbulle', 'bäst', 2, 35);

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
INSERT INTO receipt (booth) VALUES (4);

INSERT INTO receipt_group (status, receipt_id) VALUES ('initial', 1);
INSERT INTO receipt_group (status, receipt_id) VALUES ('readyForKitchen', 2);
INSERT INTO receipt_group (status, receipt_id) VALUES ('done', 3);
INSERT INTO receipt_group (status, receipt_id) VALUES ('readyForKitchen', 4);

INSERT INTO receipt_group_item (item_id, receipt_group_id) VALUES (1, 2);
INSERT INTO receipt_group_item (item_id, receipt_group_id) VALUES (2, 2);
INSERT INTO receipt_group_item (item_id, receipt_group_id) VALUES (3, 2);
INSERT INTO receipt_group_item (item_id, receipt_group_id) VALUES (5, 2);

INSERT INTO receipt_group_sub_item (item_id, receipt_group_item_id) VALUES (4, 2);


INSERT INTO note (text) VALUES ('rare');
INSERT INTO note (text) VALUES ('extra mkt');
INSERT INTO note (text) VALUES ('dubbel');

INSERT INTO receipt_group_item_note (note_id, receipt_group_item_id) VALUES (1, 2);
INSERT INTO receipt_group_item_note (note_id, receipt_group_item_id) VALUES (1, 1);

INSERT INTO receipt_group_item_note (note_id, receipt_group_item_id) VALUES (2, 3);

INSERT INTO receipt_group_sub_item_note (note_id, receipt_group_sub_item_id) VALUES (2, 1);
INSERT INTO receipt_group_sub_item_note (note_id, receipt_group_sub_item_id) VALUES (3, 1);


SET NAMES "UTF-8";