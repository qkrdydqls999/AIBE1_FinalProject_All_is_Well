package org.example.bookmarket.usedbook.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.bookmarket.usedbook.entity.QUsedBook.usedBook;
import static org.example.bookmarket.book.entity.QBook.book;

@Repository
@RequiredArgsConstructor
public class UsedBookRepositoryImpl implements UsedBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UsedBook> findByKeywords(List<String> keywords) {
        return queryFactory
                .selectFrom(usedBook)
                .join(usedBook.book, book).fetchJoin()
                .where(keywordsContains(keywords))
                .orderBy(usedBook.createdAt.desc())
                .fetch();
    }

    private BooleanExpression keywordsContains(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return null;
        }
        return keywords.stream()
                .map(this::keywordContains)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

    /**
     * [수정] CLOB/TEXT 타입인 description 필드를 검색 조건에서 제외합니다.
     */
    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        // 제목, 저자, 출판사를 대상으로만 검색을 수행합니다.
        return book.title.containsIgnoreCase(keyword)
                .or(book.author.containsIgnoreCase(keyword))
                .or(book.publisher.containsIgnoreCase(keyword));
    }
}