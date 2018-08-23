CREATE TABLE team (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 deleted bit(1) NOT NULL DEFAULT b'0',
 description varchar(255) NOT NULL,
 name varchar(20) NOT NULL,
 profile_image varchar(255) DEFAULT NULL,
 PRIMARY KEY (id),
 UNIQUE KEY UK_g2l9qqsoeuynt4r5ofdt1x2td (name)
) ENGINE=InnoDB;
​
CREATE TABLE user (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 deleted bit(1) NOT NULL,
 email varchar(40) NOT NULL,
 name varchar(10) NOT NULL,
 password varchar(255) NOT NULL,
 PRIMARY KEY (id),
 UNIQUE KEY UK_ob8kqyqqgmefl0aco34akdtpe (email)
) ENGINE=InnoDB ;
​
CREATE TABLE board (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 color varchar(255) DEFAULT NULL,
 deleted bit(1) NOT NULL DEFAULT b'0',
 title varchar(20) NOT NULL,
 team_id bigint(20) DEFAULT NULL,
 PRIMARY KEY (id),
 KEY FKhnthbc04iar1brn0b6jki0evy (team_id),
 CONSTRAINT FKhnthbc04iar1brn0b6jki0evy FOREIGN KEY (team_id) REFERENCES team (id)
) ENGINE=InnoDB ;
​
CREATE TABLE task (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 deleted bit(1) NOT NULL DEFAULT b'0',
 order_id int(11) NOT NULL DEFAULT '0',
 title varchar(30) NOT NULL,
 user_id bigint(20) DEFAULT NULL,
 board_id bigint(20) NOT NULL,
 PRIMARY KEY (id),
 KEY FK2hsytmxysatfvt0p1992cw449 (user_id),
 KEY FKrar3pm9ixqub1nilws0l8l22w (board_id),
 CONSTRAINT FK2hsytmxysatfvt0p1992cw449 FOREIGN KEY (user_id) REFERENCES user (id),
 CONSTRAINT FKrar3pm9ixqub1nilws0l8l22w FOREIGN KEY (board_id) REFERENCES board (id)
) ENGINE=InnoDB ;
​
CREATE TABLE card (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 create_date datetime DEFAULT NULL,
 deleted bit(1) NOT NULL DEFAULT b'0',
 description varchar(255) DEFAULT NULL,
 end_date datetime DEFAULT NULL,
 order_id int(11) DEFAULT NULL,
 title varchar(20) NOT NULL,
 user_id bigint(20) DEFAULT NULL,
 task_id bigint(20) DEFAULT NULL,
 PRIMARY KEY (id),
 KEY FKl4gbym62l738id056y12rt6q6 (user_id),
 KEY FK82oa5703e66owvymoxj7lrkw0 (task_id),
 CONSTRAINT FK82oa5703e66owvymoxj7lrkw0 FOREIGN KEY (task_id) REFERENCES task (id),
 CONSTRAINT FKl4gbym62l738id056y12rt6q6 FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB ;
​
CREATE TABLE comment (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 contents varchar(255) NOT NULL,
 deleted bit(1) NOT NULL DEFAULT b'0',
 user_id bigint(20) DEFAULT NULL,
 card_id bigint(20) DEFAULT NULL,
 PRIMARY KEY (id),
 KEY FK8kcum44fvpupyw6f5baccx25c (user_id),
 KEY FKqgv5aujiclf0iihwxf4gmkf18 (card_id),
 CONSTRAINT FK8kcum44fvpupyw6f5baccx25c FOREIGN KEY (user_id) REFERENCES user (id),
 CONSTRAINT FKqgv5aujiclf0iihwxf4gmkf18 FOREIGN KEY (card_id) REFERENCES card (id)
) ENGINE=InnoDB ;
​
CREATE TABLE label (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 color varchar(255) DEFAULT NULL,
 title varchar(20) DEFAULT NULL,
 PRIMARY KEY (id)
) ENGINE=InnoDB ;
​
CREATE TABLE abstract_activity (
 activity_type varchar(31) NOT NULL,
 id bigint(20) NOT NULL AUTO_INCREMENT,
 registered_date datetime DEFAULT NULL,
 type varchar(255) NOT NULL,
 permission varchar(255) DEFAULT NULL,
 receiver_id bigint(20) DEFAULT NULL,
 source_id bigint(20) DEFAULT NULL,
 target_id bigint(20) DEFAULT NULL,
 team_id bigint(20) DEFAULT NULL,
 card_id bigint(20) DEFAULT NULL,
 board_id bigint(20) DEFAULT NULL,
 task_id bigint(20) DEFAULT NULL,
 PRIMARY KEY (id),
 KEY FK6s6mb46mnaj53pp4r9qfhumvh (receiver_id),
 KEY FKbby661rl9liyh2sdy6r52h013 (source_id),
 KEY FK1nto3bpib1tk4rcpxwwllvdxb (target_id),
 KEY FKgvkx8qae837sqeo312wp8grr8 (team_id),
 KEY FKogajsinsk298xewgmrr3v4v5k (card_id),
 KEY FKd17bex86dn7a5451sgjpx79fe (board_id),
 KEY FKhkmkr7k01b3pd6k13e7h2yc2y (task_id),
 CONSTRAINT FK1nto3bpib1tk4rcpxwwllvdxb FOREIGN KEY (target_id) REFERENCES user (id),
 CONSTRAINT FK6s6mb46mnaj53pp4r9qfhumvh FOREIGN KEY (receiver_id) REFERENCES user (id),
 CONSTRAINT FKbby661rl9liyh2sdy6r52h013 FOREIGN KEY (source_id) REFERENCES user (id),
 CONSTRAINT FKd17bex86dn7a5451sgjpx79fe FOREIGN KEY (board_id) REFERENCES board (id),
 CONSTRAINT FKgvkx8qae837sqeo312wp8grr8 FOREIGN KEY (team_id) REFERENCES team (id),
 CONSTRAINT FKhkmkr7k01b3pd6k13e7h2yc2y FOREIGN KEY (task_id) REFERENCES task (id),
 CONSTRAINT FKogajsinsk298xewgmrr3v4v5k FOREIGN KEY (card_id) REFERENCES card (id)
) ENGINE=InnoDB;
​
​
CREATE TABLE card_assignee (
 card_id bigint(20) NOT NULL,
 user_id bigint(20) NOT NULL,
 KEY FKp5rqt5ahs1qw9t7pflsucbw3c (user_id),
 KEY FKfiyf2dtvqeswp85waqxx89fkf (card_id),
 CONSTRAINT FKfiyf2dtvqeswp85waqxx89fkf FOREIGN KEY (card_id) REFERENCES card (id),
 CONSTRAINT FKp5rqt5ahs1qw9t7pflsucbw3c FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB ;
​
CREATE TABLE card_label (
 card_id bigint(20) NOT NULL,
 label_id bigint(20) NOT NULL,
 KEY FKmlw7eklhh1w7r627k334hdcoa (label_id),
 KEY FK6hd640wdc120cw7py2mxj6wb7 (card_id),
 CONSTRAINT FK6hd640wdc120cw7py2mxj6wb7 FOREIGN KEY (card_id) REFERENCES card (id),
 CONSTRAINT FKmlw7eklhh1w7r627k334hdcoa FOREIGN KEY (label_id) REFERENCES label (id)
) ENGINE=InnoDB ;
​
CREATE TABLE chat_message (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 message_created datetime DEFAULT NULL,
 message_order bigint(20) NOT NULL DEFAULT '0',
 text longtext NOT NULL,
 user_id bigint(20) DEFAULT NULL,
 board_id bigint(20) NOT NULL,
 PRIMARY KEY (id),
 KEY FKf7tbywofv1iojpxc1kw8c3bx7 (user_id),
 KEY FK4jf1ltnj1fapvu8ovkm61evus (board_id),
 CONSTRAINT FK4jf1ltnj1fapvu8ovkm61evus FOREIGN KEY (board_id) REFERENCES board (id),
 CONSTRAINT FKf7tbywofv1iojpxc1kw8c3bx7 FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB ;
​
​
CREATE TABLE recently_view_board (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 board_id bigint(20) DEFAULT NULL,
 user_id bigint(20) DEFAULT NULL,
 PRIMARY KEY (id),
 KEY FKbhvqqhd9kc9jpufnn1by5awmp (board_id),
 KEY FKkhuk1mir14laoblnxk66yftyt (user_id),
 CONSTRAINT FKbhvqqhd9kc9jpufnn1by5awmp FOREIGN KEY (board_id) REFERENCES board (id),
 CONSTRAINT FKkhuk1mir14laoblnxk66yftyt FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB ;
​
​
CREATE TABLE user_included_in_board (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 permission varchar(255) DEFAULT NULL,
 board_id bigint(20) DEFAULT NULL,
 user_id bigint(20) DEFAULT NULL,
 PRIMARY KEY (id),
 KEY FK2ru1lbkdic6vcj99ekng8r86v (board_id),
 KEY FKi809sekf2trqsankwymjqdhr9 (user_id),
 CONSTRAINT FK2ru1lbkdic6vcj99ekng8r86v FOREIGN KEY (board_id) REFERENCES board (id),
 CONSTRAINT FKi809sekf2trqsankwymjqdhr9 FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB ;
​
CREATE TABLE user_included_in_team (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 permission varchar(255) DEFAULT NULL,
 team_id bigint(20) DEFAULT NULL,
 user_id bigint(20) DEFAULT NULL,
 PRIMARY KEY (id),
 KEY FKsiqi77b1dg35nvugar2yy7ntc (team_id),
 KEY FKbdlvs6m98ts2seg0r6ho7fl22 (user_id),
 CONSTRAINT FKbdlvs6m98ts2seg0r6ho7fl22 FOREIGN KEY (user_id) REFERENCES user (id),
 CONSTRAINT FKsiqi77b1dg35nvugar2yy7ntc FOREIGN KEY (team_id) REFERENCES team (id)
) ENGINE=InnoDB ;
​
​
-- $2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO is "password1" encoded by BcryptEncoder
INSERT INTO user (email, name, password, deleted) VALUES('junsulime@woowahan.com', 'junsulime', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO', false);
INSERT INTO user (email, name, password, deleted) VALUES('jhyang12345@woowahan.com', 'jhyang', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO', false);
INSERT INTO user (email, name, password, deleted) VALUES('songintae@woowahan.com', 'songintae', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO', false);
INSERT INTO user (email, name, password, deleted) VALUES('kimyeon@woowahan.com', 'kimyeon', '$2a$10$yKTRTpg8iaZBSGe.kIQYzuAF0A2nrUQ2YAMwz50mqllpo9fqpk0XO', false);
​
​
INSERT INTO team (profile_image, name, description, deleted) VALUES ('http://urlrul', 'JunsuLime', 'junsulime description', false);
INSERT INTO team (profile_image, name, description, deleted) VALUES ('http://urlrul', '우아한 형제들', '우아한 형제들 description', false);
INSERT INTO team (profile_image, name, description, deleted) VALUES ('http://urlrul', 'wannaGoHome', 'wannaGoHome description', false);
​
​
INSERT INTO board(id,color,deleted,title,team_id) VALUES(1,'SLIGHTLY_DESATURATED_CYAN',false,'one board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(2,'SLIGHTLY_DESATURATED_CYAN',false,'two board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(3,'VERY_DARK_BLUE',false,'three board',1);
INSERT INTO board(id,color,deleted,title,team_id) VALUES(4,'DARK_LIME_GREEN',false,'four board',1);
​
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(1,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(2,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(3,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(4,1,3);
INSERT INTO recently_view_board(id,board_id,user_id)VALUES(5,1,2);
​
INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(1,'ADMIN',1,3);
INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(2,'ADMIN',2,3);
INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(3,'ADMIN',3,3);
INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(4,'ADMIN',4,3);
INSERT INTO user_included_in_board(id,permission,board_id,user_id)VALUES(5,'ADMIN',1,2);
​
INSERT INTO user_included_in_team(id,permission,team_id,user_id)VALUES(1,'ADMIN',1,3);
INSERT INTO user_included_in_team(id,permission,team_id,user_id)VALUES(2,'ADMIN',2,3);
INSERT INTO user_included_in_team(id,permission,team_id,user_id)VALUES(3,'ADMIN',1,1);
INSERT INTO user_included_in_team(id,permission,team_id,user_id)VALUES(4,'ADMIN',1,4);
​
INSERT INTO abstract_activity(activity_type,type,source_id,receiver_id,board_id,registered_date) VALUES('BoardActivity', 'BOARD_CREATE', 3, 3 , 1,'2018-08-23 14:45:47');