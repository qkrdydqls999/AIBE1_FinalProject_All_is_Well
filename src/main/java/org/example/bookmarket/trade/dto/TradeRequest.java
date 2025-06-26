
package org.example.bookmarket.trade.dto;

import org.example.bookmarket.trade.entity.TradeType;

public record TradeRequest(
    Long usedBookId,
    Long buyerId,
    Integer agreedPrice,
    TradeType tradeType,
    String pickupLocation,
    String deliveryAddress,
    String buyerContactInfo
) {}
