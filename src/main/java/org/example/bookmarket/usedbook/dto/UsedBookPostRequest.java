package org.example.bookmarket.usedbook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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