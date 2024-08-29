
CREATE DATABASE clients;

\c clients;

CREATE SEQUENCE person_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE person (
    id BIGINT NOT NULL DEFAULT nextval('person_id_seq'),
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(10),
    age INTEGER,
    address TEXT,
    phone VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE SEQUENCE client_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE client (
    id BIGINT NOT NULL DEFAULT nextval('client_id_seq'),
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    person_id INTEGER REFERENCES person(id),
    PRIMARY KEY (id)
);

CREATE DATABASE accounts;

\c accounts;

CREATE SEQUENCE account_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE account (
    id BIGINT NOT NULL DEFAULT nextval('account_id_seq'),
    account_number VARCHAR(50) NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    initial_balance DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    client_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);


CREATE SEQUENCE transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE transaction (
    id BIGINT NOT NULL DEFAULT nextval('transaction_id_seq'),
    date TIMESTAMP NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    account_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_account
        FOREIGN KEY (account_id)
        REFERENCES account(id)
        ON DELETE CASCADE
);
