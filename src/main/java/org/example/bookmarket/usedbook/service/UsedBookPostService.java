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
        User seller = getCurrentUser();

        Book book = bookService.getOrCreateByIsbn(request.getIsbn());

        // 정가 정보 업데이트 로직
        if (request.getNewPrice() != null) {
            if (book.getNewPrice() == null || !book.getNewPrice().equals(request.getNewPrice())) {
                book.setNewPrice(request.getNewPrice());
                bookRepository.save(book);
            }
        }

        // 이미지 업로드 로직
        List<String> imageUrls = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (MultipartFile imageFile : request.getImages()) {
                // 파일이 null이 아니고 비어있지 않은 경우에만 업로드
                if (imageFile != null && !imageFile.isEmpty()) {
                    String imageUrl = s3UploadService.upload(imageFile, "used-book-images");
                    imageUrls.add(imageUrl);
                }
            }
        }

        if (imageUrls.isEmpty()) {
            throw new CustomException(ErrorCode.USED_BOOK_IMAGE_REQUIRED);
        }

        // AI 가격 제안 로직
        String representativeImageUrl = imageUrls.get(0);
        PriceSuggestResponse aiResponse = null;
        try {
            //  AI 분석을 위한 기본 가격 설정 로직을 간소화합니다.
            int basePrice = Objects.requireNonNullElse(book.getNewPrice(), 30000);
            aiResponse = aiService.suggestPriceFromImage(representativeImageUrl, basePrice);
        } catch (IOException e) {
            //  오류 로깅 시 컨텍스트(ISBN)를 포함하여 추적을 용이하게 합니다.
            log.error("AI 이미지 분석 중 오류 발생. ISBN: {}", request.getIsbn(), e);
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
     * [리팩토링] SecurityContext에서 현재 인증된 사용자 정보를 조회하는 로직을 공통 메서드로 추출합니다.
     * @return 현재 로그인된 User 엔티티
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        } else if (principal instanceof OAuth2User oauth2User) {
            // OAuth2 로그인 사용자 처리
            // TODO: SocialType을 동적으로 결정하는 로직이 필요할 수 있습니다. 현재는 KAKAO로 고정.
            String socialId = oauth2User.getAttribute("id").toString();
            return userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, socialId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        }

        // 그 외의 인증 타입은 지원하지 않음
        throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
    }
}