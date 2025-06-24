package org.example.bookmarket.category.repository;

import org.example.bookmarket.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
