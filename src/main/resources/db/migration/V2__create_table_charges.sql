CREATE TABLE IF NOT EXISTS charges (
    id UUID PRIMARY KEY,
    originator_cpf VARCHAR(11) NOT NULL,
    recipient_cpf VARCHAR(11) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(8) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,

    CONSTRAINT fk_originator FOREIGN KEY (originator_cpf) REFERENCES users(cpf),
    CONSTRAINT fk_recipient FOREIGN KEY (recipient_cpf) REFERENCES users(cpf)
);