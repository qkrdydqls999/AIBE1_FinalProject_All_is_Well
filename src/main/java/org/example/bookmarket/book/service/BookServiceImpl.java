package org.example.bookmarket.book.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.book.dto.BookResponse;
import org.example.bookmarket.book.entity.Book;
import org.example.bookmarket.book.repository.BookRepository;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));

        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getNewPrice(),
                book.getDescription(),
                book.getCoverImageUrl()
        );
    }
}
