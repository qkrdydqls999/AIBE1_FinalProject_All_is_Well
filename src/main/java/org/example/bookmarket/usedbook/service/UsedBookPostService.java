package org.example.bookmarket.usedbook.service;

import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;

public interface UsedBookPostService {
    UsedBookSummary registerUsedBook(UsedBookPostRequest request);


    UsedBookSummary updateUsedBook(Long id, UsedBookPostRequest request);

    void deleteUsedBook(Long id);
}
