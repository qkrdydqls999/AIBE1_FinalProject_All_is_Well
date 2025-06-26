
package org.example.bookmarket.trade.service;

import org.example.bookmarket.trade.dto.TradeRequest;
import org.example.bookmarket.trade.dto.TradeResponse;

import java.util.List;

public interface TradeService {
    TradeResponse createTrade(TradeRequest request);
    TradeResponse updateStatus(Long tradeId, String newStatus);
    TradeResponse getTradeById(Long id);
    List<TradeResponse> getMyTrades(Long userId, String role);
}
