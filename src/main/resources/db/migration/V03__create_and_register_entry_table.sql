CREATE TABLE entry (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(50) NOT NULL,
    due_date DATE NOT NULL,
    pay_date DATE,
    value DECIMAL(10,2) NOT NULL,
    note VARCHAR(100),
    type VARCHAR(20) NOT NULL,
    category_id BIGINT(20) NOT NULL,
    person_id BIGINT(20) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (person_id) REFERENCES person(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `moneyapi`.`entry` (`description`, `due_date`, `pay_date`, `value`, `note`, `type`, `category_id`, `person_id`) VALUES ('Salário mensal', '2017-06-06', null, '6500', 'Distribuição de lucros', 'RECEITA', '1', '1');
INSERT INTO `moneyapi`.`entry` (`description`, `due_date`, `pay_date`, `value`, `note`, `type`, `category_id`, `person_id`) VALUES ('Bahamas', '2017-02-10', '2017-02-10', '100.32', null, 'DESPESA', '2', '2');
INSERT INTO `moneyapi`.`entry` (`description`, `due_date`, `pay_date`, `value`, `note`, `type`, `category_id`, `person_id`) VALUES ('Top Club', '2017-06-10', null, '120', null, 'RECEITA', '3', '3');
INSERT INTO `moneyapi`.`entry` (`description`, `due_date`, `pay_date`, `value`, `note`, `type`, `category_id`, `person_id`) VALUES ('CEMIG', '2017-02-10', '2017-02-10', '110.44', 'Geração', 'RECEITA', '3', '4');
INSERT INTO `moneyapi`.`entry` (`description`, `due_date`, `pay_date`, `value`, `note`, `type`, `category_id`, `person_id`) VALUES ('DMAE', '2017-06-10', null, '200.30', null, 'DESPESA', '3', '5');
INSERT INTO `moneyapi`.`entry` (`description`, `due_date`, `pay_date`, `value`, `note`, `type`, `category_id`, `person_id`) VALUES ('Extra', '2017-03-10', '2017-03-10', '1010.32', null, 'RECEITA', '4', '6');
INSERT INTO `moneyapi`.`entry` (`description`, `due_date`, `pay_date`, `value`, `note`, `type`, `category_id`, `person_id`) VALUES ('Bahamas', '2017-06-10', null, '500', null, 'RECEITA', '1', '7');
INSERT INTO `moneyapi`.`entry` (`description`, `due_date`, `pay_date`, `value`, `note`, `type`, `category_id`, `person_id`) VALUES ('Top Club', '2017-03-10', '2017-03-10', '400.32', null, 'DESPESA', '4', '8');
INSERT INTO `moneyapi`.`entry` (`description`, `due_date`, `pay_date`, `value`, `note`, `type`, `category_id`, `person_id`) VALUES ('Despachante', '2017-06-10', null, '123.64', 'Multas', 'DESPESA', '3', '9');