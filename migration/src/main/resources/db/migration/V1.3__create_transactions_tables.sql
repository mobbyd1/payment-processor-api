CREATE SCHEMA transaction;

CREATE TABLE transaction.OPERATION_TYPES (
      operation_type_id int8 UNIQUE,
      description varchar(255),
      charge_order int
);

CREATE TABLE transaction.TRANSACTIONS (
      transaction_id bigserial UNIQUE,
      account_id int8,
      operation_type_id int8 REFERENCES transaction.OPERATION_TYPES(operation_type_id),
      amount decimal,
      balance decimal,
      event_date date,
      due_date date
);

CREATE TABLE transaction.PAYMENTS_TRACKING (
      payment_tracking_id bigserial UNIQUE,
      credit_transaction_id int8 REFERENCES transaction.TRANSACTIONS(transaction_id),
      debit_transaction_id int8 REFERENCES transaction.TRANSACTIONS(transaction_id),
      amount DECIMAL
);