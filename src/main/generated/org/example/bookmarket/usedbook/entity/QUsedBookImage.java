package org.example.bookmarket.usedbook.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsedBookImage is a Querydsl query type for UsedBookImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsedBookImage extends EntityPathBase<UsedBookImage> {

    private static final long serialVersionUID = -1488804713L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUsedBookImage usedBookImage = new QUsedBookImage("usedBookImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> orderIndex = createNumber("orderIndex", Integer.class);

    public final QUsedBook usedBook;

    public QUsedBookImage(String variable) {
        this(UsedBookImage.class, forVariable(variable), INITS);
    }

    public QUsedBookImage(Path<? extends UsedBookImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUsedBookImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUsedBookImage(PathMetadata metadata, PathInits inits) {
        this(UsedBookImage.class, metadata, inits);
    }

    public QUsedBookImage(Class<? extends UsedBookImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.usedBook = inits.isInitialized("usedBook") ? new QUsedBook(forProperty("usedBook"), inits.get("usedBook")) : null;
    }

}

