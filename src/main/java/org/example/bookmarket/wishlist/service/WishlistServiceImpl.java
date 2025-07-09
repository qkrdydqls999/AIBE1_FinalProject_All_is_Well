package org.example.bookmarket.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
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
        // 사용자가 존재하는지 확인하고, 없으면 USER_NOT_FOUND 예외를 던집니다.
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return wishlistRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addItem(Long userId, Long usedBookId) {
        if (wishlistRepository.existsByUserIdAndUsedBookId(userId, usedBookId)) {
            throw new CustomException(ErrorCode.WISHLIST_DUPLICATED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UsedBook usedBook = usedBookRepository.findById(usedBookId)
                .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));

        if (usedBook.getSeller() != null && usedBook.getSeller().getId().equals(userId)) {
            throw new CustomException(ErrorCode.WISHLIST_OWN_BOOK);
        }

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .usedBook(usedBook)
                .build();
        wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public void removeItem(Long userId, Long usedBookId) {
        //  삭제할 위시리스트 아이템을 조회하고, 없으면 USED_BOOK_NOT_FOUND를 사용합니다.

        Wishlist wishlistItem = wishlistRepository.findByUserIdAndUsedBookId(userId, usedBookId)
                .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));

        wishlistRepository.delete(wishlistItem);
    }

    private WishlistItem toDto(Wishlist w) {
        UsedBook ub = w.getUsedBook();

        String imageUrl = (ub.getImages() != null && !ub.getImages().isEmpty())
                ? ub.getImages().get(0).getImageUrl()
                : "/images/default-book.png";

        String sellerNickname = (ub.getSeller() != null)
                ? ub.getSeller().getNickname()
                : "탈퇴한 사용자";

        return new WishlistItem(
                ub.getId(),
                ub.getBook().getTitle(),
                imageUrl,
                ub.getSellingPrice(),
                sellerNickname,
                w.getCreatedAt()
        );
    }
}