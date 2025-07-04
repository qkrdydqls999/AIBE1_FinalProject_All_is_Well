package org.example.bookmarket.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.example.bookmarket.wishlist.dto.WishlistItem;
import org.example.bookmarket.wishlist.entity.Wishlist;
import org.example.bookmarket.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class WishlistServiceImpl implements WishlistService {

        private final WishlistRepository wishlistRepository;
        private final UsedBookRepository usedBookRepository;
        private final UserRepository userRepository;

        @Override
        @Transactional(readOnly = true)
        public List<WishlistItem> getItems(Long userId) {
            return wishlistRepository.findByUserId(userId).stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void addItem(Long userId, Long usedBookId) {
            if (wishlistRepository.existsByUserIdAndUsedBookId(userId, usedBookId)) {
                return;
            }
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("user not found"));
            UsedBook usedBook = usedBookRepository.findById(usedBookId)
                    .orElseThrow(() -> new IllegalArgumentException("used book not found"));
            Wishlist wishlist = Wishlist.builder()
                    .user(user)
                    .usedBook(usedBook)
                    .build();
            wishlistRepository.save(wishlist);
        }

        @Override
        @Transactional
        public void removeItem(Long userId, Long usedBookId) {
            wishlistRepository.findByUserIdAndUsedBookId(userId, usedBookId)
                    .ifPresent(wishlistRepository::delete);
        }

        private WishlistItem toDto(Wishlist w) {
            UsedBook ub = w.getUsedBook();
            String imageUrl = null;
            if (ub.getImages() != null && !ub.getImages().isEmpty()) {
                imageUrl = ub.getImages().get(0).getImageUrl();
            }
            return new WishlistItem(
                    ub.getId(),
                    ub.getBook().getTitle(),
                    imageUrl,
                    ub.getSellingPrice(),
                    ub.getSeller() != null ? ub.getSeller().getNickname() : null,
                    w.getCreatedAt()
            );
        }
    }