CREATE SCHEMA account;

CREATE TABLE account.ACCOUNTS (
      id bigserial unique,
      available_credit_limit decimal,
      available_withdraw_limit decimal
)