CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL,
  `email` varchar(40) NOT NULL,
  `name` varchar(10) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

CREATE TABLE `team` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `description` varchar(255) NOT NULL,
  `name` varchar(20) NOT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_g2l9qqsoeuynt4r5ofdt1x2td` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `board` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `color` int(11) DEFAULT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `title` varchar(20) NOT NULL,
  `team_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhnthbc04iar1brn0b6jki0evy` (`team_id`),
  CONSTRAINT `FKhnthbc04iar1brn0b6jki0evy` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `user_included_in_board` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission` int(11) DEFAULT NULL,
  `board_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2ru1lbkdic6vcj99ekng8r86v` (`board_id`),
  KEY `FKi809sekf2trqsankwymjqdhr9` (`user_id`),
  CONSTRAINT `FK2ru1lbkdic6vcj99ekng8r86v` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`),
  CONSTRAINT `FKi809sekf2trqsankwymjqdhr9` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `user_included_in_team` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission` int(11) DEFAULT NULL,
  `team_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsiqi77b1dg35nvugar2yy7ntc` (`team_id`),
  KEY `FKbdlvs6m98ts2seg0r6ho7fl22` (`user_id`),
  CONSTRAINT `FKbdlvs6m98ts2seg0r6ho7fl22` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKsiqi77b1dg35nvugar2yy7ntc` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `recently_view_board` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `board_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbhvqqhd9kc9jpufnn1by5awmp` (`board_id`),
  KEY `FKkhuk1mir14laoblnxk66yftyt` (`user_id`),
  CONSTRAINT `FKbhvqqhd9kc9jpufnn1by5awmp` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`),
  CONSTRAINT `FKkhuk1mir14laoblnxk66yftyt` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

