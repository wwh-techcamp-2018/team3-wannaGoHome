-- $2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO is "password1" encoded by BcryptEncoder
INSERT INTO USER (email, name, password, deleted) VALUES('junsulime@woowahan.com', 'junsulime', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO USER (email, name, password, deleted) VALUES('jhyang@woowahan.com', 'jhyang', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO USER (email, name, password, deleted) VALUES('songintae@woowahan.com', 'songintae', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO USER (email, name, password, deleted) VALUES('kimyeon@woowahan.com', 'kimyeon', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);


INSERT INTO TEAM (profile_image, name, description, deleted) VALUES ('http://urlrul', 'JunsuLime', 'junsulime description', false);
INSERT INTO TEAM (profile_image, name, description, deleted) VALUES ('http://urlrul', '우아한 형제들', '우아한 형제들 description', false);
INSERT INTO TEAM (profile_image, name, description, deleted) VALUES ('http://urlrul', 'wannaGoHome', 'wannaGoHome description', false);


INSERT INTO board(id,color,deleted,title,team_id) VALUES(1,0,false,'one board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(2,0,false,'two board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(3,0,false,'three board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(4,0,false,'four board',1);

INSERT INTO recently_view_board(id,board_id,user_id)VALUES(1,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(2,2,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(3,3,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(4,4,3);


INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(1,0,1,3);
INSERT INTO user_included_in_team(id,permission,team_id,user_id)VALUES(1,0,1,3);
