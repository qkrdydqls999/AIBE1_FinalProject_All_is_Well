package org.example.bookmarket.wishlist.entity;

import java.io.Serializable;
import java.util.Objects;

// 복합 키를 위한 클래스
public class WishlistId implements Serializable {
    private Long user;
    private Long usedBook;

    // hashCode and equals 구현 필수
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistId that = (WishlistId) o;
        return Objects.equals(user, that.user) && Objects.equals(usedBook, that.usedBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, usedBook);
    }
}