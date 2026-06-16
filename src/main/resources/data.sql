-- Dados de demonstracao do sistema Dentix.
-- Atencao: este script limpa os dados das tabelas principais antes de inserir a massa de exemplo.
-- Senha padrao dos usuarios: 123456

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM financeiro_lancamentos;
DELETE FROM notificacoes;
DELETE FROM solicitacoes_alteracao;
DELETE FROM consultas;
DELETE FROM dentista_especialidade;
DELETE FROM dentistas;
DELETE FROM especialidades;
DELETE FROM pacientes;
DELETE FROM dados_clinica;
DELETE FROM usuarios;

ALTER TABLE financeiro_lancamentos AUTO_INCREMENT = 1;
ALTER TABLE notificacoes AUTO_INCREMENT = 1;
ALTER TABLE solicitacoes_alteracao AUTO_INCREMENT = 1;
ALTER TABLE consultas AUTO_INCREMENT = 1;
ALTER TABLE dentistas AUTO_INCREMENT = 1;
ALTER TABLE especialidades AUTO_INCREMENT = 1;
ALTER TABLE pacientes AUTO_INCREMENT = 1;
ALTER TABLE dados_clinica AUTO_INCREMENT = 1;
ALTER TABLE usuarios AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO usuarios (id, nome, cpf, email, senha, perfil, ativo, data_criacao, ultimo_login, foto_perfil, two_factor_ativo, two_factor_secret)
VALUES
    (1, 'Lorenzo Carneiro Andreoli', '04905882044', 'lorenzo.andreoli@dentix.com.br', '$2a$10$62HmIpBscIPAJXEnTCHuiecssI2VC1eLh6dSr5IlG/8EEQ.bYC37K', 'ADMIN', true, '2026-05-01 08:00:00', null, null, false, null),
    (2, 'Marina Alves Ribeiro', '07674066304', 'marina.ribeiro@dentix.com.br', '$2a$10$62HmIpBscIPAJXEnTCHuiecssI2VC1eLh6dSr5IlG/8EEQ.bYC37K', 'ADMIN', true, '2026-05-01 08:10:00', null, null, false, null),
    (3, 'Dr Joao Alves Camargo', '18935671096', 'joao.camargo@dentix.com.br', '$2a$10$62HmIpBscIPAJXEnTCHuiecssI2VC1eLh6dSr5IlG/8EEQ.bYC37K', 'DENTISTA', true, '2026-05-01 08:20:00', null, null, false, null),
    (4, 'Dra Helena Castro Lopes', '22107445125', 'helena.lopes@dentix.com.br', '$2a$10$62HmIpBscIPAJXEnTCHuiecssI2VC1eLh6dSr5IlG/8EEQ.bYC37K', 'DENTISTA', true, '2026-05-01 08:30:00', null, null, false, null),
    (5, 'Dr Miguel Gomes de Souza', '24133514696', 'miguel.souza@dentix.com.br', '$2a$10$62HmIpBscIPAJXEnTCHuiecssI2VC1eLh6dSr5IlG/8EEQ.bYC37K', 'DENTISTA', true, '2026-05-01 08:40:00', null, null, false, null);

INSERT INTO especialidades (id, nome)
VALUES
    (1, 'Ortodontia'),
    (2, 'Endodontia'),
    (3, 'Implantodontia'),
    (4, 'Protese Dentaria'),
    (5, 'Clinica Geral');

INSERT INTO dentistas (id, nome, cpf, email, cro, ativo, data_criacao, usuario_id)
VALUES
    (1, 'Dr Joao Alves Camargo', '18935671096', 'joao.camargo@dentix.com.br', 'CRO/PR 15482', true, '2026-05-01 09:00:00', 3),
    (2, 'Dra Helena Castro Lopes', '22107445125', 'helena.lopes@dentix.com.br', 'CRO/PR 73528', true, '2026-05-01 09:10:00', 4),
    (3, 'Dr Miguel Gomes de Souza', '24133514696', 'miguel.souza@dentix.com.br', 'CRO/PR 84163', true, '2026-05-01 09:20:00', 5);

