
-- #########
-- # Items #
-- #########

CREATE TABLE item
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name		VARCHAR(255) NOT NULL,
	description	VARCHAR(255) NOT NULL,
	price		DECIMAL NOT NULL,
	type		INT NOT NULL,
	deleted		BOOL NOT NULL DEFAULT 0
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
	type		INT NOT NULL,
	start_date	DATE,
	stop_date	DATE
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
	booth		INT NOT NULL,
	payed		BOOL NOT NULL
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
ALTER TABLE receipt_group_item_note ADD FOREIGN KEY (receipt_group_item_id) REFERENCES receipt_group_item(id) ON DELETE CASCADE;

ALTER TABLE receipt_group_sub_item_note ADD FOREIGN KEY (note_id) REFERENCES note(id);
ALTER TABLE receipt_group_sub_item_note ADD FOREIGN KEY (receipt_group_sub_item_id) REFERENCES receipt_group_sub_item(id) ON DELETE CASCADE;

-- ###########
-- # Account #
-- ###########

CREATE TABLE account
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT, 
	username	VARCHAR(255) NOT NULL UNIQUE,
	userhash	VARCHAR(255) NOT NULL,
	role		INT NOT NULL,
	first_name	VARCHAR(255) NOT NULL,
    last_name	VARCHAR(255) NOT NULL
);

CREATE TABLE shift
(
	id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	max_staff	INT NOT NULL,
	start		TIMESTAMP NOT NULL,
	stop		TIMESTAMP NOT NULL,
	description VARCHAR(255) NOT NULL,
	repeated	BOOL NOT NULL DEFAULT 0
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
	article_id			INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	article_name		VARCHAR(255),
	image		LONGBLOB,
	amount		DOUBLE,
	unit		VARCHAR(255),
	exp_date 	VARCHAR(255)
);

CREATE TABLE category
(
	category_id		INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	category_name	VARCHAR(255) NOT NULL
);

CREATE TABLE article_category
(
	category_id		INT NOT NULL,
    article_id		INT NOT NULL,
    PRIMARY KEY (category_id, article_id)
);
ALTER TABLE article_category ADD FOREIGN KEY (article_id) REFERENCES article(article_id);
ALTER TABLE article_category ADD FOREIGN KEY (category_id) REFERENCES category(category_id);

-- Skapa en ny kategoriserad article med värdena, Dryck, grönsaker, råvaror, färskvaror, tillbehör.


-- #############
-- # Book booth #
-- #############
CREATE TABLE book_booth
(
	booth_id	INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	persons		INT NOT NULL,
	date_time 	TIMESTAMP NOT NULL,
	name 		VARCHAR(255) NOT NULL,
	phone 		VARCHAR(20) NOT NULL,
	email 		VARCHAR(255) NOT NULL,
	status 		INT NOT NULL DEFAULT 0
);

-- #############
-- # Test Data #
-- #############

INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('root', 'toor', 1, 'Gustav', 'Åström');
INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('cook', 'password', 1, 'Sebastian', 'Persson');
INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('waitress', 'tipme', 2, 'Viktor', 'Spindler');