DROP TABLE IF EXISTS `Persons`;

CREATE TABLE Persons (
    PersonID int,
    LastName varchar(255),
    FirstName varchar(255),
    Address varchar(255),
    City varchar(255)
);

INSERT INTO Persons
VALUES (
	1,
	"Foo",
	"Baz",
	"123 Bar Street",
	"FooBazBar City"
);

DROP TABLE IF EXISTS `users`;
create table users
(
    id              varchar(256) not null,
    email           varchar(128)  not null,
    password        varchar(256) not null,
    firstName       varchar(100)     null,
    lastName        varchar(100)     null,
    account_created varchar(100)     null,
    account_updated varchar(100)     null,
    constraint users_pk
        unique (id)
);

insert into users(email,password,firstName,lastName,account_created,account_updated,id) values("11@qq.com",
"$2y$10$sTEjAha0zxFNV4EMRSuBze1l1kAhLQrENe/ICZG4zfjWJwkorUfq","HJ","MJ","2018-01-01","2018-01-01",3);

DROP TABLE IF EXISTS `Bill`;

create table Bill
(
    id                    varchar(256) not null primary key unique ,
    created_ts            varchar(100) null,
    updated_ts            varchar(100) null,
    owner_id             varchar(256) null,
    vendor                 varchar(100) null,
    bill_date                varchar(100) null,
    due_date                varchar(100) null,
    amount_due             double null,
    categories              TEXT,
    paymentStatus          varchar(100) null

);


insert into Bill(id,created_ts,updated_ts,owner_id,vendor,bill_date,due_date,amount_due,categories,paymentStatus)
values(5,"2010-01-01","2010-01-01",123456,"JAck","2010-01-01","2010-01-10",500,"A,B","due");




