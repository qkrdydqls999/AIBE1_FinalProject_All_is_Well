package org.example.bookmarket.trade.service;

import org.example.bookmarket.chat.entity.ChatChannel;
import org.example.bookmarket.trade.entity.Trade;
import org.example.bookmarket.usedbook.entity.UsedBook;
import org.example.bookmarket.user.entity.User;

public interface TradeService {
    Trade createTrade(ChatChannel channel, UsedBook usedBook, User seller, User buyer);
    Trade getTradeByChannel(Long channelId);
    java.util.Optional<Trade> findTradeByChannel(Long channelId);
    Trade completeTrade(Long tradeId, Integer price);
    Trade cancelTrade(Long tradeId);
}
