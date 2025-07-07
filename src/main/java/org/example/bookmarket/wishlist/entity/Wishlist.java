package org.example.bookmarket.wishlist.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookmarket.common.TimeEntity; // 다시 상속받습니다.
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.user.entity.User;

// [개선] @Table 어노테이션에 uniqueConstraints를 추가합니다.
// 이렇게 하면 데이터베이스 수준에서 (user_id, used_book_id) 쌍의 중복을 원천적으로 차단하여
// 한 유저가 같은 책을 여러 번 찜하는 것을 막을 수 있습니다.
@Table(name = "wishlist", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "used_book_id"})
})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist extends TimeEntity { // TimeEntity를 상속받아 createdAt(찜한 일시) 자동 관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 관리하기 쉬운 단일 기본 키를 사용합니다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_book_id", nullable = false)
    private UsedBook usedBook;

    // [개선] @Builder는 클래스 레벨이 아닌, 생성자 위에 직접 붙여주는 것이
    // 객체 생성의 명확성을 높이고 안정성을 보장하는 가장 좋은 방법입니다.
    @Builder
    public Wishlist(User user, UsedBook usedBook) {
        this.user = user;
        this.usedBook = usedBook;
    }
}