package org.example.bookmarket.wishlist.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishlist is a Querydsl query type for Wishlist
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishlist extends EntityPathBase<Wishlist> {

    private static final long serialVersionUID = 654132290L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishlist wishlist = new QWishlist("wishlist");

    public final org.example.bookmarket.common.QTimeEntity _super = new org.example.bookmarket.common.QTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final org.example.bookmarket.usedbook.entity.QUsedBook usedBook;

    public final org.example.bookmarket.user.entity.QUser user;

    public QWishlist(String variable) {
        this(Wishlist.class, forVariable(variable), INITS);
    }

    public QWishlist(Path<? extends Wishlist> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishlist(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishlist(PathMetadata metadata, PathInits inits) {
        this(Wishlist.class, metadata, inits);
    }

    public QWishlist(Class<? extends Wishlist> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.usedBook = inits.isInitialized("usedBook") ? new org.example.bookmarket.usedbook.entity.QUsedBook(forProperty("usedBook"), inits.get("usedBook")) : null;
        this.user = inits.isInitialized("user") ? new org.example.bookmarket.user.entity.QUser(forProperty("user")) : null;
    }

}

