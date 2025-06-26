package org.example.bookmarket.book.service;

import org.example.bookmarket.book.dto.BookResponse;

public interface BookService {
    BookResponse getBookByIsbn(String isbn);
}
