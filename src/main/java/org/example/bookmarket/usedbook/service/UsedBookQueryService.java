package org.example.bookmarket.usedbook.service;

import org.example.bookmarket.usedbook.dto.UsedBookResponse;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;

import java.util.List;

public interface UsedBookQueryService {
    UsedBookResponse getUsedBookById(Long id);
    List<UsedBookSummary> getAllUsedBooks();

    /**
     * [신규 기능 추가] 키워드 리스트로 중고책을 검색하는 메소드
     */
    List<UsedBookResponse> getUsedBooksByKeywords(List<String> keywords);
}