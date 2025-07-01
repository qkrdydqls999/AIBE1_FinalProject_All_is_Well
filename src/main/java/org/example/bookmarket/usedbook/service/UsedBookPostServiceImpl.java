package org.example.bookmarket.usedbook.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.book.entity.Book;
import org.example.bookmarket.book.repository.BookRepository;
import org.example.bookmarket.category.entity.Category;
import org.example.bookmarket.category.repository.CategoryRepository;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.usedbook.dto.UsedBookPostRequest;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsedBookPostServiceImpl implements UsedBookPostService {

    private final UsedBookRepository usedBookRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UsedBookSummary registerUsedBook(UsedBookPostRequest request) {
        Book book = bookRepository.findByIsbn(request.isbn())
                .orElseGet(() -> bookRepository.save(Book.builder()
                        .isbn(request.isbn())
                        .title(request.title())
                        .author(request.author())
                        .publisher(request.publisher())
                        .publicationYear(request.publicationYear())
                        .build()));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        User seller = userRepository.findById(request.sellerId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UsedBook usedBook = UsedBook.builder()
                .book(book)
                .category(category)
                .seller(seller)
                .conditionGrade(request.conditionGrade())
                .hasWriting(request.hasWriting())
                .hasStains(request.hasStains())
                .hasTears(request.hasTears())
                .hasWaterDamage(request.hasWaterDamage())
                .likeNew(request.likeNew())
                .detailedCondition(request.detailedCondition())
                .sellingPrice(request.sellingPrice())
                .status("AVAILABLE")
                .build();

        UsedBook saved = usedBookRepository.save(usedBook);

        return new UsedBookSummary(
                saved.getId(),
                saved.getBook().getTitle(),
                saved.getSellingPrice(),
                saved.getStatus(),
                null,
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional
    public UsedBookSummary updateUsedBook(Long id, UsedBookPostRequest request) {
        UsedBook usedBook = usedBookRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));

        Book book = bookRepository.findByIsbn(request.isbn())
                .orElseGet(() -> bookRepository.save(Book.builder()
                        .isbn(request.isbn())
                        .title(request.title())
                        .author(request.author())
                        .publisher(request.publisher())
                        .publicationYear(request.publicationYear())
                        .build()));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        usedBook.setBook(book);
        usedBook.setCategory(category);
        usedBook.setConditionGrade(request.conditionGrade());
        usedBook.setHasWriting(request.hasWriting());
        usedBook.setHasStains(request.hasStains());
        usedBook.setHasTears(request.hasTears());
        usedBook.setHasWaterDamage(request.hasWaterDamage());
        usedBook.setLikeNew(request.likeNew());
        usedBook.setDetailedCondition(request.detailedCondition());
        usedBook.setSellingPrice(request.sellingPrice());

        UsedBook saved = usedBookRepository.save(usedBook);

        return new UsedBookSummary(
                saved.getId(),
                saved.getBook().getTitle(),
                saved.getSellingPrice(),
                saved.getStatus(),
                null,
                saved.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public void deleteUsedBook(Long id) {
        UsedBook usedBook = usedBookRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));
        usedBookRepository.delete(usedBook);
    }
}