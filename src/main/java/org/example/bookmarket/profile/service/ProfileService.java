
package org.example.bookmarket.profile.service;

import org.example.bookmarket.chat.dto.ChatSummary;
import org.example.bookmarket.trade.dto.PurchaseSummary;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.wishlist.dto.WishlistItem;
import org.example.bookmarket.profile.dto.ProfileResponse;
import org.example.bookmarket.profile.dto.ProfileUpdateRequest;

import java.util.List;

public interface ProfileService {

    ProfileResponse getMyProfile(Long userId);

    void updateMyProfile(Long userId, ProfileUpdateRequest request);

    List<ChatSummary> getMyDmList(Long userId);

    List<UsedBookSummary> getMySellBooks(Long userId);

    List<PurchaseSummary> getMyPurchases(Long userId);

    List<WishlistItem> getMyWishlist(Long userId);
}
