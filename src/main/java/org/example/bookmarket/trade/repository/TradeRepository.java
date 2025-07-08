package org.example.bookmarket.trade.repository;

import org.example.bookmarket.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    boolean existsByUsedBookIdAndStatusIn(Long usedBookId, List<org.example.bookmarket.trade.entity.TradeStatus> statuses);
    java.util.List<Trade> findByBuyerId(Long buyerId);
    java.util.List<Trade> findBySellerId(Long sellerId);
}