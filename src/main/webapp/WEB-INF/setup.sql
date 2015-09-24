
-- excerpt from Drive/Database/Sql


CREATE TABLE menu
(
    id              INT PRIMARY KEY,
    name            VARCHAR(255)
);

CREATE TABLE item
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255) NOT NULL,
    price           DECIMAL NOT NULL,
    type            INT
);

CREATE TABLE menu_item
(
    menu_id         INT,
    item_id         INT,
    PRIMARY KEY     (menu_id, item_id)
);


ALTER TABLE menu_item ADD FOREIGN KEY (menu_id) REFERENCES menu(id);
ALTER TABLE menu_item ADD FOREIGN KEY (item_id) REFERENCES item(id);


INSERT INTO menu VALUES (0, 'lunch');
INSERT INTO menu VALUES (1, 'dinner');
INSERT INTO menu VALUES (2, 'alacarte');

INSERT INTO item VALUES (0, 'Beef Stew', 'mouth watering description', 75, 0);
INSERT INTO item VALUES (1, 'Smoked Rabbit', 'mouth watering description', 59, 0);
INSERT INTO item VALUES (2, 'Tartar Sauce', 'mouth watering description', 15, 0);
INSERT INTO item VALUES (3, 'Vitlökssås', 'gott', 15, 0);

INSERT INTO menu_item VALUES (0, 0);
INSERT INTO menu_item VALUES (0, 1);
INSERT INTO menu_item VALUES (0, 2);

