drop table category if exists;

create table category (category_no integer not null AUTO_INCREMENT, category_name varchar(255), parent_no integer, depth integer not null, primary key (category_no));

drop table product if exists;

create table product (product_no bigint not null AUTO_INCREMENT, brand_name varchar(255), product_name varchar(255), product_price decimal(19,2), category_no integer, primary key (product_no));


