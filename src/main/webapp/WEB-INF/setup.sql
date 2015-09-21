
-- test comment

create table people (
    id int auto_increment primary key,
    name varchar(50) not null,
    age int not null
);


insert into people (name, age) values ('Bosse', 57);
insert into people (name, age) values ('Eva', 39);
insert into people (name, age) values ('Albin', 14);
insert into people (name, age) values ('Karin', 60);