create user db_user_x with encrypted password 'asd123';

create database news_api with owner db_user_x;

grant all privileges on news_api to db_user_x;