
package org.example.bookmarket.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookmarket.category.entity.Category;

@Entity
@Table(name = "user_categories")
@IdClass(UserCategoryId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCategory {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
