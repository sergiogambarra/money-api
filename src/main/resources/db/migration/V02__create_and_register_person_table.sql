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


INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('Sergio Gambarra da Silva', 'Av. Edgar Pires de Castro', '2520', 'Casa 66', 'Hipica', '91786299', 'Porto Alegre', 'RS', 1);
INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('João da Silva', 'Av. Cavalhada', '3000', 'Casa 12', 'Cavalhada', '99999999', 'Porto Alegre', 'RS', 1);
INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('Daniel Silveira da Silva', 'Estrada Costa Gama', '876', null, 'Aberta dos Morros', '99876345', 'Porto Alegre', 'RS', 1);
INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('Robson João de Matos', 'Av. Paulista', '54320', 'Sala 333', 'Morumbi', '33386299', 'São Paulo', 'SP', 1);
INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('Gustavo Fring', 'Av. Brasil', '520', null, 'Botafogo', '55586299', 'Rio de Janeiro', 'RJ', 0);
INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('Ana Luiza Souza', 'Av. Silveira', '3520', 'Casa 33', 'São João', '97786299', 'Porto Alegre', 'RS', 1);
INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('Ernesto Che Guevara', 'Rua Uruguai', '2233', 'Sala 66', 'Centro', '96796299', 'Porto Alegre', 'RS', 0);
INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('Daniela Santos', 'Rua Argentina', '4433', 'Sala 55', 'São Pedro', '96962655', 'Porto Alegre', 'RS', 0);
INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('Ana Carolina', 'Rua Jary', '6833', 'Ap 436', 'Higienopolis', '94356266', 'Porto Alegre', 'RS', 1);
INSERT INTO person (name, street, number, complement, neighborhood, zip, city, state, active) values ('Dorivaldo Junior', 'Rua Antonio Amaro', '233', null, 'Campo Novo', '96796299', 'Porto Alegre', 'RS', 1);