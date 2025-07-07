package org.example.bookmarket.book.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.category.dto.CategoryResponse;
import org.example.bookmarket.category.service.CategoryService;
import org.example.bookmarket.usedbook.dto.BookIsbnResponse;
import org.example.bookmarket.usedbook.service.BookIsbnService;
import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;
import org.example.bookmarket.usedbook.dto.UsedBookResponse;
import org.example.bookmarket.usedbook.service.UsedBookPostService; // [추가] 책 등록 로직을 위한 서비스
import org.example.bookmarket.usedbook.service.UsedBookQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; // [수정] PostMapping, ModelAttribute 등을 사용하기 위해 와일드카드로 변경

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    private final UsedBookQueryService usedBookQueryService;
    private final CategoryService categoryService;
    private final BookIsbnService bookIsbnService;
    private final UsedBookPostService usedBookPostService; // [추가] 책 등록 처리를 위해 주입

    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, Model model) {
        List<UsedBookResponse> searchResults = Collections.emptyList();
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchResults", searchResults);
        return "search-results";
    }

    @GetMapping("/used-books/{bookId}")
    public String bookDetail(@PathVariable Long bookId, Model model) {
        UsedBookResponse book = usedBookQueryService.getUsedBookById(bookId);
        model.addAttribute("book", book);
        return "book-detail";
    }

    @GetMapping("/used-books/isbn/{isbn}")
    @ResponseBody
    public ResponseEntity<BookIsbnResponse> getBookInfoByIsbn(@PathVariable String isbn) {
        BookIsbnResponse bookInfo = bookIsbnService.fetchBookInfo(isbn);
        return ResponseEntity.ok(bookInfo);
    }

    @GetMapping("/used-books/new")
    public String registerBookForm(Model model) {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("bookRequest", new UsedBookPostRequest());
        return "book-register";
    }

    /**
     * [신규 추가]
     * 책 등록 폼(book-register.html)에서 'POST' 방식으로 전송된 데이터를 처리합니다.
     * 성공적으로 책을 등록한 후, '/profile/me' 경로로 리다이렉트하여 사용자를 마이페이지로 이동시킵니다.
     * 이 메소드가 'No static resource profile/main' 오류를 해결합니다.
     */
    @PostMapping("/used-books")
    public String registerUsedBook(@ModelAttribute UsedBookPostRequest request) {
        // 서비스에 책 등록 로직을 위임
        usedBookPostService.registerUsedBook(request);
        // 성공 후, 올바른 주소인 /profile/me로 리다이렉트
        return "redirect:/profile/me";
    }
}