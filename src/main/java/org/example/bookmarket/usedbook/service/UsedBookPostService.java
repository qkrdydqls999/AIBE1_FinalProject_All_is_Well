package org.example.bookmarket.usedbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmarket.ai.dto.PriceSuggestResponse;
import org.example.bookmarket.ai.service.AiService;
import org.example.bookmarket.book.entity.Book;
import org.example.bookmarket.book.repository.BookRepository;
import org.example.bookmarket.book.service.BookService;
import org.example.bookmarket.category.entity.Category;
import org.example.bookmarket.category.repository.CategoryRepository;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.common.service.S3UploadService;
import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.usedbook.entity.UsedBookImage;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.example.bookmarket.user.entity.SocialType;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsedBookPostService {

    private final UsedBookRepository usedBookRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final AiService aiService;
    private final S3UploadService s3UploadService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registerUsedBook(UsedBookPostRequest request) {
        User seller = getCurrentUser(); // 현재 로그인한 사용자 정보를 가져옵니다.

        Book book = bookService.getOrCreateByIsbn(request.getIsbn());

        if (request.getNewPrice() != null) {
            if (book.getNewPrice() == null || !book.getNewPrice().equals(request.getNewPrice())) {
                book.setNewPrice(request.getNewPrice());
                bookRepository.save(book);
            }
        }

        List<String> imageUrls = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (MultipartFile imageFile : request.getImages()) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    String imageUrl = s3UploadService.upload(imageFile, "used-book-images");
                    imageUrls.add(imageUrl);
                }
            }
        }

        if (imageUrls.isEmpty()) {
            throw new CustomException(ErrorCode.USED_BOOK_IMAGE_REQUIRED);
        }

        PriceSuggestResponse aiResponse = null;
        String representativeImageUrl = imageUrls.get(0);
        try {
            int basePrice = Objects.requireNonNullElse(book.getNewPrice(), 30000);
            aiResponse = aiService.suggestPriceFromImage(representativeImageUrl, basePrice);
        } catch (IOException e) {
            log.error("책 등록 중 AI 이미지 분석에 실패했습니다. ISBN: {}", request.getIsbn(), e);
            throw new CustomException(ErrorCode.AI_ANALYSIS_FAILED);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        UsedBook usedBook = UsedBook.builder()
                .seller(seller)
                .book(book)
                .category(category)
                .conditionGrade(request.getConditionGrade())
                .detailedCondition(request.getDetailedCondition())
                .sellingPrice(request.getSellingPrice())
                .aiSuggestedMinPrice(aiResponse != null ? aiResponse.getSuggestedMinPrice() : null)
                .aiSuggestedMaxPrice(aiResponse != null ? aiResponse.getSuggestedMaxPrice() : null)
                .aiDetectedDefects(aiResponse != null ? aiResponse.getDetectedDefects() : null)
                .status("FOR_SALE")
                .build();

        List<UsedBookImage> usedBookImages = imageUrls.stream()
                .map(url -> UsedBookImage.builder().imageUrl(url).build())
                .toList();
        usedBook.setImages(usedBookImages);

        usedBookRepository.save(usedBook);
        log.info("새로운 중고책이 등록되었습니다. ID: {}", usedBook.getId());
    }

    /**
     * Spring Security의 SecurityContextHolder에서 현재 인증된 사용자 정보를 조회하여 반환하는 메소드입니다.
     * 이메일/비밀번호 로그인과 소셜 로그인(KAKAO) 사용자를 모두 처리할 수 있습니다.
     * @return 현재 로그인된 User 엔티티
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user; // 일반 로그인 사용자 처리
        } else if (principal instanceof OAuth2User oauth2User) {
            // OAuth2 소셜 로그인 사용자 처리
            String socialId = oauth2User.getAttribute("id").toString();
            return userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, socialId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "소셜 로그인 사용자를 DB에서 찾을 수 없습니다."));
        }

        throw new CustomException(ErrorCode.AUTHENTICATION_FAILED, "지원하지 않는 인증 방식입니다.");
    }
}