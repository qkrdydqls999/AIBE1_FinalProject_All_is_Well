package org.example.bookmarket.book.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -516103030L;

    public static final QBook book = new QBook("book");

    public final StringPath author = createString("author");

    public final StringPath coverImageUrl = createString("coverImageUrl");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isbn = createString("isbn");

    public final NumberPath<Integer> newPrice = createNumber("newPrice", Integer.class);

    public final NumberPath<Integer> publicationYear = createNumber("publicationYear", Integer.class);

    public final StringPath publisher = createString("publisher");

    public final StringPath title = createString("title");

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

