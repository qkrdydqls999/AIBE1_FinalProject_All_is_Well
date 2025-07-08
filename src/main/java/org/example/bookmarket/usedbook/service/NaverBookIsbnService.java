package org.example.bookmarket.usedbook.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.usedbook.dto.BookIsbnResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
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

            return BookIsbnResponse.builder()
                    .title(removeHtmlTags(bookItem.getTitle()))
                    .author(removeHtmlTags(bookItem.getAuthor()))
                    .publisher(removeHtmlTags(bookItem.getPublisher()))
                    .publicationYear(bookItem.getPubdate())
                    .newPrice(parsePrice(bookItem.getDiscount()))
                    .imageUrl(bookItem.getImage())
                    .build();

        } catch (RestClientException e) {
            log.error("Naver Book API 호출 중 오류 발생. ISBN: {}", isbn, e);
            throw new CustomException(ErrorCode.EXTERNAL_API_ERROR, "외부 도서 API 호출에 실패했습니다.");
        }
    }

    private String removeHtmlTags(String text) {
        return text != null ? text.replaceAll("<[^>]*>", "") : "";
    }

    private Integer parsePrice(String price) {
        try {
            return Integer.parseInt(price);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // --- 네이버 API 응답 매핑용 DTO 클래스들 ---
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
        private String pubdate;
        private String discount;
        private String image;
    }
}