package org.example.bookmarket.trade.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTrade is a Querydsl query type for Trade
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrade extends EntityPathBase<Trade> {

    private static final long serialVersionUID = 2036906718L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTrade trade = new QTrade("trade");

    public final org.example.bookmarket.common.QTimeEntity _super = new org.example.bookmarket.common.QTimeEntity(this);

    public final NumberPath<Integer> agreedPrice = createNumber("agreedPrice", Integer.class);

    public final org.example.bookmarket.user.entity.QUser buyer;

    public final StringPath buyerContactInfo = createString("buyerContactInfo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath deliveryAddress = createString("deliveryAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath pickupLocation = createString("pickupLocation");

    public final org.example.bookmarket.user.entity.QUser seller;

    public final StringPath sellerContactInfo = createString("sellerContactInfo");

    public final EnumPath<TradeStatus> status = createEnum("status", TradeStatus.class);

    public final EnumPath<TradeType> tradeType = createEnum("tradeType", TradeType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final org.example.bookmarket.usedbook.entity.QUsedBook usedBook;

    public QTrade(String variable) {
        this(Trade.class, forVariable(variable), INITS);
    }

    public QTrade(Path<? extends Trade> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTrade(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTrade(PathMetadata metadata, PathInits inits) {
        this(Trade.class, metadata, inits);
    }

    public QTrade(Class<? extends Trade> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new org.example.bookmarket.user.entity.QUser(forProperty("buyer")) : null;
        this.seller = inits.isInitialized("seller") ? new org.example.bookmarket.user.entity.QUser(forProperty("seller")) : null;
        this.usedBook = inits.isInitialized("usedBook") ? new org.example.bookmarket.usedbook.entity.QUsedBook(forProperty("usedBook"), inits.get("usedBook")) : null;
    }

}

