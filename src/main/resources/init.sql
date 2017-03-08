CREATE TABLE IF NOT EXISTS groups
(id bigint identity NOT NULL, number_of_group varchar(25) NOT NULL UNIQUE,
name_of_faculty varchar(50) NOT NULL,
CONSTRAINT groups_pk PRIMARY KEY(ID));

CREATE TABLE IF NOT EXISTS students
(id bigint identity NOT NULL, name varchar(25) NOT NULL, surname varchar(25) NOT NULL,
patronymic varchar(25), birthdate date NOT NULL, number_of_group varchar(25) NOT NULL,
CONSTRAINT students_pk PRIMARY KEY (ID),
CONSTRAINT students_fk FOREIGN KEY (number_of_group) REFERENCES groups(number_of_group)
                    ON DELETE NO ACTION
                    ON UPDATE CASCADE);

