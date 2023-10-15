 drop table if exists users cascade;
 create table users (
 user_id int primary key auto_increment,
 email varchar(100),
 login varchar(100),
 user_name varchar(100),
 birthday date
);

 drop table if exists friends cascade;
 create table friends (
 friends_id int primary key auto_increment,
 user_id int REFERENCES users (user_id) ON DELETE CASCADE,
 friend_id int REFERENCES users (user_id) ON DELETE CASCADE,
 status boolean
);

drop table if exists rating cascade;
 create table rating (
 rating_id int primary key auto_increment,
 rating_name varchar(50)
);

 drop table if exists films cascade;
 create table films (
 film_id int primary key auto_increment,
 film_name varchar(150),
 description varchar(255),
 release_date date,
 duration int,
 rating_id int REFERENCES rating (rating_id) ON DELETE CASCADE
);

 drop table if exists likes cascade;
 create table likes (
 like_id int primary key auto_increment,
 film_id int REFERENCES films (film_id) ON DELETE CASCADE,
 user_id int REFERENCES users (user_id) ON DELETE CASCADE
 );

drop table if exists genres cascade;
 create table genres (
 genre_id int primary key auto_increment,
 genre_name varchar(50)
);


 drop table if exists films_genres cascade;
 create table films_genres (
 film_genre_id int primary key auto_increment,
 film_id int REFERENCES films (film_id) ON DELETE CASCADE,
 genre_id int REFERENCES genres (genre_id) ON DELETE CASCADE
 );