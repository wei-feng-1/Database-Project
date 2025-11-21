package com.gamemarket.controller;

import com.gamemarket.entity.Asset;
import com.gamemarket.entity.MarketOrder;
import com.gamemarket.repository.AssetRepository;
import com.gamemarket.repository.MarketOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/market")
@CrossOrigin(origins = "*")
public class MarketController {

    @Autowired
    private MarketOrderRepository orderRepository;

    @Autowired
    private AssetRepository assetRepository;

    @GetMapping("/listings")
    public List<Map<String, Object>> getListings(@RequestParam(required = false) String sort, @RequestParam(required = false) Integer limit) {
        // Simplified logic: return open orders as listings
        // In a real app, this would aggregate data or filter by sort type (hot, gainers)
        List<MarketOrder> orders = orderRepository.findByStatus("OPEN");
        
        return orders.stream().limit(limit != null ? limit : 10).map(order -> {
            Asset asset = order.getAsset();
            return Map.<String, Object>of(
                "id", asset.getAssetId(),
                "name", asset.getAssetName(),
                "price", order.getPrice(),
                "change", 0.0, // Mock change
                "img", "https://via.placeholder.com/300x200" // Mock image
            );
        }).collect(Collectors.toList());
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return List.of("Rifle", "Sniper", "Pistol", "Knife");
    }
}
