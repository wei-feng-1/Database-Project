package com.gamemarket.controller;

import com.gamemarket.entity.Asset;
import com.gamemarket.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private AssetRepository assetRepository;

    @GetMapping("/{id}")
    public Map<String, Object> getItem(@PathVariable Integer id) {
        Asset asset = assetRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        return Map.<String, Object>of(
            "id", asset.getAssetId(),
            "name", asset.getAssetName(),
            "price", asset.getBasePrice(),
            "change", 2.5, // Mock
            "img", "https://via.placeholder.com/400x300",
            "description", "Type: " + asset.getAssetType()
        );
    }

    @GetMapping("/search")
    public List<Asset> searchItems(@RequestParam String q) {
        // Simplified search
        return assetRepository.findAll(); 
    }
}
