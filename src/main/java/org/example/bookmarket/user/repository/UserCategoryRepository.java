package org.example.bookmarket.user.repository;

import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.entity.UserCategory;
import org.example.bookmarket.user.entity.UserCategoryId;
import org.example.bookmarket.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, UserCategoryId> {
    List<UserCategory> findByUser(User user);
    void deleteByUser(User user);
}