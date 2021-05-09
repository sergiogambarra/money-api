package com.gambarra.money.api.dto;

import com.gambarra.money.api.model.Person;
import com.gambarra.money.api.model.TypeEntry;

import java.math.BigDecimal;

public class EntryStatisticPerson {

    private TypeEntry type;

    private Person person;

    private BigDecimal total;

    public EntryStatisticPerson(TypeEntry type, Person person, BigDecimal total) {
        this.person = person;
        this.type = type;
        this.total = total;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public TypeEntry getType() {
        return type;
    }

    public void setType(TypeEntry type) {
        this.type = type;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
