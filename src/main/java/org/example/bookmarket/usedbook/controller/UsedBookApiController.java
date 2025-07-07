package org.example.bookmarket.usedbook.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.book.dto.BookResponse;
import org.example.bookmarket.book.service.BookService;
import org.example.bookmarket.usedbook.dto.UsedBookResponse;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.usedbook.service.UsedBookPurchaseService;
import org.example.bookmarket.usedbook.service.UsedBookQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 중고책 관련 모든 API 요청을 처리하는 통합 컨트롤러입니다.
 * (기존 Query, Purchase, Post 컨트롤러의 기능을 통합)
 */
@RestController
@RequestMapping("/api/used-books")
@RequiredArgsConstructor
public class UsedBookApiController {

    private final UsedBookQueryService usedBookQueryService;
    private final UsedBookPurchaseService usedBookPurchaseService;
    private final BookService bookService;

    /**
     * 모든 중고책 목록을 요약된 정보 형태로 JSON으로 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<UsedBookSummary>> getAllUsedBooks() {
        return ResponseEntity.ok(usedBookQueryService.getAllUsedBooks());
    }

    /**
     * ID로 특정 중고책의 상세 정보를 JSON으로 반환합니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsedBookResponse> getUsedBookById(@PathVariable Long id) {
        return ResponseEntity.ok(usedBookQueryService.getUsedBookById(id));
    }

    /**
     * ISBN으로 책의 기본 정보를 조회하여 JSON으로 반환합니다.
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookInfoByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }

    /**
     * 특정 책을 구매 처리합니다.
     * [중요] 이 API는 데이터만 처리하고 페이지를 이동시키지 않습니다.
     * 페이지 이동은 이 API를 호출한 후 JavaScript에서 처리해야 합니다.
     */
    @PostMapping("/{bookId}/purchase")
    public ResponseEntity<String> purchaseBook(@PathVariable Long bookId) {
        // try-catch를 제거하고, 예외가 발생하면 GlobalExceptionHandler가 처리하도록 위임합니다.
        usedBookPurchaseService.purchase(bookId);
        return ResponseEntity.ok("책 구매에 성공했습니다. ID: " + bookId);
    }
    }
