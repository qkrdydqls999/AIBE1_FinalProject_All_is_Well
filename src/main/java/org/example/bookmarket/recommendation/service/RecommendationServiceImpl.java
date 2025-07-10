package org.example.bookmarket.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.trade.repository.TradeRepository;
import org.example.bookmarket.usedbook.dto.UsedBookResponse;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.example.bookmarket.usedbook.service.UsedBookQueryService;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserCategoryRepository;
import org.example.bookmarket.user.repository.UserRepository;
import org.example.bookmarket.wishlist.entity.Wishlist;
import org.example.bookmarket.wishlist.repository.WishlistRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final UsedBookRepository usedBookRepository;
    private final TradeRepository tradeRepository;
    private final UsedBookQueryService usedBookQueryService;

    @Override
    @Transactional(readOnly = true)
    public List<UsedBookResponse> getPersonalizedRecommendations(Long userId, int limit) {
        // 1. 비로그인 사용자는 최신 도서를 추천하고 로직을 종료합니다.
        if (userId == null) {
            return usedBookQueryService.getLatestUsedBooks(limit);
        }

        // --- 2. 추천 제외 대상 ID 수집 ---
        // 사용자가 찜했거나, 이미 구매했거나, 판매 중인 책은 추천하지 않습니다.
        final Set<Long> excludedBookIds = new HashSet<>();

        // 찜 목록에 있는 책 ID 추가 (null 체크 추가)
        List<Wishlist> userWishlist = wishlistRepository.findByUserId(userId);
        userWishlist.forEach(w -> {
            if (w.getUsedBook() != null && w.getUsedBook().getId() != null) {
                excludedBookIds.add(w.getUsedBook().getId());
            }
        });

        // 구매한 책 ID 추가
        tradeRepository.findByBuyerId(userId).forEach(t -> {
            if (t.getUsedBook() != null && t.getUsedBook().getId() != null) {
                excludedBookIds.add(t.getUsedBook().getId());
            }
        });

        // SQL의 IN () 구문 오류 방지를 위해, 목록이 비어있으면 무의미한 ID(-1)를 추가합니다.
        if (excludedBookIds.isEmpty()) {
            excludedBookIds.add(-1L);
        }

        // --- 3. 추천 기반 카테고리 ID 수집 ---
        // 사용자의 관심 카테고리와, 찜한 책들의 카테고리를 모두 합쳐 추천 기준으로 삼습니다.
        final Set<Long> recommendationCategoryIds = new HashSet<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // 관심 카테고리 추가
        userCategoryRepository.findByUser(user).forEach(uc -> {
            if (uc.getCategory() != null && uc.getCategory().getId() != null) {
                recommendationCategoryIds.add(uc.getCategory().getId());
            }
        });

        // 찜한 책들의 카테고리 추가
        userWishlist.forEach(w -> {
            if (w.getUsedBook() != null && w.getUsedBook().getCategory() != null && w.getUsedBook().getCategory().getId() != null) {
                recommendationCategoryIds.add(w.getUsedBook().getCategory().getId());
            }
        });

        List<UsedBook> recommendedBooks = new ArrayList<>();

        // --- 4. 카테고리 기반 추천 실행 ---
        // 추천 기준 카테고리가 있을 경우에만 실행합니다.
        if (!recommendationCategoryIds.isEmpty()) {
            recommendedBooks = usedBookRepository.findRecommendationsByCategory(
                    recommendationCategoryIds, excludedBookIds, userId, PageRequest.of(0, limit));
        }

        // --- 5. 추천이 부족할 경우 최신 도서로 보충 ---
        // 위 추천만으로 4개를 채우지 못했다면, 최신 도서 중에서 중복되지 않게 추가 추천합니다.
        if (recommendedBooks.size() < limit) {
            int needed = limit - recommendedBooks.size();

            // 방금 추천된 책들도 제외 목록에 추가하여 중복을 방지합니다.
            recommendedBooks.forEach(book -> excludedBookIds.add(book.getId()));

            List<UsedBook> generalRecommendations = usedBookRepository.findGeneralRecommendations(
                    excludedBookIds, userId, PageRequest.of(0, needed));

            recommendedBooks.addAll(generalRecommendations);
        }

        return recommendedBooks.stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * UsedBook 엔티티를 홈 화면에 필요한 UsedBookResponse DTO로 변환합니다.
     */
    private UsedBookResponse toResponse(UsedBook ub) {
        // 중고책 이미지가 있으면 첫 번째 이미지를, 없으면 원본 새 책의 표지를 사용합니다.
        String coverImageUrl = (ub.getImages() != null && !ub.getImages().isEmpty())
                ? ub.getImages().get(0).getImageUrl()
                : (ub.getBook() != null ? ub.getBook().getCoverImageUrl() : "/images/default-book.png");

        return new UsedBookResponse(
                ub.getId(),
                ub.getBook().getId(),
                ub.getBook().getIsbn(),
                ub.getBook().getTitle(),
                ub.getBook().getAuthor(),
                ub.getBook().getPublisher(),
                ub.getBook().getPublicationYear(),
                ub.getConditionGrade(),
                ub.getDetailedCondition(),
                ub.getSellingPrice(),
                ub.getStatus(),
                ub.getCategory().getId(),
                ub.getSeller().getId(),
                ub.getSeller().getNickname(),
                ub.getSeller().getProfileImageUrl(),
                coverImageUrl
        );
    }
}