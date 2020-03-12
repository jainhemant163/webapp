
DROP database users_database;
CREATE database users_database;
use users_database;

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

insert into users(email,password,firstName,lastName,account_created,account_updated,id) values("22@qq.com","$2y$10$sTEjAha0zxFNV4EMRSuBze1l1kAhLQrENe/IC/ZG4zfjWJwkorUfq","YYJ","XXJ","2018-01-01","2018-01-01",4);    

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
    paymentStatus          varchar(100) null,
    attachment             json null

);

create table File
(
   file_name                  varchar(256) not null,
    id                       varchar(256) not null,
    url                  varchar(1024) null,
    upload_date                     varchar(100) null,
    hashedcode                     varchar(256) null
    
);
