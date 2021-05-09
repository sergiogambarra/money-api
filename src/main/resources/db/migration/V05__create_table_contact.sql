CREATE TABLE contact (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	id_person BIGINT(20) NOT NULL,
	name VARCHAR(50) NOT NULL,
	email VARCHAR(100) NOT NULL,
	phone VARCHAR(20) NOT NULL,
  FOREIGN KEY (id_person) REFERENCES person(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into contact (id, id_person, name, email, phone) values (1, 3, 'Marcos Henrique', 'marcos@gambarra.com.br', '00 0000-0000');