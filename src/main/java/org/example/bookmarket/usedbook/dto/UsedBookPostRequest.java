package org.example.bookmarket.usedbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// record를 class로 변경하고, Lombok 애너테이션을 추가합니다.
@Getter
@Setter
@NoArgsConstructor
public class UsedBookPostRequest {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer publicationYear;
    private Integer newPrice;
    private String conditionGrade;
    private String detailedCondition;
    private Integer sellingPrice;
    private Long categoryId;
    private List<MultipartFile> images;
}