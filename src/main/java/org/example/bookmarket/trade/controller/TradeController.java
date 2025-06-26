
package org.example.bookmarket.trade.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookmarket.trade.dto.TradeRequest;
import org.example.bookmarket.trade.dto.TradeResponse;
import org.example.bookmarket.trade.service.TradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping
    public ResponseEntity<TradeResponse> createTrade(@RequestBody TradeRequest request) {
        return ResponseEntity.ok(tradeService.createTrade(request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TradeResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(tradeService.updateStatus(id, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeResponse> getTradeById(@PathVariable Long id) {
        return ResponseEntity.ok(tradeService.getTradeById(id));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<TradeResponse>> getMyTrades(
            @RequestParam Long userId,
            @RequestParam String role) {
        return ResponseEntity.ok(tradeService.getMyTrades(userId, role));
    }
}
