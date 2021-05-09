package com.gambarra.money.api.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Contact.class)
public abstract class Contact_ {

	public static volatile SingularAttribute<Contact, Long> id;
	public static volatile SingularAttribute<Contact, String> phone;
	public static volatile SingularAttribute<Contact, Person> person;
	public static volatile SingularAttribute<Contact, String> name;
	public static volatile SingularAttribute<Contact, String> email;

}

