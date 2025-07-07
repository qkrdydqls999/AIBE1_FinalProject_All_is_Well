package org.example.bookmarket.category.service;

import org.example.bookmarket.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
}