package com.gambarra.money.api.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Person.class)
public abstract class Person_ {

	public static volatile SingularAttribute<Person, Long> id;
	public static volatile SingularAttribute<Person, Boolean> active;
	public static volatile SingularAttribute<Person, Address> address;
	public static volatile ListAttribute<Person, Contact> contacts;
	public static volatile SingularAttribute<Person, String> name;

}

