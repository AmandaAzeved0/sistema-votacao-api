-- Criação da tabela pauta
CREATE TABLE pauta (
                       id SERIAL PRIMARY KEY,
                       titulo VARCHAR(255) NOT NULL,
                       descricao TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criação da tabela sessao_votacao
CREATE TABLE sessao_votacao (
                                id SERIAL PRIMARY KEY,
                                pauta_id INTEGER NOT NULL,
                                data_inicio TIMESTAMP NOT NULL,
                                duracao_em_minutos INTEGER DEFAULT 1,
                                status VARCHAR(20) NOT NULL DEFAULT 'ABERTA',
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (pauta_id) REFERENCES pauta(id)
);

-- Índice em pauta_id na tabela sessao_votacao
CREATE INDEX idx_sessao_pauta_id ON sessao_votacao (pauta_id);

-- Criação da tabela associado
CREATE TABLE associado (
                           id SERIAL PRIMARY KEY,
                           nome VARCHAR(255) NOT NULL,
                           cpf VARCHAR(11) UNIQUE NOT NULL,
                           apto_para_votar BOOLEAN DEFAULT TRUE
);

-- Criação da tabela voto
CREATE TABLE voto (
                      id SERIAL PRIMARY KEY,
                      sessao_id INTEGER NOT NULL,
                      associado_id INTEGER NOT NULL,
                      voto VARCHAR(3) CHECK (voto IN ('SIM', 'NAO')),
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (sessao_id) REFERENCES sessao_votacao(id),
                      FOREIGN KEY (associado_id) REFERENCES associado(id),
                      UNIQUE (sessao_id, associado_id) -- Restrição de voto único por sessão
);

-- Índices adicionais para otimização de consultas
CREATE INDEX idx_voto_sessao_id ON voto (sessao_id);
CREATE INDEX idx_voto_associado_id ON voto (associado_id);
