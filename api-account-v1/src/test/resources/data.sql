
CREATE SEQUENCE IF NOT EXISTS account_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS account (
    id BIGINT NOT NULL DEFAULT nextval('account_id_seq'),
    account_number VARCHAR(50) NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    initial_balance DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    client_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);


CREATE SEQUENCE IF NOT EXISTS transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


CREATE TABLE IF NOT EXISTS transaction (
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
