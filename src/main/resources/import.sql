-- $2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO is "password1" encoded by BcryptEncoder
INSERT INTO USER (email, name, password, deleted) VALUES('junsulime@woowahan.com', 'junsulime', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO USER (email, name, password, deleted) VALUES('jhyang@woowahan.com', 'jhyang', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO USER (email, name, password, deleted) VALUES('songintae@woowahan.com', 'songintae', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO USER (email, name, password, deleted) VALUES('kimyeon@woowahan.com', 'kimyeon', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);


INSERT INTO team(id,deleted,description,name,profile_image) VALUES(1,false,'wannagohome 화이팅','wannagohome','default.png');

INSERT INTO board(id,color,deleted,title,team_id) VALUES(1,1,false,'one board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(2,1,false,'two board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(3,1,false,'three board',1);

INSERT INTO recently_view_board(id,board_id,user_id)VALUES(1,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(2,2,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(3,3,3);


INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(1,1,1,3);
