-- ----------------------------- --
-- Database: db_web_app_contacts --
-- ----------------------------- --
-- DROP DATABASE db_web_app_contacts;
CREATE DATABASE db_web_app_contacts
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       CONNECTION LIMIT = -1;


-- --------------------------------------- --
-- CONNECT TO DATABASE db_web_app_contacts --
-- --------------------------------------- --
\connect db_web_app_contacts;


-- ------------------------- --
-- Table: public.tbl_contact --
-- ------------------------- --
-- DROP TABLE public.tbl_contact;
CREATE SEQUENCE public.tbl_contact_seq;
CREATE TABLE public.tbl_contact
(
       id bigint NOT NULL DEFAULT nextval('public.tbl_contact_seq')
     , first_name character varying(60) NOT NULL
     , last_name character varying(40) NOT NULL
     , birth_date DATE
     , description character varying(2000)
     , photo BYTEA
     , version INT NOT NULL DEFAULT 0
     , PRIMARY KEY (id)
)
WITH (
   OIDS=FALSE
);
ALTER SEQUENCE public.tbl_contact_seq OWNED BY public.tbl_contact.id;

ALTER TABLE public.tbl_contact
  OWNER TO postgres;


-- ------------------------------ --
-- INSERT INTO public.tbl_contact --
-- ------------------------------ --
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Chris', 'Schaefer', '1981-05-03');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Scott', 'Tiger', '1990-11-02');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('John', 'Smith', '1964-02-28');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Peter', 'Jackson', '1944-1-10');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Jacky', 'Chan', '1955-10-31');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Susan', 'Boyle', '1970-05-06');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Tinner', 'Turner', '1967-04-30');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Lotus', 'Notes', '1990-02-28');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Henry', 'Dickson', '1997-06-30');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Sam', 'Davis', '2001-01-31');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Max', 'Beckham', '2002-02-01');
insert into public.tbl_contact (first_name, last_name, birth_date) values ('Paul', 'Simon', '2002-02-28');