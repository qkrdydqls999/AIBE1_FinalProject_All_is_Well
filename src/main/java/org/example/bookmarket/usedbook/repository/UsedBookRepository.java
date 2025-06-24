package org.example.bookmarket.usedbook.repository;

import org.example.bookmarket.usedbook.entity.UsedBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsedBookRepository extends JpaRepository<UsedBook, Long> {
}
