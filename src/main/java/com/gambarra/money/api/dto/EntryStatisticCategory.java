package com.gambarra.money.api.dto;

import com.gambarra.money.api.model.Category;

import java.math.BigDecimal;

public class EntryStatisticCategory {

    private Category category;

    private BigDecimal total;

    public EntryStatisticCategory(Category category, BigDecimal total) {
        this.category = category;
        this.total = total;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
