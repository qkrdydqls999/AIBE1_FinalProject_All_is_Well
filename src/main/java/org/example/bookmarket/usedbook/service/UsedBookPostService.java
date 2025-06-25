package org.example.bookmarket.usedbook.service;

import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;

public interface UsedBookPostService {
    void registerUsedBook(UsedBookPostRequest request);
}
