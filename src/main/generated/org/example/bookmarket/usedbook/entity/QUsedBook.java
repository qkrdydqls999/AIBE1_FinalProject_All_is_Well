package org.example.bookmarket.usedbook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsedBook is a Querydsl query type for UsedBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsedBook extends EntityPathBase<UsedBook> {

    private static final long serialVersionUID = -1917949148L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUsedBook usedBook = new QUsedBook("usedBook");

    public final org.example.bookmarket.common.QTimeEntity _super = new org.example.bookmarket.common.QTimeEntity(this);

    public final ListPath<String, StringPath> aiDetectedDefects = this.<String, StringPath>createList("aiDetectedDefects", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> aiSuggestedMaxPrice = createNumber("aiSuggestedMaxPrice", Integer.class);

    public final NumberPath<Integer> aiSuggestedMinPrice = createNumber("aiSuggestedMinPrice", Integer.class);

    public final org.example.bookmarket.book.entity.QBook book;

    public final org.example.bookmarket.category.entity.QCategory category;

    public final StringPath conditionGrade = createString("conditionGrade");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath detailedCondition = createString("detailedCondition");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<UsedBookImage, QUsedBookImage> images = this.<UsedBookImage, QUsedBookImage>createList("images", UsedBookImage.class, QUsedBookImage.class, PathInits.DIRECT2);

    public final org.example.bookmarket.user.entity.QUser seller;

    public final NumberPath<Integer> sellingPrice = createNumber("sellingPrice", Integer.class);

    public final StringPath status = createString("status");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUsedBook(String variable) {
        this(UsedBook.class, forVariable(variable), INITS);
    }

    public QUsedBook(Path<? extends UsedBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUsedBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUsedBook(PathMetadata metadata, PathInits inits) {
        this(UsedBook.class, metadata, inits);
    }

    public QUsedBook(Class<? extends UsedBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new org.example.bookmarket.book.entity.QBook(forProperty("book")) : null;
        this.category = inits.isInitialized("category") ? new org.example.bookmarket.category.entity.QCategory(forProperty("category")) : null;
        this.seller = inits.isInitialized("seller") ? new org.example.bookmarket.user.entity.QUser(forProperty("seller")) : null;
    }

}

