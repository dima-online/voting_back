/************ Update: Schemas ***************/

/* Add Schema: core */
CREATE SCHEMA IF NOT EXISTS core;
DROP SCHEMA core CASCADE;
CREATE SCHEMA core;



/************ Update: Tables ***************/

/******************** Add Table: core.answer ************************/

/* Build Table Structure */
CREATE TABLE core.answer
(
  id BIGINT NOT NULL,
  answer VARCHAR(2000) NOT NULL,
  question_id BIGINT NOT NULL,
  final_score VARCHAR(2000) NULL
) WITHOUT OIDS;

/* Add Primary Key */
ALTER TABLE core.answer ADD CONSTRAINT pkanswer
PRIMARY KEY (id);

COMMENT ON COLUMN core.answer.final_score IS 'Финалные результат по этому ответу';

COMMENT ON TABLE core.answer IS 'Ответы';


/******************** Add Table: core.message ************************/

/* Build Table Structure */
CREATE TABLE core.message
(
  id BIGINT NOT NULL,
  organisation_id BIGINT NULL,
  subject VARCHAR(2000) NULL,
  "body" VARCHAR(20000) NULL,
  parent_id BIGINT NULL,
  date_create TIMESTAMP NOT NULL,
  user_id BIGINT NOT NULL,
  date_read TIMESTAMP NULL
) WITHOUT OIDS;

/* Add Primary Key */
ALTER TABLE core.message ADD CONSTRAINT pkmessage
PRIMARY KEY (id);

/* Add Comments */
COMMENT ON COLUMN core.message.subject IS 'Тема сообщения';

COMMENT ON COLUMN core.message."body" IS 'Тело сообщения';

COMMENT ON COLUMN core.message.parent_id IS 'ID предыдущего сообщения';

COMMENT ON COLUMN core.message.date_create IS 'Дата сообщения';

COMMENT ON COLUMN core.message.date_read IS 'Дата прочтения';

COMMENT ON TABLE core.message IS 'Сообщения';


/******************** Add Table: core.organisation ************************/

/* Build Table Structure */
CREATE TABLE core.organisation
(
  id BIGINT NOT NULL,
  organisation_name VARCHAR(200) NULL,
  organisation_num VARCHAR(50) NULL,
  external_id VARCHAR(50) NULL,
  status VARCHAR(20) DEFAULT 'NEW' NULL
) WITHOUT OIDS;

/* Add Primary Key */
ALTER TABLE core.organisation ADD CONSTRAINT pkorganisation
PRIMARY KEY (id);

/* Add Comments */
COMMENT ON COLUMN core.organisation.id IS 'ID';

COMMENT ON COLUMN core.organisation.organisation_name IS 'Наименование организации';

COMMENT ON COLUMN core.organisation.organisation_num IS 'Регистрационный номер организации';

COMMENT ON COLUMN core.organisation.external_id IS 'ID организации во внешней системе';

COMMENT ON COLUMN core.organisation.status IS 'Статусы организации NEW APPrOVED CAN_VOTE CANNOT_VOTE';

COMMENT ON TABLE core.organisation IS 'Акционерное общество';


/******************** Add Table: core.question ************************/

/* Build Table Structure */
CREATE TABLE core.question
(
  id BIGINT NOT NULL,
  question VARCHAR(2000) NOT NULL,
  num INTEGER NULL,
  decision VARCHAR(2000) NULL,
  voting_id BIGINT NOT NULL,
  question_type VARCHAR(20) NOT NULL
) WITHOUT OIDS;

/* Add Primary Key */
ALTER TABLE core.question ADD CONSTRAINT pkquestion
PRIMARY KEY (id);

COMMENT ON COLUMN core.question.num IS 'Номер вопроса';

COMMENT ON COLUMN core.question.decision IS 'Итоговое решение по вопросу';

COMMENT ON COLUMN core.question.question_type IS 'Тип вопроса ORDINARY CUMULATIVE ';

COMMENT ON TABLE core.question IS 'Вопросы';


/******************** Add Table: core.user ************************/

/* Build Table Structure */
CREATE TABLE core.user
(
  id BIGINT NOT NULL,
  iin VARCHAR(255) NULL,
  username VARCHAR(255) NULL,
  password VARCHAR(255) NULL,
  status VARCHAR(255) NULL,
  user_info_id BIGINT NULL
) WITHOUT OIDS;

/* Add Primary Key */
ALTER TABLE core.user ADD CONSTRAINT user_pkey
PRIMARY KEY (id);


/******************** Add Table: core.user_info ************************/

/* Build Table Structure */
CREATE TABLE core.user_info
(
  id BIGINT NOT NULL,
  last_name VARCHAR(200) NULL,
  first_name VARCHAR(200) NULL,
  middle_name VARCHAR(200) NULL,
  idn VARCHAR(12) NULL,
  phone VARCHAR(255) NULL,
  email VARCHAR(255) NULL,
  status VARCHAR(255) NULL
) WITHOUT OIDS;

/* Add Primary Key */
ALTER TABLE core.user_info ADD CONSTRAINT user_info_pkey
PRIMARY KEY (id);


/******************** Add Table: core.user_roles ************************/

/* Build Table Structure */
CREATE TABLE core.user_roles
(
  id BIGINT NOT NULL,
  role_code VARCHAR(20) NOT NULL,
  org_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL
) WITHOUT OIDS;

