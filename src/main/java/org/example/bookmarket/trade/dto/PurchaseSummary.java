
package org.example.bookmarket.trade.dto;

import java.time.LocalDateTime;

public record PurchaseSummary(
    Long transactionId,
    String itemTitle,
    String sellerNickname,
    Integer price,
    String status,
    LocalDateTime updatedAt
) {}
