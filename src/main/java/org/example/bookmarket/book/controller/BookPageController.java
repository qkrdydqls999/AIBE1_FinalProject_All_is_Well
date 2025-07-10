package org.example.bookmarket.book.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmarket.ai.service.AiService;
import org.example.bookmarket.category.dto.CategoryResponse;
import org.example.bookmarket.category.service.CategoryService;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.usedbook.dto.BookIsbnResponse;
import org.example.bookmarket.usedbook.service.BookIsbnService;
import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;
import org.example.bookmarket.usedbook.dto.UsedBookResponse;
import org.example.bookmarket.usedbook.service.UsedBookPostService;
import org.example.bookmarket.usedbook.service.UsedBookQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookPageController {

    private final UsedBookQueryService usedBookQueryService;
    private final CategoryService categoryService;
    private final BookIsbnService bookIsbnService;
    private final UsedBookPostService usedBookPostService;
    private final AiService aiService;


    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         Model model) {
        if (keyword.isBlank()) {
            model.addAttribute("keyword", keyword);
            model.addAttribute("searchResults", Collections.emptyList());
            return "search-results";
        }

        try {
            // 1. AI 서비스에게 키워드 추출 요청
            List<String> extractedKeywords = aiService.extractKeywordsFromQuery(keyword);
            log.info("사용자 검색어 '{}'에서 AI가 추출한 키워드: {}", keyword, extractedKeywords);

            // 2. 추출된 키워드로 DB에서 중고책 검색
            List<UsedBookResponse> allResults = usedBookQueryService.getUsedBooksByKeywords(extractedKeywords);
            int pageSize = 16;
            int fromIndex = Math.min(page * pageSize, allResults.size());
            int toIndex = Math.min(fromIndex + pageSize, allResults.size());
            List<UsedBookResponse> searchResults = allResults.subList(fromIndex, toIndex);

            model.addAttribute("searchResults", searchResults);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", (int) Math.ceil(allResults.size() / (double) pageSize));
            model.addAttribute("totalResults", allResults.size());

        } catch (IOException e) {
            // 3. AI 서비스에서 (네트워크 등) 오류 발생 시
            log.error("AI 키워드 추출 서비스 호출 중 오류 발생", e);
            throw new CustomException(ErrorCode.AI_ANALYSIS_FAILED);
        }

        model.addAttribute("keyword", keyword);
        return "search-results";
    }

    /**
     *  특정 중고책의 상세 페이지를 보여줍니다.
     */
    @GetMapping("/used-books/{bookId}")
    public String bookDetail(@PathVariable Long bookId, Model model) {
        UsedBookResponse book = usedBookQueryService.getUsedBookById(bookId);
        model.addAttribute("book", book);
        return "book-detail";
    }

    /**
     *  ISBN으로 책 정보를 조회하는 API (주로 책 등록 페이지에서 사용)
     */
    @GetMapping("/used-books/isbn/{isbn}")
    @ResponseBody
    public ResponseEntity<BookIsbnResponse> getBookInfoByIsbn(@PathVariable String isbn) {
        BookIsbnResponse bookInfo = bookIsbnService.fetchBookInfo(isbn);
        return ResponseEntity.ok(bookInfo);
    }

    /**
     * 중고책 판매 등록 페이지를 보여줍니다.
     */
    @GetMapping("/used-books/new")
    public String registerBookForm(Model model) {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("bookRequest", new UsedBookPostRequest()); // 폼 바인딩을 위한 빈 객체
        return "book-register";
    }

    /**
     *  중고책 판매 등록 폼 제출을 처리합니다.
     */
    @PostMapping("/used-books")
    public String registerUsedBook(@ModelAttribute UsedBookPostRequest request) {
        usedBookPostService.registerUsedBook(request);
        // 등록 성공 후 마이페이지의 판매 목록으로 리다이렉트
        return "redirect:/profile/me";
    }
}