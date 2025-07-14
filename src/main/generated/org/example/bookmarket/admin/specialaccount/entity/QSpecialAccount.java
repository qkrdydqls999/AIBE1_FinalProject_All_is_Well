package org.example.bookmarket.admin.specialaccount.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSpecialAccount is a Querydsl query type for SpecialAccount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpecialAccount extends EntityPathBase<SpecialAccount> {

    private static final long serialVersionUID = -1534309633L;

    public static final QSpecialAccount specialAccount = new QSpecialAccount("specialAccount");

    public final org.example.bookmarket.common.QTimeEntity _super = new org.example.bookmarket.common.QTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final EnumPath<SpecialAccountStatus> status = createEnum("status", SpecialAccountStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSpecialAccount(String variable) {
        super(SpecialAccount.class, forVariable(variable));
    }

    public QSpecialAccount(Path<? extends SpecialAccount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpecialAccount(PathMetadata metadata) {
        super(SpecialAccount.class, metadata);
    }

}

