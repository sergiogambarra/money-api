package com.gambarra.money.api.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> password;
	public static volatile ListAttribute<User, Permission> permissoes;
	public static volatile SingularAttribute<User, Long> codigo;
	public static volatile SingularAttribute<User, String> nome;
	public static volatile SingularAttribute<User, String> email;

}

