
package org.example.bookmarket.user.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserCategoryId implements Serializable {
    private Long user;
    private Long category;

    public UserCategoryId() {}

    public UserCategoryId(Long user, Long category) {
        this.user = user;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCategoryId that)) return false;
        return Objects.equals(user, that.user) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, category);
    }
}
