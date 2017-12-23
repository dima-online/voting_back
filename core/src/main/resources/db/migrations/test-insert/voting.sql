---------------------------insert user
INSERT INTO core.user(id,iin, username, password, status) values(1,'880101123456','bsb.user','cfcd208495d565ef66e7dff9f98764da','ACTIVE');
--------------------------insert orgs
INSERT INTO core.organisation (id, organisation_name, organisation_num, external_id, status, total_share_count, executive_name, logo, result_link) VALUES (1, 'BSB Org', '111111111111', '123', 'CAN_VOTE', 200000, NULL, '', NULL);
INSERT INTO core.organisation (id, organisation_name, organisation_num, external_id, status, total_share_count, executive_name, logo, result_link) VALUES (4, 'АКЦИОНЕРНОЕ ОБЩЕСТВО "КРИСТАЛЛ МЕНЕДЖМЕНТ"', '841113300369', NULL, 'CAN_VOTE', 100000, 'МУКАШЕВ СЕРИК', NULL, NULL);
INSERT INTO core.organisation (id, organisation_name, organisation_num, external_id, status, total_share_count, executive_name, logo, result_link) VALUES (5, 'АКЦИОНЕРНОЕ ОБЩЕСТВО "КАЗАЗОТ"', '051140001409', NULL, 'CAN_VOTE', 120000, 'АЛИЖАНОВ МАРАТ АБДУХАМИТОВИЧ', NULL, NULL);

------------------------insert votings

INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (7, 'SIMPLE', 'Внеочередное общее собрание акционеров', '2017-06-08 10:13:13.437', '2017-06-08 10:19:55.098', '2017-06-08 15:55:00', '2017-06-08 15:55:21.101', 'CLOSED', '2017-05-12 00:00:00', 1, 4, 1, true, NULL);
INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (8, 'SIMPLE', 'Годовое общее собрание акционеров', '2017-06-08 10:51:39.481', '2017-06-08 13:00:00', '2017-06-09 13:00:00', '2017-06-09 13:00:51.213', 'CLOSED', '2017-06-08 00:00:00', 1, 5, 3, false, NULL);
INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (9, 'SIMPLE', 'Внеочередное общее собрание акционеров', '2017-06-09 11:52:35.036', '2017-06-09 13:00:00', '2017-06-14 13:00:00', '2017-06-09 14:51:41.955', 'CLOSED', '2017-06-06 00:00:00', 1, 5, 2, false, NULL);
INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (10, 'SIMPLE', 'Внеочередное общее собрание акционеров', '2017-06-09 14:52:26.546', '2017-06-09 15:30:00', '2017-06-14 15:00:00', '2017-06-14 15:00:02.732', 'CLOSED', '2017-06-09 14:52:26.546', 1, 5, NULL, false, NULL);
INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (11, 'SIMPLE', 'Внеочередное общее собрание акционеров', '2017-06-09 15:16:30.094', '2017-06-09 16:00:00', '2017-06-09 15:35:00', '2017-06-09 15:35:02.739', 'CLOSED', '2017-06-09 15:31:35.228', 1, 5, NULL, false, NULL);
INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (12, 'SIMPLE', 'Внеочередное общее собрание акционеров', '2017-06-15 12:22:35.917', '2017-06-15 14:00:00', '2017-06-22 14:00:00', '2017-06-22 14:00:02.746', 'CLOSED', '2017-06-15 00:00:00', 1, 5, 5, true, NULL);
INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (13, 'SIMPLE', 'ry', '2017-07-01 10:05:44.401', '2017-07-15 10:05:00', '2017-07-26 14:05:00', '2017-07-26 14:05:12.228', 'CLOSED', '2017-07-05 12:30:54.113', 1, 4, NULL, false, NULL);
INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (21, 'SIMPLE', 'test mobile', '2017-11-28 11:53:43.604', '2017-11-28 11:55:00', '2017-12-22 11:55:00', NULL, 'NEW', '2017-11-28 11:53:43.604', 1, 5, 6, NULL, NULL);
INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (22, 'SIMPLE', 'test mobile2', '2017-11-28 11:58:03.521', '2017-11-28 11:59:54.179', '2017-12-28 13:45:00', NULL, 'STARTED', '2017-11-28 11:58:03.521', 1, 4, 1, NULL, NULL);
INSERT INTO core.voting (id, voting_type, subject, date_create, date_begin, date_end, date_close, status, last_changed, who_changed, organisation_id, last_reestr_id, kvoroom, description) VALUES (23, 'SIMPLE', 'asdf', '2017-12-13 10:13:10.788', '2017-12-13 10:30:00', '2017-12-21 14:50:00', NULL, 'NEW', '2017-12-13 10:13:10.788', 1, 5, NULL, NULL, NULL);

-------------------------------------insert voting_messages
INSERT INTO core.voting_message (id, description, locale, subject, voting_id) VALUES (1, 'asd', 'ru', 'asd', 7);
INSERT INTO core.voting_message (id, description, locale, subject, voting_id) VALUES (2, 'qwe', 'kk', 'qwe', 7);
INSERT INTO core.voting_message (id, description, locale, subject, voting_id) VALUES (3, 'zxc', 'en', 'zxc', 7);
INSERT INTO core.voting_message (id, description, locale, subject, voting_id) VALUES (4, 'русский', 'ru', 'на русском', 23);
INSERT INTO core.voting_message (id, description, locale, subject, voting_id) VALUES (5, 'казахский', 'kk', 'на казахском', 23);
INSERT INTO core.voting_message (id, description, locale, subject, voting_id) VALUES (6, 'английский', 'en', 'на английском', 23);
INSERT INTO core.voting_message (id, description, locale, subject, voting_id) VALUES (7, 'на русском', 'ru', 'какое-то описание', 22);

