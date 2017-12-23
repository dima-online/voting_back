
--
INSERT INTO "user" (id, iin, username, password, status, user_info_id, account_number, executive_iin) VALUES (2, '870101123456', 'user', 'cfcd208495d565ef66e7dff9f98764da', 'ACTIVE', NULL, NULL, NULL);

--
INSERT INTO theme (id, voting_id, status, scope, create_time, end_time, user_id) VALUES (1, 23, 'ACTIVE', 'PUBLIC', '2017-12-14 10:13:10.788', '2017-12-21 14:50:00', 1);
INSERT INTO theme (id, voting_id, status, scope, create_time, end_time, user_id) VALUES (2, 22, 'ACTIVE', 'PUBLIC', '2017-11-23 11:58:03.521', NULL, 1);
INSERT INTO theme (id, voting_id, status, scope, create_time, end_time, user_id) VALUES (3, 21, 'ACTIVE', 'PUBLIC', '2017-11-23 11:58:03.521', NULL, 1);

--
INSERT INTO theme_message (id, theme_id, title, message, locale) VALUES (3, 2, 'Активное голосование', 'Общее собрание 2', 'ru');
INSERT INTO theme_message (id, theme_id, title, message, locale) VALUES (1, 1, 'Прошедшее голосование', 'Общее собрание 1', 'ru');
INSERT INTO theme_message (id, theme_id, title, message, locale) VALUES (2, 1, 'Откен голосование', 'Толык жиналыс 1', 'kk');
INSERT INTO theme_message (id, theme_id, title, message, locale) VALUES (4, 2, 'Болып жаткан голосование', 'Толык жиналыс 2', 'kk');

--
INSERT INTO chat (id, user_id, theme_id, create_time) VALUES (1, 2, 1, '2017-12-14 10:13:10.788');
INSERT INTO chat (id, user_id, theme_id, create_time) VALUES (2, 2, 2, '2017-11-23 11:58:03.521');

--
INSERT INTO chat_message (id, chat_id, user_id, message, message_type, status, create_time, read_time) VALUES (1, 1, 1, 'Я админ', 'OUTGOING', 'NEW', '2017-12-14 10:13:10.788', NULL);
INSERT INTO chat_message (id, chat_id, user_id, message, message_type, status, create_time, read_time) VALUES (2, 2, 1, 'Я админ 2', 'OUTGOING', 'READ', '2017-11-28 11:58:03.521', '2017-11-23 11:58:03.521');
INSERT INTO chat_message (id, chat_id, user_id, message, message_type, status, create_time, read_time) VALUES (3, 2, 2, 'я пользователь', 'INCOMING', 'NEW', '2017-11-23 15:58:03.521', NULL);