INSERT INTO dentista_especialidade (dentista_id, especialidade_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 5),
    (2, 1),
    (2, 4),
    (3, 3),
    (3, 5);

INSERT INTO pacientes (id, nome, email, cpf, telefone, ativo, data_criacao)
VALUES
    (1, 'Muriel Cordeiro Maia', 'murielcmaia@gmail.com', '01248945000', '4191213455', true, '2026-05-17 16:16:00'),
    (2, 'Joao da Silva', 'joaosilva@gmail.com', '26049204098', '41987442356', true, '2026-05-18 09:00:00'),
    (3, 'Carla Rocha', 'carlarocha@gmail.com', '60797474056', '41988553309', true, '2026-05-19 10:30:00'),
    (4, 'Rafael Lima', 'rafael.lima@gmail.com', '76178159019', '41977889900', false, '2026-05-20 14:20:00'),
    (5, 'Ana Paula Martins', 'ana.martins@gmail.com', '28390165012', '41991234567', true, '2026-05-21 08:45:00'),
    (6, 'Bruno Henrique Costa', 'bruno.costa@gmail.com', '78124093055', '41992345678', true, '2026-05-21 10:15:00'),
    (7, 'Camila Fernanda Souza', 'camila.souza@gmail.com', '36481972003', '41993456789', true, '2026-05-22 09:10:00'),
    (8, 'Daniel Ribeiro Alves', 'daniel.alves@gmail.com', '53027684091', '41994567890', true, '2026-05-22 13:40:00'),
    (9, 'Eduarda Lima Ferreira', 'eduarda.ferreira@gmail.com', '69412038047', '41995678901', true, '2026-05-23 11:05:00'),
    (10, 'Felipe Augusto Pereira', 'felipe.pereira@gmail.com', '91730528064', '41996789012', true, '2026-05-23 15:25:00'),
    (11, 'Gabriela Nunes Oliveira', 'gabriela.oliveira@gmail.com', '43869012075', '41997890123', true, '2026-05-24 08:30:00'),
    (12, 'Henrique Moreira Santos', 'henrique.santos@gmail.com', '80571943028', '41998901234', true, '2026-05-24 16:45:00'),
    (13, 'Isabela Cristina Rocha', 'isabela.rocha@gmail.com', '14958273066', '41999012345', true, '2026-05-25 09:55:00'),
    (14, 'Lucas Gabriel Almeida', 'lucas.almeida@gmail.com', '57294381020', '41990123456', true, '2026-05-25 14:35:00'),
    (15, 'Mariana Azevedo Castro', 'mariana.castro@gmail.com', '02648395017', '41990234567', true, '2026-05-26 10:20:00'),
    (16, 'Patricia Gomes Batista', 'patricia.batista@gmail.com', '31587046092', '41990345678', false, '2026-05-26 12:10:00'),
    (17, 'Rodrigo Teixeira Moura', 'rodrigo.moura@gmail.com', '74039168054', '41990456789', true, '2026-05-27 08:05:00'),
    (18, 'Sabrina Lopes Cardoso', 'sabrina.cardoso@gmail.com', '98126075033', '41990567890', false, '2026-05-27 17:30:00');

INSERT INTO dados_clinica (id, nome_fantasia, razao_social, cnpj, email, telefone, endereco, cidade, estado, cep, responsavel_tecnico, cro_responsavel, horario_funcionamento, data_criacao, data_atualizacao)
VALUES
    (1, 'Dentix', 'Dentix Clinica Odontologica LTDA', '12.345.678/0001-90', 'contato@dentix.com', '(41) 3333-4444', 'Rua das Flores, 1200 - Centro', 'Curitiba', 'PR', '80010-000', 'Dr Joao Alves Camargo', 'CRO/PR 15482', 'Segunda a sexta, 08:00 as 18:00', '2026-05-01 09:30:00', '2026-05-01 09:30:00');

