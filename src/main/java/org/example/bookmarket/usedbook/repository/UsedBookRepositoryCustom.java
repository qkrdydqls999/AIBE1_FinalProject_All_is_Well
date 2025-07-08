package org.example.bookmarket.usedbook.repository;

import org.example.bookmarket.usedbook.entity.UsedBook;
import java.util.List;

public interface UsedBookRepositoryCustom {
    /**
     * 여러 키워드 중 하나라도 책의 제목, 저자, 설명, 출판사에 포함되는 중고책을 검색합니다.
     * @param keywords AI가 추출하거나 사용자가 직접 입력한 검색 키워드 리스트
     * @return 검색된 중고책 리스트
     */
    List<UsedBook> findByKeywords(List<String> keywords);
}