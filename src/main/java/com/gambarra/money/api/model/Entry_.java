package com.gambarra.money.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Entry.class)
public abstract class Entry_ {

	public static volatile SingularAttribute<Entry, Long> id;
	public static volatile SingularAttribute<Entry, String> note;
	public static volatile SingularAttribute<Entry, TypeEntry> type;
	public static volatile SingularAttribute<Entry, LocalDate> payDate;
	public static volatile SingularAttribute<Entry, Person> person;
	public static volatile SingularAttribute<Entry, LocalDate> dueDate;
	public static volatile SingularAttribute<Entry, Category> category;
	public static volatile SingularAttribute<Entry, BigDecimal> value;
	public static volatile SingularAttribute<Entry, String> description;

}

