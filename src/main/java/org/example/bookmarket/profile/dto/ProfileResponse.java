package org.example.bookmarket.profile.dto;

import org.example.bookmarket.user.dto.UserCategoryResponse;

import java.util.List;

public record ProfileResponse(
    String nickname,
    String email,
    String profileImageUrl,
    List<UserCategoryResponse> interestCategories
) {}
