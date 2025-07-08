package org.example.bookmarket.usedbook.service;

import org.example.bookmarket.usedbook.dto.BookIsbnResponse;

public interface BookIsbnService {
    /**
     * ISBN을 사용하여 외부 API에서 도서 정보를 가져옵니다.
     * @param isbn 도서 ISBN
     * @return 프론트엔드로 전달할 도서 정보 DTO
     */
    BookIsbnResponse fetchBookInfo(String isbn);
}