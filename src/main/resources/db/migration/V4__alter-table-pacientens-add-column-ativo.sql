ALTER TABLE pacientes ADD COLUMN ativo TINYINT(1) NOT NULL DEFAULT 1;

UPDATE pacientes SET ativo = 1;
