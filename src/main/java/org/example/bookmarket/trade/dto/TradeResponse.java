
package org.example.bookmarket.trade.dto;

import org.example.bookmarket.trade.entity.TradeStatus;
import org.example.bookmarket.trade.entity.TradeType;

public record TradeResponse(
    Long id,
    Long usedBookId,
    Long buyerId,
    Long sellerId,
    Integer agreedPrice,
    TradeStatus status,
    TradeType tradeType,
    String pickupLocation,
    String deliveryAddress,
    String buyerContactInfo,
    String sellerContactInfo
) {}
