package org.example.bookmarket.trade.service;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.common.handler.exception.CustomException;
import org.example.bookmarket.common.handler.exception.ErrorCode;
import org.example.bookmarket.trade.entity.Trade;
import org.example.bookmarket.trade.entity.TradeStatus;
import org.example.bookmarket.trade.repository.TradeRepository;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;

    @Override
    @Transactional
    public Trade createTrade(ChatChannel channel, UsedBook usedBook, User seller, User buyer) {
        Trade trade = Trade.builder()
                .channel(channel)
                .usedBook(usedBook)
                .seller(seller)
                .buyer(buyer)
                .agreedPrice(usedBook.getSellingPrice())
                .status(TradeStatus.REQUESTED)
                .build();
        return tradeRepository.save(trade);
    }

    @Override
    @Transactional(readOnly = true)
    public Trade getTradeByChannel(Long channelId) {
        return tradeRepository.findByChannelId(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRADE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<Trade> findTradeByChannel(Long channelId) {
        return tradeRepository.findByChannelId(channelId);
    }
    @Override
    @Transactional
    public Trade completeTrade(Long tradeId, Integer price) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRADE_NOT_FOUND));
        trade.setAgreedPrice(price);
        trade.setStatus(TradeStatus.COMPLETED);
        trade.getUsedBook().markAsSold();
        return trade;
    }

    @Override
    @Transactional
    public Trade cancelTrade(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRADE_NOT_FOUND));
        trade.setStatus(TradeStatus.CANCELED);
        return trade;
    }
}