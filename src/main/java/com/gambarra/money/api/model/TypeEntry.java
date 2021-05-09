package com.gambarra.money.api.model;

public enum TypeEntry {

    RECEITA("Receita"),
    DESPESA("Despesa")

    ;

    private final String description;

    TypeEntry(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
