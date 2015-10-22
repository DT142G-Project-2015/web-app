

-- #############
-- # Test Data #
-- #############



INSERT INTO category (category_name) VALUES
('Tillbeh�r');
INSERT INTO category (category_name) VALUES
('Gr�nsaker');
INSERT INTO category (category_name) VALUES
('R�varor');
INSERT INTO category (category_name) VALUES
('F�rskvaror');
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
('root', 'toor', 1, 'Gustav', '�str�m');
INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('cook', 'password', 1, 'Sebastian', 'Persson');
INSERT INTO account (username, userhash, role, first_name, last_name) VALUES
('waitress', 'tipme', 2, 'Viktor', 'Spindler');

INSERT INTO menu (type, start_date, stop_date) VALUES (0, NOW(), '2015-12-31');

INSERT INTO menu (type, start_date, stop_date) VALUES (1, NOW(), '2015-12-31');

INSERT INTO menu (type, start_date, stop_date) VALUES (2, NOW(), '2015-12-31');

INSERT INTO menu_group (name, menu_id) VALUES ('Mat', 1);
INSERT INTO menu_group (name, menu_id) VALUES ('Tillbeh�r till v�fflor', 1);

INSERT INTO menu_group (name, menu_id) VALUES ('F�rr�tter', 2);
INSERT INTO menu_group (name, menu_id) VALUES ('Huvudr�tter', 2);
INSERT INTO menu_group (name, menu_id) VALUES ('Drycker', 3);

INSERT INTO item (name, description, type, price) VALUES
('Ostpanerad schnitzel med tomatgr�dds�s', '', 0, 75);
INSERT INTO item (name, description, type, price) VALUES
('Fiskgrat�ng med lax kummel och kr�ftor', '', 0, 75);
INSERT INTO item (name, description, type, price) VALUES
('Dagens Soppa och Hembakat br�d', '', 0, 45);
INSERT INTO item (name, description, type, price) VALUES
('Dagens Soppa och Hembakat br�d inkl dryck och kaffe', '', 0, 55);
INSERT INTO item (name, description, type, price) VALUES
('Matiga v�fflor', '', 0, 45);
INSERT INTO item (name, description, type, price) VALUES
('Matiga v�fflor inkl dryck och kaffe', '', 0, 55);
INSERT INTO item (name, description, type, price) VALUES
('Skagenr�ra, Kyckling', '', 0, 0);
INSERT INTO item (name, description, type, price) VALUES
('Tonfisk & Keso, Brie & Salami', '', 0, 0);
INSERT INTO item (name, description, type, price) VALUES
('Mozzarella & Tomat', '', 0, 0);

INSERT INTO item (name, description, type, price) VALUES
('L�jrom L�k Citron Cr�me Fraiche', 'Kalixl�jrom med klassiska tillbeh�r', 0, 235);
INSERT INTO item (name, description, type, price) VALUES
('Pilgrimsmussla Pumpa Krasse', 'Sm�rstekt pilgrimsmussla, friterad bl�mussla, pumpacr�me, inkokt pumpa, vattenkrasseskum och hyvlad morot', 0, 165);
INSERT INTO item (name, description, type, price) VALUES
('Hare Persiljerot Kantareller Granskott', 'Gravad, sotad hare med persiljerotspur�, sm�rstekta kantareller, friterad svartrot, lingon och granolja', 0, 165);
INSERT INTO item (name, description, type, price) VALUES
('R�ding �lgranit� L�k', 'R�dingterrin serveras med purjol�kscr�me, �lgranit�, citronkokt l�k och friterad silverl�k ', 0, 155);
INSERT INTO item (name, description, type, price) VALUES
('Vichyssoise Rotselleri Ren L�jrom', 'Vichyssoisse p� rotselleri serveras med r�kt ren, l�jrom och krasse ', 0, 145);

INSERT INTO item (name, description, type, price) VALUES
('P�rlh�na Svamp Portvin Tryffel', 'Pannstekt p�rlh�na serveras med potatispur� smaksatt med tryffel samt svamp, r�kt fl�sk och r�dvinss�s smaksatt med portvin och torkad frukt ', 0, 275);
INSERT INTO item (name, description, type, price) VALUES
('H�st Jord�rtskocka K�l', 'Ryggbiff av h�st serveras med r�kt jord�rtskockspur�, rostade jord�rtskockor och spetsk�l', 2, 275);
INSERT INTO item (name, description, type, price) VALUES
('G�s Morot Kr�fta Kantareller', 'Pannstekt g�s serveras med morotsstomp smaksatt med kr�fta, rostad svartrot, r�kt fl�sk, kantareller samt beurre blanc p� kr�ftfond', 0, 295);
INSERT INTO item (name, description, type, price) VALUES
('Hjort Blomk�l Betor N�tter', 'Hjortinnanl�r serveras med blomk�lscr�me, rostade betor, picklad blomk�l, rostade hasseln�tter samt Granny Smith-gel�', 2, 295);
INSERT INTO item (name, description, type, price) VALUES
('Jord�rtskocka Betor Svamp N�tter', 'R�kt jord�rtskockspur� samt bakade r�dbetor serveras med sm�rstekt svamp, friterat �pple, rostade hasseln�tter, �ppelgel� och linser kokta i portvin', 0, 295);

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
('Husets vin (r�tt)', '', 1, 79);
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