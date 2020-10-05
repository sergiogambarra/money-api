package com.gambarra.money.api.repository;

import com.gambarra.money.api.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
