

-- #############
-- # Test Data #
-- #############



INSERT INTO category (category_name) VALUES
('Tillbehör');
INSERT INTO category (category_name) VALUES
('Grönsaker');
INSERT INTO category (category_name) VALUES
('Råvaror');
INSERT INTO category (category_name) VALUES
('Färskvaror');
INSERT INTO category (category_name) VALUES
('Dryck');

INSERT INTO article (article_name, amount, unit, exp_date) VALUES
('Citron', 650, 'gram', '2015-08-28');
INSERT INTO article (article_name, amount, unit, exp_date) VALUES
('Kyckling', 0.5, 'kg', '2015-09-15');
INSERT INTO article (article_name, amount, unit, exp_date) VALUES
('Potatis', 7, 'kg', '2015-11-13');
INSERT INTO article (article_name, amount, unit, exp_date) VALUES
('Ris', 0.5, 'kg', '2015-11-15');
INSERT INTO article (article_name, amount, unit, exp_date) VALUES
('Pasta', 7, 'kg', '2015-12-13');

INSERT INTO article_category (category_id, article_id) VALUES
(1, 1);
INSERT INTO article_category (category_id, article_id) VALUES
(3, 2);
INSERT INTO article_category (category_id, article_id) VALUES
(1, 3);
INSERT INTO article_category (category_id, article_id) VALUES
(1, 4);
INSERT INTO article_category (category_id, article_id) VALUES
(1, 5);

-----------------------------------------------

INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('root', 'toor', 1, 'Gustav', 'Åström');
INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('cook', 'password', 1, 'Sebastian', 'Persson');
INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('waitress', 'tipme', 2, 'Viktor', 'Spindler');

INSERT INTO menu (type, start_date, stop_date) VALUES (0, NOW(), '2015-12-31');

INSERT INTO menu (type, start_date, stop_date) VALUES (1, NOW(), '2015-12-31');

INSERT INTO menu (type, start_date, stop_date) VALUES (2, NOW(), '2015-12-31');

INSERT INTO menu_group (name, menu_id) VALUES ('Mat', 1);
INSERT INTO menu_group (name, menu_id) VALUES ('Tillbehör till våfflor', 1);

INSERT INTO menu_group (name, menu_id) VALUES ('Förrätter', 2);
INSERT INTO menu_group (name, menu_id) VALUES ('Huvudrätter', 2);
INSERT INTO menu_group (name, menu_id) VALUES ('Drycker', 3);

INSERT INTO item (name, description, type, price) VALUES
('Ostpanerad schnitzel med tomatgräddsås', '', 0, 75);
INSERT INTO item (name, description, type, price) VALUES
('Fiskgratäng med lax kummel och kräftor', '', 0, 75);
INSERT INTO item (name, description, type, price) VALUES
('Dagens Soppa och Hembakat bröd', '', 0, 45);
INSERT INTO item (name, description, type, price) VALUES
('Dagens Soppa och Hembakat bröd inkl dryck och kaffe', '', 0, 55);
INSERT INTO item (name, description, type, price) VALUES
('Matiga våfflor', '', 0, 45);
INSERT INTO item (name, description, type, price) VALUES
('Matiga våfflor inkl dryck och kaffe', '', 0, 55);
INSERT INTO item (name, description, type, price) VALUES
('Skagenröra, Kyckling', '', 0, 0);
INSERT INTO item (name, description, type, price) VALUES
('Tonfisk & Keso, Brie & Salami', '', 0, 0);
INSERT INTO item (name, description, type, price) VALUES
('Mozzarella & Tomat', '', 0, 0);

INSERT INTO item (name, description, type, price) VALUES
('Löjrom Lök Citron Crème Fraiche', 'Kalixlöjrom med klassiska tillbehör', 0, 235);
INSERT INTO item (name, description, type, price) VALUES
('Pilgrimsmussla Pumpa Krasse', 'Smörstekt pilgrimsmussla, friterad blåmussla, pumpacrème, inkokt pumpa, vattenkrasseskum och hyvlad morot', 0, 165);
INSERT INTO item (name, description, type, price) VALUES
('Hare Persiljerot Kantareller Granskott', 'Gravad, sotad hare med persiljerotspuré, smörstekta kantareller, friterad svartrot, lingon och granolja', 0, 165);
INSERT INTO item (name, description, type, price) VALUES
('Röding Ölgranité Lök', 'Rödingterrin serveras med purjolökscrème, ölgranité, citronkokt lök och friterad silverlök ', 0, 155);
INSERT INTO item (name, description, type, price) VALUES
('Vichyssoise Rotselleri Ren Löjrom', 'Vichyssoisse på rotselleri serveras med rökt ren, löjrom och krasse ', 0, 145);

INSERT INTO item (name, description, type, price) VALUES
('Pärlhöna Svamp Portvin Tryffel', 'Pannstekt pärlhöna serveras med potatispuré smaksatt med tryffel samt svamp, rökt fläsk och rödvinssås smaksatt med portvin och torkad frukt ', 0, 275);
INSERT INTO item (name, description, type, price) VALUES
('Häst Jordärtskocka Kål', 'Ryggbiff av häst serveras med rökt jordärtskockspuré, rostade jordärtskockor och spetskål', 2, 275);
INSERT INTO item (name, description, type, price) VALUES
('Gös Morot Kräfta Kantareller', 'Pannstekt gös serveras med morotsstomp smaksatt med kräfta, rostad svartrot, rökt fläsk, kantareller samt beurre blanc på kräftfond', 0, 295);
INSERT INTO item (name, description, type, price) VALUES
('Hjort Blomkål Betor Nötter', 'Hjortinnanlår serveras med blomkålscrème, rostade betor, picklad blomkål, rostade hasselnötter samt Granny Smith-gelé', 2, 295);
INSERT INTO item (name, description, type, price) VALUES
('Jordärtskocka Betor Svamp Nötter', 'Rökt jordärtskockspuré samt bakade rödbetor serveras med smörstekt svamp, friterat äpple, rostade hasselnötter, äppelgelé och linser kokta i portvin', 0, 295);

INSERT INTO item (name, description, type, price) VALUES
('Fanta', '', 1, 20);
INSERT INTO item (name, description, type, price) VALUES
('Coca Cola', '', 1, 20);
INSERT INTO item (name, description, type, price) VALUES
('Sprite', '', 1, 20);
INSERT INTO item (name, description, type, price) VALUES
('Coca Cola Zero', '', 1, 20);
INSERT INTO item (name, description, type, price) VALUES
('Falcon', '', 1, 59);
INSERT INTO item (name, description, type, price) VALUES
('Staropramen', '', 1, 59);
INSERT INTO item (name, description, type, price) VALUES
('Heiniken', '', 1, 59);
INSERT INTO item (name, description, type, price) VALUES
('Mariestad', '', 1, 59);
INSERT INTO item (name, description, type, price) VALUES
('Husets vin (rött)', '', 1, 79);
INSERT INTO item (name, description, type, price) VALUES
('Husets vin (vitt)', '', 1, 79);

INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 1);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 2);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 3);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 4);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 5);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (1, 6);

INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (2, 7);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (2, 8);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (2, 9);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (3, 10);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (3, 11);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (3, 12);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (3, 13);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (3, 14);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (4, 15);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (4, 16);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (4, 17);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (4, 18);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (4, 19);

INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 20);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 21);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 22);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 23);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 24);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 25);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 26);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 27);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 28);
INSERT INTO menu_group_item (menu_group_id, item_id) VALUES (5, 29);