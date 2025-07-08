package org.example.bookmarket.usedbook.service;

import org.example.bookmarket.usedbook.dto.UsedBookResponse;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;

import java.util.List;

public interface UsedBookQueryService {
    UsedBookResponse getUsedBookById(Long id);
    List<UsedBookSummary> getAllUsedBooks();

    /**
     * 최신 등록된 중고책을 조회합니다.
     *
     * @param limit 최대 조회 건수
     * @return 최신 중고책 목록
     */
    List<UsedBookResponse> getLatestUsedBooks(int limit);
}