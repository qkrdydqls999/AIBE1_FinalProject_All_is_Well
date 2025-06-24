package org.example.bookmarket.profile.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.profile.dto.ProfileResponse;
import org.example.bookmarket.profile.dto.ProfileUpdateRequest;
//import org.example.bookmarket.common.handler.exception.CustomException;
//import org.example.bookmarket.common.handler.exception.ErrorCode;
//import org.example.bookmarket.user.entity.User;
//import org.example.bookmarket.user.repository.UserRepository;
//import org.example.bookmarket.category.repository.CategoryRepository;
//import org.example.bookmarket.user.repository.UserCategoryRepository;
//import org.example.bookmarket.user.entity.UserCategory;
//import org.example.bookmarket.category.entity.Category;
//import org.springframework.stereotype.Service;

//import java.util.*;
//import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
public class ProfileServiceImpl /* implements ProfileService */ {

    // private final UserRepository userRepository;
    // private final CategoryRepository categoryRepository;
    // private final UserCategoryRepository userCategoryRepository;

    // @Override
    public Object getMyProfile(Long userId) {
        // User user = userRepository.findById(userId)
        //         .orElseThrow(() -> new CustomException(ErrorCode.PROFILE_USER_NOT_FOUND));

        // List<String> categories = userCategoryRepository.findByUser(user).stream()
        //         .map(uc -> uc.getCategory().getName())
        //         .toList();

        // return new ProfileResponse(
        //         user.getNickname(),
        //         user.getEmail(),
        //         user.getProfileImageUrl(),
        //         categories
        // );
        return null;
    }

    // @Override
    public void updateMyProfile(Long userId, Object request) {
        // User user = userRepository.findById(userId)
        //         .orElseThrow(() -> new CustomException(ErrorCode.PROFILE_USER_NOT_FOUND));

        // user.setNickname(request.nickname());
        // user.setProfileImageUrl(request.profileImageUrl());

        // List<UserCategory> existing = userCategoryRepository.findByUser(user);
        // Set<Long> existingIds = existing.stream()
        //         .map(uc -> uc.getCategory().getId())
        //         .collect(Collectors.toSet());

        // Set<Long> newIds = new HashSet<>(request.interestCategoryIds());

        // List<UserCategory> toDelete = existing.stream()
        //         .filter(uc -> !newIds.contains(uc.getCategory().getId()))
        //         .toList();

        // List<Long> toAdd = newIds.stream()
        //         .filter(id -> !existingIds.contains(id))
        //         .toList();

        // userCategoryRepository.deleteAll(toDelete);

        // List<Category> categoriesToAdd = categoryRepository.findAllById(toAdd);
        // List<UserCategory> toInsert = categoriesToAdd.stream()
        //         .map(cat -> UserCategory.builder().user(user).category(cat).build())
        //         .toList();

        // userCategoryRepository.saveAll(toInsert);
    }

    // @Override
    public Object getMyDmList(Long userId) {
        // TODO: 구현 예정
        return null;
    }

    // @Override
    public Object getMySellBooks(Long userId) {
        return null;
    }

    // @Override
    public Object getMyPurchases(Long userId) {
        return null;
    }

    // @Override
    public Object getMyWishlist(Long userId) {
        return null;
    }
}