/* Add Primary Key */
ALTER TABLE core.user_roles ADD CONSTRAINT pkuser_roles
PRIMARY KEY (id);

/* Add Comments */
COMMENT ON COLUMN core.user_roles.role_code IS 'Роль ADMIN OPERATOR USER';

COMMENT ON COLUMN core.user_roles.org_id IS 'ID организации';


/******************** Add Table: core.voter ************************/

/* Build Table Structure */
CREATE TABLE core.voter
(
  id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  voting_id BIGINT NOT NULL,
  date_adding TIMESTAMP NULL,
  share_count INTEGER DEFAULT 0 NOT NULL,
  signature VARCHAR(200) NULL,
  public_key VARCHAR(2000) NULL
) WITHOUT OIDS;

/* Add Primary Key */
ALTER TABLE core.voter ADD CONSTRAINT pkvoter
PRIMARY KEY (id);

/* Add Comments */
COMMENT ON COLUMN core.voter.date_adding IS 'Дата добавления в список голосования';

COMMENT ON COLUMN core.voter.share_count IS 'Количество акции';

COMMENT ON COLUMN core.voter.signature IS 'Код проверки подлинности';

COMMENT ON COLUMN core.voter.public_key IS 'Публичный ключ проверки';

COMMENT ON TABLE core.voter IS 'Участник голосования';


/******************** Add Table: core.voting ************************/

/* Build Table Structure */
CREATE TABLE core.voting
(
  id BIGINT NOT NULL,
  voting_type VARCHAR(20) NOT NULL,
  subject VARCHAR(200) NOT NULL,
  date_create TIMESTAMP NOT NULL,
  date_begin TIMESTAMP NULL,
  date_end TIMESTAMP NULL,
  date_close TIMESTAMP NULL,
  status VARCHAR(50) DEFAULT 'CREATED' NOT NULL,
  last_changed TIMESTAMP NULL,
  who_changed BIGINT NOT NULL,
  organisation_id BIGINT NOT NULL
) WITHOUT OIDS;

/* Add Primary Key */
ALTER TABLE core.voting ADD CONSTRAINT pkvoting
PRIMARY KEY (id);

/* Add Comments */
COMMENT ON COLUMN core.voting.voting_type IS 'Тип голосования SIMPLE MIXED';

COMMENT ON COLUMN core.voting.subject IS 'Тема голосования';

COMMENT ON COLUMN core.voting.date_create IS 'Дата создания голосования';

COMMENT ON COLUMN core.voting.date_begin IS 'Дата начала голосования';

COMMENT ON COLUMN core.voting.date_end IS 'Дата окончания голосования';

COMMENT ON COLUMN core.voting.date_close IS 'Дата закрытия голосования';

COMMENT ON COLUMN core.voting.status IS 'Статус голосования CREATED STARTED ENDED CLOSED CANCELED';

COMMENT ON COLUMN core.voting.last_changed IS 'Дата последнего изменения';

COMMENT ON COLUMN core.voting.who_changed IS 'Пользователь сделавший последнее изменение';

COMMENT ON TABLE core.voting IS 'Голосование';





/************ Add Foreign Keys ***************/

/* Add Foreign Key: fk_answer_question */
ALTER TABLE core.answer ADD CONSTRAINT fk_answer_question
FOREIGN KEY (question_id) REFERENCES core.question (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_message_message */
ALTER TABLE core.message ADD CONSTRAINT fk_message_message
FOREIGN KEY (parent_id) REFERENCES core.message (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_message_organisation */
ALTER TABLE core.message ADD CONSTRAINT fk_message_organisation
FOREIGN KEY (organisation_id) REFERENCES core.organisation (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_message_user */
ALTER TABLE core.message ADD CONSTRAINT fk_message_user
FOREIGN KEY (user_id) REFERENCES core.user (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_question_voting */
ALTER TABLE core.question ADD CONSTRAINT fk_question_voting
FOREIGN KEY (voting_id) REFERENCES core.voting (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: user_user_info_fk */
ALTER TABLE core.user ADD CONSTRAINT user_user_info_fk
FOREIGN KEY (user_info_id) REFERENCES core.user_info (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_user_roles_organisation */
ALTER TABLE core.user_roles ADD CONSTRAINT fk_user_roles_organisation
FOREIGN KEY (org_id) REFERENCES core.organisation (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_user_roles_user */
ALTER TABLE core.user_roles ADD CONSTRAINT fk_user_roles_user
FOREIGN KEY (user_id) REFERENCES core.user (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_voter_user */
ALTER TABLE core.voter ADD CONSTRAINT fk_voter_user
FOREIGN KEY (user_id) REFERENCES core.user (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_voter_voting */
ALTER TABLE core.voter ADD CONSTRAINT fk_voter_voting
FOREIGN KEY (voting_id) REFERENCES core.voting (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_voting_organisation */
ALTER TABLE core.voting ADD CONSTRAINT fk_voting_organisation
FOREIGN KEY (organisation_id) REFERENCES core.organisation (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Add Foreign Key: fk_voting_user */
ALTER TABLE core.voting ADD CONSTRAINT fk_voting_user
FOREIGN KEY (who_changed) REFERENCES core.user (id)
ON UPDATE NO ACTION ON DELETE NO ACTION;