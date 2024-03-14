
CREATE TABLE _grade
(
    id                 UUID NOT NULL,
    created_by         VARCHAR(255),
    created_date       date,
    last_modified_date date,
    grade              INTEGER,
    user_id            UUID,
    CONSTRAINT pk__grade PRIMARY KEY (id)
);

CREATE TABLE _harvest_rate
(
    id                 UUID NOT NULL,
    created_by         VARCHAR(255),
    created_date       date,
    last_modified_date date,
    rate               INTEGER,
    date               date,
    product_id         UUID,
    CONSTRAINT pk__harvest_rate PRIMARY KEY (id)
);

CREATE TABLE _product
(
    id                 UUID NOT NULL,
    created_by         VARCHAR(255),
    created_date       date,
    last_modified_date date,
    name               VARCHAR(255),
    amount             INTEGER,
    measure            VARCHAR(255),
    CONSTRAINT pk__product PRIMARY KEY (id)
);

CREATE TABLE _report
(
    id                 UUID NOT NULL,
    created_by         VARCHAR(255),
    created_date       date,
    last_modified_date date,
    amount             INTEGER,
    product_id         UUID,
    user_id            UUID,
    CONSTRAINT pk__report PRIMARY KEY (id)
);

CREATE TABLE _user
(
    id                 UUID NOT NULL,
    created_by         VARCHAR(255),
    created_date       date,
    last_modified_date date,
    name               VARCHAR(255),
    surname            VARCHAR(255),
    patronymic         VARCHAR(255),
    email              VARCHAR(255),
    password           VARCHAR(255),
    role               VARCHAR(255),
    is_enabled         BOOLEAN,
    CONSTRAINT pk__user PRIMARY KEY (id)
);

ALTER TABLE _grade
    ADD CONSTRAINT FK__GRADE_ON_USER FOREIGN KEY (user_id) REFERENCES _user (id);

ALTER TABLE _harvest_rate
    ADD CONSTRAINT FK__HARVEST_RATE_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES _product (id);

ALTER TABLE _report
    ADD CONSTRAINT FK__REPORT_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES _product (id);

ALTER TABLE _report
    ADD CONSTRAINT FK__REPORT_ON_USER FOREIGN KEY (user_id) REFERENCES _user (id);