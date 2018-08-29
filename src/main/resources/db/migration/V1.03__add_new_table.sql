CREATE TABLE attachment (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  link longtext NOT NULL,
  origin_file_name varchar(255) NOT NULL,
  card_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKpyjq6uiperx43dbsny1gjvxne (card_id),
  CONSTRAINT FKpyjq6uiperx43dbsny1gjvxne FOREIGN KEY (card_id) REFERENCES card (id)
) ENGINE=InnoDB

CREATE TABLE team_invite (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) DEFAULT NULL,
  team_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKi7icaouabbmwn2irtp6sur3vb (user_id),
  KEY FKp9wtc66edyxm3i0mshq108h77 (team_id),
  CONSTRAINT FKi7icaouabbmwn2irtp6sur3vb FOREIGN KEY (user_id) REFERENCES user (id),
  CONSTRAINT FKp9wtc66edyxm3i0mshq108h77 FOREIGN KEY (team_id) REFERENCES team (id)
) ENGINE=InnoDB


alter table user CHANGE profile profile longtext;
alter table team CHANGE profile_image profile varchar(255);
alter table team CHANGE profile profile longtext;
alter table task CHANGE title title varchar(30);