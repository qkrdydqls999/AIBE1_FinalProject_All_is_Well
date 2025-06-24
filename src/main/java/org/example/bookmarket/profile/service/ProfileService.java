
package org.example.bookmarket.profile.service;

import org.example.bookmarket.profile.dto.ProfileResponse;
import org.example.bookmarket.profile.dto.ProfileUpdateRequest;

import java.util.List;

public interface ProfileService {

    ProfileResponse getMyProfile(Long userId);

    void updateMyProfile(Long userId, ProfileUpdateRequest request);

    List<String> getMyDmList(Long userId); // TODO: ChatSummary 대신 임시 문자열 목록

    List<String> getMySellBooks(Long userId); // TODO: UsedBookSummary 대신 임시 문자열 목록

    List<String> getMyPurchases(Long userId); // TODO: PurchaseSummary 대신 임시 문자열 목록

    List<String> getMyWishlist(Long userId); // TODO: WishlistItem 대신 임시 문자열 목록
}
