package org.example.bookmarket.book.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmarket.book.entity.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverBookClient {

    private static final String API_URL = "https://openapi.naver.com/v1/search/book_adv.json";

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public Book fetchBook(String isbn) {
        String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("d_isbn", isbn)
                .build()
                .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        try {
            ResponseEntity<NaverBookResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), NaverBookResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                NaverBookResponse body = response.getBody();
                if (body != null && body.items != null && !body.items.isEmpty()) {
                    Item item = body.items.get(0);
                    return Book.builder()
                            .isbn(isbn)
                            .title(stripHtml(item.title))
                            .author(item.author)
                            .publisher(item.publisher)
                            .publicationYear(parseYear(item.pubdate))
                            .newPrice(resolvePrice(item))
                            .description(item.description)
                            .coverImageUrl(item.image)
                            .build();
                }
            }
        } catch (Exception e) {
            log.error("Failed to call Naver Book API", e);
        }
        return null;
    }

    private String stripHtml(String text) {
        return text == null ? null : text.replaceAll("<[^>]*>", "");
    }

    private Integer parseYear(String pubdate) {
        if (pubdate == null || pubdate.length() < 4) return null;
        try {
            return Integer.parseInt(pubdate.substring(0, 4));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    private Integer resolvePrice(Item item) {
        Integer discount = parseInt(item.discount);
        if (discount != null && discount > 0) {
            return discount;
        }
        return parseInt(item.price);
    }

    private record NaverBookResponse(List<Item> items) {}
    private record Item(String title, String author, String publisher, String pubdate,
                        String price, String discount, String image, String description) {}
}