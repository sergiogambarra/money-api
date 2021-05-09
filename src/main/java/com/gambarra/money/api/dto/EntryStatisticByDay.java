package com.gambarra.money.api.dto;

import com.gambarra.money.api.model.TypeEntry;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EntryStatisticByDay {

    private TypeEntry type;

    private LocalDate day;

    private BigDecimal total;

    public EntryStatisticByDay(TypeEntry type, LocalDate day, BigDecimal total) {
        this.type = type;
        this.day = day;
        this.total = total;
    }

    public TypeEntry getType() {
        return type;
    }

    public void setType(TypeEntry type) {
        this.type = type;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
