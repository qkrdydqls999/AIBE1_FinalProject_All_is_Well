package org.example.bookmarket.usedbook.repository;

import org.example.bookmarket.usedbook.entity.UsedBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository와 함께 UsedBookRepositoryCustom 인터페이스를 상속받도록 수정
public interface UsedBookRepository extends JpaRepository<UsedBook, Long>, UsedBookRepositoryCustom {
    List<UsedBook> findBySellerId(Long sellerId);
}