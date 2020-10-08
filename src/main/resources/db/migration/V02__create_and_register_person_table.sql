CREATE TABLE person (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    street VARCHAR(50),
    number VARCHAR(50),
    complement VARCHAR(50),
    neighborhood VARCHAR(50),
    zip VARCHAR(50),
    city VARCHAR(50),
    state VARCHAR(50),
    active BIT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('João Silva', 'Rua do Abacaxi', '10', null, 'Brasil', '38.400-121', 'Uberlândia', 'MG', true);
INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('Maria Rita', 'Rua do Sabiá', '110', 'Apto 101', 'Colina', '11.400-121', 'Ribeirão Preto', 'SP', true);
INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('Pedro Santos', 'Rua da Bateria', '23', null, 'Morumbi', '54.212-121', 'Goiânia', 'GO', true);
INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('Ricardo Pereira', 'Rua do Motorista', '123', 'Apto 302', 'Aparecida', '38.400-12', 'Salvador', 'BA', true);
INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('Josué Mariano', 'Av Rio Branco', '321', null, 'Jardins', '56.400-121', 'Natal', 'RN', true);
INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('Pedro Barbosa', 'Av Brasil', '100', null, 'Tubalina', '77.400-121', 'Porto Alegre', 'RS', true);
INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('Henrique Medeiros', 'Rua do Sapo', '1120', 'Apto 201', 'Centro', '12.400-121', 'Rio de Janeiro', 'RJ', true);
INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('Carlos Santana', 'Rua da Manga', '433', null, 'Centro', '31.400-121', 'Belo Horizonte', 'MG', true);
INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('Leonardo Oliveira', 'Rua do Músico', '566', null, 'Segismundo Pereira', '38.400-00', 'Uberlândia', 'MG', true);
INSERT INTO pessoa (name, street, number, complement, neighborhood, zip, city, state, active) values ('Isabela Martins', 'Rua da Terra', '1233', 'Apto 10', 'Vigilato', '99.400-121', 'Manaus', 'AM', true);