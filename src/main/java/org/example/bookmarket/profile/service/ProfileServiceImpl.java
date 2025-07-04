package org.example.bookmarket.profile.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.dto.ChatSummary;
import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.chat.entity.ChatMessage;
import org.example.bookmarket.chat.repository.ChatChannelRepository;
import org.example.bookmarket.chat.repository.ChatMessageRepository;
import org.example.bookmarket.profile.dto.ProfileResponse;
import org.example.bookmarket.profile.dto.ProfileUpdateRequest;
import org.example.bookmarket.common.service.S3UploadService;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.trade.dto.PurchaseSummary;
import org.example.bookmarket.trade.entity.Trade;
import org.example.bookmarket.trade.repository.TradeRepository;
import org.example.bookmarket.usedbook.dto.UsedBookSummary;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.example.bookmarket.user.dto.UserCategoryResponse;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.user.entity.UserCategory;
import org.example.bookmarket.user.repository.UserCategoryRepository;
import org.example.bookmarket.user.repository.UserRepository;
import org.example.bookmarket.category.entity.Category;
import org.example.bookmarket.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final UsedBookRepository usedBookRepository;
    private final TradeRepository tradeRepository;
    private final ChatChannelRepository chatChannelRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final S3UploadService s3UploadService;

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        List<UserCategoryResponse> categories = userCategoryRepository.findByUser(user)
                .stream()
                .map(uc -> new UserCategoryResponse(
                        uc.getCategory().getId(),
                        uc.getCategory().getName()))
                .collect(Collectors.toList());
        return new ProfileResponse(
                user.getNickname(),
                user.getEmail(),
                user.getProfileImageUrl(),
                categories
        );
    }
    @Override
    @Transactional
    public void updateMyProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        if (request.nickname() != null) {
            user.setNickname(request.nickname());
        }
        if (request.profileImageUrl() != null) {
            user.setProfileImageUrl(request.profileImageUrl());
        }
        if (request.interestCategoryIds() != null) {
            userCategoryRepository.deleteByUser(user);
            List<Category> categories = categoryRepository.findAllById(request.interestCategoryIds());
            List<UserCategory> list = categories.stream()
                    .map(cat -> UserCategory.builder().user(user).category(cat).build())
                    .collect(Collectors.toList());
            userCategoryRepository.saveAll(list);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public String uploadProfileImage(Long userId, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROFILE_USER_NOT_FOUND));
        if (image == null || image.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PROFILE_IMAGE);
        }
        String url = s3UploadService.upload(image, "profile-images");
        user.setProfileImageUrl(url);
        userRepository.save(user);
        return url;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatSummary> getMyDmList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        List<ChatChannel> channels = chatChannelRepository.findByUser1OrUser2(user, user);
        return channels.stream()
                .map(ch -> {
                    ChatMessage lastMsg = chatMessageRepository
                            .findFirstByChannelOrderBySentAtDesc(ch)
                            .orElse(null);
                    String partner = ch.getUser1().equals(user) ? ch.getUser2().getNickname() : ch.getUser1().getNickname();
                    String lastContent = lastMsg != null ? lastMsg.getMessageContent() : null;
                    return new ChatSummary(ch.getId(), lastContent, partner, ch.getLastMessageAt());
                })
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<UsedBookSummary> getMySellBooks(Long userId) {
        List<UsedBook> list = usedBookRepository.findBySellerId(userId);
        return list.stream()
                .map(ub -> new UsedBookSummary(
                        ub.getId(),
                        ub.getBook().getTitle(),
                        ub.getSellingPrice(),
                        ub.getStatus(),
                        null,
                        ub.getUpdatedAt()))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseSummary> getMyPurchases(Long userId) {
        List<Trade> trades = tradeRepository.findByBuyerId(userId);
        return trades.stream()
                .map(tx -> new PurchaseSummary(
                        tx.getId(),
                        tx.getUsedBook().getBook().getTitle(),
                        tx.getSeller().getNickname(),
                        tx.getAgreedPrice(),
                        tx.getStatus().name(),
                        tx.getUpdatedAt()))
                .collect(Collectors.toList());
    }
    @Override
    public List<org.example.bookmarket.wishlist.dto.WishlistItem> getMyWishlist(Long userId) {
        return List.of();
    }
}