INSERT INTO consultas (id, id_paciente, id_dentista, id_especialidade, id_usuario, descricao, motivo_cancelamento, data_inicio, data_fim, data_registro, status)
VALUES
    (1, 1, 1, 1, 1, 'Consulta inicial de ortodontia', null, '2026-06-17 09:30:00', '2026-06-17 10:30:00', '2026-06-10 14:00:00', 'AGENDADA'),
    (2, 2, 1, 2, 1, 'Tratamento de canal', null, '2026-06-17 12:02:00', '2026-06-17 13:02:00', '2026-06-10 15:10:00', 'AGENDADA'),
    (3, 3, 3, 3, 1, 'Avaliacao para implante', null, '2026-06-17 09:00:00', '2026-06-17 10:00:00', '2026-06-11 08:30:00', 'AGENDADA'),
    (4, 1, 1, 5, 1, 'Consulta odontologica', null, '2026-06-11 09:00:00', '2026-06-11 10:00:00', '2026-06-01 11:00:00', 'FINALIZADA'),
    (5, 3, 2, 4, 1, 'Protese dentaria', 'Paciente solicitou remarcacao', '2026-06-08 14:00:00', '2026-06-08 15:00:00', '2026-06-02 13:20:00', 'CANCELADA');

INSERT INTO financeiro_lancamentos (id, id_consulta, id_paciente, id_dentista, descricao, valor, tipo, status, data_vencimento, data_pagamento, data_criacao)
VALUES
    (1, 1, 1, 1, 'Consulta inicial de ortodontia', 350.00, 'RECEITA', 'PENDENTE', '2026-06-17', null, '2026-06-10 14:00:10'),
    (2, 2, 2, 1, 'Tratamento de canal', 450.00, 'RECEITA', 'PENDENTE', '2026-06-17', null, '2026-06-10 15:10:10'),
    (3, 3, 3, 3, 'Avaliacao para implante', 120.00, 'RECEITA', 'PENDENTE', '2026-06-17', null, '2026-06-11 08:30:10'),
    (4, 4, 1, 1, 'Consulta odontologica', 350.00, 'RECEITA', 'PAGO', '2026-06-11', '2026-06-11 10:05:00', '2026-06-01 11:00:10'),
    (5, 5, 3, 2, 'Protese dentaria', 350.00, 'RECEITA', 'CANCELADO', '2026-06-08', null, '2026-06-02 13:20:10');

INSERT INTO notificacoes (id, destinatario_id, titulo, mensagem, link, lida, data_criacao, data_leitura)
VALUES
    (1, 1, 'Solicitacao de alteracao recebida', 'Dr Joao Alves Camargo solicitou alteracao de dados cadastrais.', '/solicitacoes-alteracao', false, '2026-06-12 09:30:00', null),
    (2, 4, 'Solicitacao respondida', 'Sua solicitacao de alteracao foi respondida pelo administrador.', '/perfil', true, '2026-06-12 11:00:00', '2026-06-12 11:20:00');

INSERT INTO solicitacoes_alteracao (id, solicitante_id, assunto, descricao, status, resposta, respondida_por_id, data_criacao, data_resposta)
VALUES
    (1, 3, 'Atualizacao de telefone', 'Solicito alterar meu telefone cadastrado para (41) 98888-7777.', 'PENDENTE', null, null, '2026-06-12 09:30:00', null),
    (2, 4, 'Correcao de nome profissional', 'Solicito ajustar a grafia do meu nome profissional nos dados do sistema.', 'RESPONDIDA', 'Dados revisados. A alteracao foi registrada no cadastro.', 1, '2026-06-11 10:00:00', '2026-06-11 15:00:00');

