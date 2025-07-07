package org.example.bookmarket.user.repository;

import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

    /**
     * 특정 사용자의 모든 관심 카테고리 정보를 조회합니다.
     * ProfileServiceImpl의 getMyProfile 메서드에서 사용됩니다.
     * @param user 조회할 사용자
     * @return UserCategory 리스트
     */
    List<UserCategory> findByUser(User user);

    /**
     * 특정 사용자의 모든 관심 카테고리 정보를 삭제합니다.
     * ProfileServiceImpl의 updateMyProfile 메서드에서 사용됩니다.
     * @param user 삭제할 사용자
     */
    void deleteByUser(User user);
}