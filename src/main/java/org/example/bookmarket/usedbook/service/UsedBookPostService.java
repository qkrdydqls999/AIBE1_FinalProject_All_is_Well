package org.example.bookmarket.usedbook.service;

import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;

public interface UsedBookPostService {
    UsedBookSummary registerUsedBook(UsedBookPostRequest request);
}
