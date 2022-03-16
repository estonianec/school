alter table student
    add constraint age_constraint check (age > 16);

alter table student
    ADD CONSTRAINT name_unique UNIQUE (name);

alter table student
    alter name set not null;

ALTER TABLE faculty
    ADD CONSTRAINT login_pass_unique UNIQUE (color, name);

ALTER TABLE student
    alter age set DEFAULT 20;
