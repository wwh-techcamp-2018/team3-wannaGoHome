-- $2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO is "password1" encoded by BcryptEncoder
INSERT INTO USER (email, name, password, deleted) VALUES('junsulime@woowahan.com', 'junsulime', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO USER (email, name, password, deleted) VALUES('jhyang12345@woowahan.com', 'jhyang', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO USER (email, name, password, deleted) VALUES('songintae@woowahan.com', 'songintae', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO USER (email, name, password, deleted) VALUES('kimyeon@woowahan.com', 'kimyeon', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);


INSERT INTO TEAM (profile_image, name, description, deleted) VALUES ('http://urlrul', 'JunsuLime', 'junsulime description', false);
INSERT INTO TEAM (profile_image, name, description, deleted) VALUES ('http://urlrul', '우아한 형제들', '우아한 형제들 description', false);
INSERT INTO TEAM (profile_image, name, description, deleted) VALUES ('http://urlrul', 'wannaGoHome', 'wannaGoHome description', false);


INSERT INTO board(id,color,deleted,title,team_id) VALUES(1,'SLIGHTLY_DESATURATED_CYAN',false,'one board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(2,'SLIGHTLY_DESATURATED_CYAN',false,'two board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(3,'VERY_DARK_BLUE',false,'three board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(4,'DARK_LIME_GREEN',false,'four board',1);

INSERT INTO recently_view_board(id,board_id,user_id)VALUES(1,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(2,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(3,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(4,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(5,1,2);

INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(1,'ADMIN',1,3);
INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(2,'ADMIN',2,3);
INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(3,'ADMIN',3,3);
INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(4,'ADMIN',4,3);
INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(5,'ADMIN',1,2);

INSERT INTO user_included_in_team(id,permission,team_id,user_id)VALUES(1,'ADMIN',1,3);
INSERT INTO user_included_in_team(id,permission,team_id,user_id)VALUES(2,'ADMIN',2,3);
INSERT INTO user_included_in_team(id,permission,team_id,user_id)VALUES(3,'ADMIN',1,1);
INSERT INTO user_included_in_team(id,permission,team_id,user_id)VALUES(4,'ADMIN',1,4);


INSERT INTO abstract_activity(activity_type,type,source_id,receiver_id,board_id,registered_date) VALUES('BoardActivity', 'BOARD_CREATE', 3, 3 , 1,'2018-08-23 14:20:47');
INSERT INTO abstract_activity(activity_type,type,source_id,receiver_id,board_id,registered_date) VALUES('BoardActivity', 'BOARD_CREATE', 3, 3 , 1,'2018-08-23 14:45:47');

INSERT INTO task(user_id, board_id, title, order_id) VALUES(1, 2, 'sampleT1', 0);
INSERT INTO task(user_id, board_id, title, order_id) VALUES(4, 2, 'sampleT2', 1);

INSERT INTO card(title, description, user_id, task_id, create_date, order_id) VALUES('ImportedCard1', 'Hell Wannagohome', 3, 1, '2018-08-23 14:20:47', 0);
INSERT INTO card(title, description, user_id, task_id, create_date, order_id) VALUES('ImportedCard2', 'Haha Wannagohome', 1, 1, '2018-08-23 14:27:45', 1);