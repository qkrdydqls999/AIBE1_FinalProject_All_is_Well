package org.example.bookmarket.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatChannel is a Querydsl query type for ChatChannel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatChannel extends EntityPathBase<ChatChannel> {

    private static final long serialVersionUID = 550279419L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatChannel chatChannel = new QChatChannel("chatChannel");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastMessageAt = createDateTime("lastMessageAt", java.time.LocalDateTime.class);

    public final org.example.bookmarket.usedbook.entity.QUsedBook relatedUsedBook;

    public final org.example.bookmarket.user.entity.QUser user1;

    public final org.example.bookmarket.user.entity.QUser user2;

    public QChatChannel(String variable) {
        this(ChatChannel.class, forVariable(variable), INITS);
    }

    public QChatChannel(Path<? extends ChatChannel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatChannel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatChannel(PathMetadata metadata, PathInits inits) {
        this(ChatChannel.class, metadata, inits);
    }

    public QChatChannel(Class<? extends ChatChannel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.relatedUsedBook = inits.isInitialized("relatedUsedBook") ? new org.example.bookmarket.usedbook.entity.QUsedBook(forProperty("relatedUsedBook"), inits.get("relatedUsedBook")) : null;
        this.user1 = inits.isInitialized("user1") ? new org.example.bookmarket.user.entity.QUser(forProperty("user1")) : null;
        this.user2 = inits.isInitialized("user2") ? new org.example.bookmarket.user.entity.QUser(forProperty("user2")) : null;
    }

}

