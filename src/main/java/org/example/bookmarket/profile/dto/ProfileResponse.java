
package org.example.bookmarket.profile.dto;

import java.util.List;

public record ProfileResponse(
    String nickname,
    String email,
    String profileImageUrl,
    List<String> interestCategories
) {}
