package org.example.bookmarket.usedbook.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.usedbook.dto.BookIsbnResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NaverBookIsbnService implements BookIsbnService {

    private final RestTemplate restTemplate;

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    private static final String NAVER_API_URL = "https://openapi.naver.com/v1/search/book_adv.json?d_isbn=";

    @Override
    public BookIsbnResponse fetchBookInfo(String isbn) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<NaverApiResponse> response = restTemplate.exchange(
                    NAVER_API_URL + isbn,
                    HttpMethod.GET,
                    entity,
                    NaverApiResponse.class
            );

            NaverApiResponse apiResponse = response.getBody();
            if (apiResponse == null || apiResponse.getItems() == null || apiResponse.getItems().isEmpty()) {
                throw new CustomException(ErrorCode.BOOK_NOT_FOUND);
            }

            NaverBookItem bookItem = apiResponse.getItems().get(0);

            // API 응답을 BookIsbnResponse DTO로 변환
            return BookIsbnResponse.builder()
                    .title(removeHtmlTags(bookItem.getTitle()))
                    .author(removeHtmlTags(bookItem.getAuthor()))
                    .publisher(removeHtmlTags(bookItem.getPublisher()))
                    .publicationYear(bookItem.getPubdate()) // YYYYMMDD 형식의 문자열
                    .newPrice(parsePrice(bookItem.getDiscount())) // API에서는 discount가 정가
                    .imageUrl(bookItem.getImage())
                    .build();

        } catch (Exception e) {
            // API 호출 실패 또는 책 정보 없음
            throw new CustomException(ErrorCode.BOOK_NOT_FOUND, "해당 ISBN의 도서 정보를 찾을 수 없습니다.");
        }
    }

    // 네이버 API 응답에 포함된 HTML 태그 제거 유틸리티
    private String removeHtmlTags(String text) {
        return text != null ? text.replaceAll("<[^>]*>", "") : "";
    }

    // 가격 문자열을 정수로 변환
    private Integer parsePrice(String price) {
        try {
            return Integer.parseInt(price);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // --- 네이버 API 응답을 매핑하기 위한 내부 DTO 클래스들 ---
    @Getter
    @Setter
    private static class NaverApiResponse {
        private List<NaverBookItem> items;
    }

    @Getter
    @Setter
    private static class NaverBookItem {
        private String title;
        private String author;
        private String publisher;
        private String pubdate; // 출판일 (YYYYMMDD)
        private String discount; // 네이버 API에서는 discount 필드가 정가 정보
        private String image;
    }
}