-- insert questions

INSERT INTO core.question (id, question, num, voting_id, question_type, max_count, priv_can_vote) VALUES (12, 'Об увеличении количественного состава Совета директоров Общества и доизбрании членов Совета директоров Общества, определение размера и условий выплаты вознаграждений и компенсации расходов всем членам Совета директоров за исполнение ими своих обязанностей', 1, 7, 'ORDINARY', 1, NULL);
INSERT INTO core.question (id, question, num, voting_id, question_type, max_count, priv_can_vote) VALUES (13, 'Об утверждении и внесении изменений в Устав Общества', 2, 7, 'ORDINARY', 1, NULL);
INSERT INTO core.question (id, question, num, voting_id, question_type, max_count, priv_can_vote) VALUES (14, 'Об утверждении кодекса корпоративного управления Общества', 3, 7, 'ORDINARY', 1, NULL);
INSERT INTO core.question (id, question, num, voting_id, question_type, max_count, priv_can_vote) VALUES (15, 'О принятии решения о включении выпуска акций в официальный список  АО «Казахстанская фондовая биржа» (KASE) второй категории', 4, 7, 'ORDINARY', 1, NULL);
INSERT INTO core.question (id, question, num, decision, voting_id, question_type, max_count, priv_can_vote) VALUES (40, 'test 1', 1, NULL, 22, 'ORDINARY', 1, NULL);
INSERT INTO core.question (id, question, num, decision, voting_id, question_type, max_count, priv_can_vote) VALUES (41, 'test 2', 2, NULL, 22, 'CUMULATIVE', 1, NULL);
INSERT INTO core.question (id, question, num, decision, voting_id, question_type, max_count, priv_can_vote) VALUES (42, '12354', 1, NULL, 23, 'ORDINARY', 1, NULL);

-- insert question messages

INSERT INTO core.question_message (id, locale, text, title, question_id) VALUES (1, 'ru', 'Об увеличении количественного состава Совета директоров Общества и доизбрании членов Совета директоров Общества, определение размера и условий выплаты вознаграждений и компенсации расходов всем членам Совета директоров за исполнение ими своих обязанностей', 'вопрос 1', 12);
INSERT INTO core.question_message (id, locale, text, title, question_id) VALUES (2, 'ru', 'Об утверждении и внесении изменений в Устав Общества', 'вопрос 2', 13);
INSERT INTO core.question_message (id, locale, text, title, question_id) VALUES (3, 'ru', 'Об утверждении кодекса корпоративного управления Общества', 'вопрос 3', 14);
INSERT INTO core.question_message (id, locale, text, title, question_id) VALUES (4, 'ru', 'О принятии решения о включении выпуска акций в официальный список  АО «Казахстанская фондовая биржа» (KASE) второй категории', 'ВОПРОС 4', 15);
INSERT INTO core.question_message (id, locale, text, title, question_id) VALUES (5, 'ru', 'test 1', 'test1', 40);
INSERT INTO core.question_message (id, locale, text, title, question_id) VALUES (6, 'ru', 'test 2', 'test2', 41);
INSERT INTO core.question_message (id, locale, text, title, question_id) VALUES (7, 'ru', 'qwe1', 'qwe 1', 42);



-- insert answers

INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (1, 'Yes', 12, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (2, 'NO', 12, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (3, 'ignore', 12, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (5, 'Против', 13, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (4, 'За ', 13, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (6, 'Воздержался', 13, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (7, 'За ', 14, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (8, 'Против', 14, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (9, 'Воздержался', 14, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (10, 'ЗА', 40, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (11, 'Против', 40, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (12, 'Воздержался', 40, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (13, '1', 41, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (14, '2', 41, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (15, '3', 41, NULL);
INSERT INTO core.answer (id, answer, question_id, final_score) VALUES (16, '4', 41, NULL);

-- insert answer messages

INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (1, 'ru', 'за', 1);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (2, 'ru', 'против', 2);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (3, 'ru', 'воздержался', 3);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (4, 'ru', 'за', 4);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (5, 'ru', 'против', 5);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (6, 'ru', 'воздержался', 6);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (7, 'ru', 'за', 7);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (8, 'ru', 'против', 8);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (9, 'ru', 'воздержался', 9);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (10, 'ru', 'за', 10);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (11, 'ru', 'против', 11);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (12, 'ru', 'воздержался', 12);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (13, 'ru', '1', 13);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (14, 'ru', '2', 14);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (15, 'ru', '3', 15);
INSERT INTO core.answer_message (id, locale, text, answer_id) VALUES (16, 'ru', '4', 16);


-- insert files
INSERT INTO core.files (id, file_name, file_path, voting_id, description, type) VALUES (1, 'File 1', '1492778565710.docx', 7, 'some description', 'DOCUMENT');
INSERT INTO core.files (id, file_name, file_path, voting_id, description, type) VALUES (2, 'Video 1', 'https://youtu.be/C5CzN3hEE3o?list=RDMMC5CzN3hEE3o', 7, 'это видео', 'VIDEO');







