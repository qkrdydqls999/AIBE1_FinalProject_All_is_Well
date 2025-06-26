package org.example.bookmarket.trade.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.trade.dto.TradeRequest;
import org.example.bookmarket.trade.dto.TradeResponse;
import org.example.bookmarket.trade.entity.*;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.user.entity.User;
import org.example.bookmarket.trade.repository.TradeRepository;
import org.example.bookmarket.usedbook.repository.UsedBookRepository;
import org.example.bookmarket.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final UsedBookRepository usedBookRepository;
    private final UserRepository userRepository;

    @Override
    public TradeResponse createTrade(TradeRequest request) {
        UsedBook usedBook = usedBookRepository.findById(request.usedBookId())
            .orElseThrow(() -> new CustomException(ErrorCode.USED_BOOK_NOT_FOUND));

        User buyer = userRepository.findById(request.buyerId())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User seller = usedBook.getSeller();

        if (buyer.getId().equals(seller.getId())) {
            throw new CustomException(ErrorCode.CANNOT_BUY_OWN_ITEM);
        }

        boolean exists = tradeRepository.existsByUsedBookIdAndStatusIn(
            usedBook.getId(),
            List.of(TradeStatus.REQUESTED, TradeStatus.CONFIRMED)
        );
        if (exists) {
            throw new CustomException(ErrorCode.TRADE_ALREADY_EXISTS);
        }

        usedBook.setStatus("PENDING_TRANSACTION");
        usedBookRepository.save(usedBook);

        Trade tx = Trade.builder()
                .usedBook(usedBook)
                .buyer(buyer)
                .seller(seller)
                .agreedPrice(request.agreedPrice())
                .status(TradeStatus.REQUESTED)
                .tradeType(request.tradeType())
                .pickupLocation(request.pickupLocation())
                .deliveryAddress(request.deliveryAddress())
                .buyerContactInfo(request.buyerContactInfo())
                .sellerContactInfo(null)
                .build();

        Trade saved = tradeRepository.save(tx);
        return toResponse(saved);
    }

    @Override
    public TradeResponse updateStatus(Long tradeId, String newStatus) {
        Trade tx = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found"));
        tx.setStatus(TradeStatus.valueOf(newStatus));
        return toResponse(tradeRepository.save(tx));
    }

    @Override
    public TradeResponse getTradeById(Long id) {
        Trade tx = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found"));
        return toResponse(tx);
    }

    @Override
    public List<TradeResponse> getMyTrades(Long userId, String role) {
        List<Trade> list = tradeRepository.findAll();
        return list.stream()
                .filter(tx -> "buyer".equals(role) ? tx.getBuyer().getId().equals(userId) : tx.getSeller().getId().equals(userId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TradeResponse toResponse(Trade tx) {
        return new TradeResponse(
                tx.getId(),
                tx.getUsedBook().getId(),
                tx.getBuyer().getId(),
                tx.getSeller().getId(),
                tx.getAgreedPrice(),
                tx.getStatus(),
                tx.getTradeType(),
                tx.getPickupLocation(),
                tx.getDeliveryAddress(),
                tx.getBuyerContactInfo(),
                tx.getSellerContactInfo()
        );
    }
}
