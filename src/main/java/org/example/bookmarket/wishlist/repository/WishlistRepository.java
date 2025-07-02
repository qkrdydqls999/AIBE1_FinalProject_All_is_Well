package org.example.bookmarket.wishlist.repository;

import org.example.bookmarket.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    boolean existsByUserIdAndUsedBookId(Long userId, Long usedBookId);
    Optional<Wishlist> findByUserIdAndUsedBookId(Long userId, Long usedBookId);
    List<Wishlist> findByUserId(Long userId);
}