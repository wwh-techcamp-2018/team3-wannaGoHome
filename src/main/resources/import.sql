-- 우리 계정
-- $2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO is "password1" encoded by BcryptEncoder
INSERT INTO user (email, name, password, deleted) VALUES('junsulime@woowahan.com', 'junsulime', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO user (email, name, password, deleted) VALUES('jhyang12345@woowahan.com', 'jhyang', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO user (email, name, password, deleted) VALUES('songintae@woowahan.com', 'songintae', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO user (email, name, password, deleted) VALUES('kimyeon@woowahan.com', 'kimyeon', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO user (email, name, password, deleted) VALUES('boobby@woowahan.com', 'booby', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);

INSERT INTO team (profile_image, name, description, deleted) VALUES ('http://urlrul', 'WannaGoHome', 'WannaGoHome description', false);

INSERT INTO user_included_in_team(permission,team_id,user_id)VALUES('ADMIN',1,1);
INSERT INTO user_included_in_team(permission,team_id,user_id)VALUES('ADMIN',1,2);
INSERT INTO user_included_in_team(permission,team_id,user_id)VALUES('ADMIN',1,3);
INSERT INTO user_included_in_team(permission,team_id,user_id)VALUES('ADMIN',1,4);
INSERT INTO user_included_in_team(permission,team_id,user_id)VALUES('ADMIN',1,5);


INSERT INTO board(color,deleted,title,team_id) VALUES('SLIGHTLY_DESATURATED_CYAN',false,'3주차 Sprint',1);

INSERT INTO user_included_in_board(permission,board_id,user_id)VALUES('ADMIN',1,1);
INSERT INTO user_included_in_board(permission,board_id,user_id)VALUES('ADMIN',1,2);
INSERT INTO user_included_in_board(permission,board_id,user_id)VALUES('ADMIN',1,3);
INSERT INTO user_included_in_board(permission,board_id,user_id)VALUES('ADMIN',1,4);
INSERT INTO user_included_in_board(permission,board_id,user_id)VALUES('ADMIN',1,5);

-- 데모용 계정.
INSERT INTO user (email, name, password, deleted) VALUES('example1@woowahan.com', 'example1', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO user (email, name, password, deleted) VALUES('example2@woowahan.com', 'example2', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO user (email, name, password, deleted) VALUES('example3@woowahan.com', 'example3', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);
INSERT INTO user (email, name, password, deleted) VALUES('example4@woowahan.com', 'example4', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO',  false);


INSERT INTO team (profile_image, name, description, deleted) VALUES ('http://urlrul', '우아한테크캠프', '우아한형제들 테크캠프', false);

INSERT INTO user_included_in_team(permission,team_id,user_id)VALUES('ADMIN',2,6);
INSERT INTO user_included_in_team(permission,team_id,user_id)VALUES('ADMIN',2,7);
INSERT INTO user_included_in_team(permission,team_id,user_id)VALUES('ADMIN',2,8);
INSERT INTO user_included_in_team(permission,team_id,user_id)VALUES('ADMIN',2,9);

INSERT INTO board(color,deleted,title,team_id) VALUES('SLIGHTLY_DESATURATED_CYAN',false,'데모용 보드',2);

INSERT INTO user_included_in_board(permission,board_id,user_id)VALUES('ADMIN',2,6);
INSERT INTO user_included_in_board(permission,board_id,user_id)VALUES('ADMIN',2,7);
INSERT INTO user_included_in_board(permission,board_id,user_id)VALUES('ADMIN',2,8);
INSERT INTO user_included_in_board(permission,board_id,user_id)VALUES('ADMIN',2,9);