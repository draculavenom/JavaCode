create table ROLES(
	ID bigint auto_increment PRIMARY KEY,
	NAME varchar(255)
);

create table USERS(
	ID bigint auto_increment PRIMARY KEY,
	USERNAME varchar(255),
	PASSWORD varchar(255),
	NAME varchar(255),
	LAST_NAME varchar(255),
	EMAIL varchar(255),
	ENABLED boolean,
	CREATION_TIME date,
	UPDATE_DATE date
);

create table USER_ROLES(
	USER_ID bigint,
	ROLE_ID bigint
);

create table AUTHORITIES(
	ID bigint auto_increment PRIMARY KEY,
	AUTHORITY varchar(255),
	USERNAME varchar(255)
);