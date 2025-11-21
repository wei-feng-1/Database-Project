package com.gamemarket.controller;

import com.gamemarket.entity.MarketOrder;
import com.gamemarket.entity.TradeHistory;
import com.gamemarket.repository.AssetRepository;
import com.gamemarket.repository.MarketOrderRepository;
import com.gamemarket.repository.TradeHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trade")
@CrossOrigin(origins = "*")
public class TradeController {

    @Autowired
    private MarketOrderRepository orderRepository;

    @Autowired
    private TradeHistoryRepository tradeHistoryRepository;

    @Autowired
    private AssetRepository assetRepository;

    @PostMapping("/orders")
    public Map<String, String> createOrder(@RequestBody Map<String, Object> payload) {
        MarketOrder order = new MarketOrder();
        order.setPlayerId(1); // Hardcoded user for now
        order.setAsset(assetRepository.findById((Integer) payload.get("itemId")).get());
        order.setPrice(new BigDecimal(payload.get("price").toString()));
        order.setQuantity((Integer) payload.get("amount"));
        order.setOrderType((String) payload.get("type"));
        order.setStatus("OPEN");
        
        orderRepository.save(order);
        return Map.of("message", "Order created successfully");
    }

    @GetMapping("/orders")
    public List<Map<String, Object>> getOrders(@RequestParam(required = false) Integer userId) {
        if (userId != null) {
            // Return trade history for profile
            List<TradeHistory> history = tradeHistoryRepository.findByPlayerId(userId);
            return history.stream().map(h -> Map.<String, Object>of(
                "id", h.getTradeId(),
                "type", "buy", // Simplified
                "itemName", h.getAsset().getAssetName(),
                "date", h.getTradeTime().toString(),
                "price", h.getPrice(),
                "amount", h.getQuantity()
            )).collect(Collectors.toList());
        }
        return List.of();
    }
}
