package org.example.bookmarket.profile.dto;

import java.util.List;

public record ProfileUpdateRequest(
        String profileImageUrl,
        List<Long> interestCategoryIds
) {}