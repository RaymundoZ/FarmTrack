--liquibase formatted sql

--changeset Raymundo:10
create extension if not exists pgcrypto;

--changeset Raymundo:11
insert into _user (id,
                   created_by,
                   created_date,
                   last_modified_date,
                   name,
                   surname,
                   patronymic,
                   email,
                   password,
                   role,
                   is_enabled)
values (gen_random_uuid(),
        'liquibase',
        current_date,
        current_date,
        '${liquibase.admin.name}',
        '${liquibase.admin.surname}',
        '${liquibase.admin.patronymic}',
        '${liquibase.admin.email}',
        crypt('${liquibase.admin.password}', gen_salt('bf')),
        'ADMIN',
        true);