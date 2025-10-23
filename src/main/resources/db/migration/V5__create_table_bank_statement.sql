CREATE TABLE IF NOT EXISTS bank_statement (
    id UUID PRIMARY KEY,
    charge_id UUID NOT NULL UNIQUE,
    type_payment TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,

    CONSTRAINT fk_charge FOREIGN KEY (charge_id) REFERENCES charges(id)
);