package com.gamemarket;

import com.gamemarket.entity.Asset;
import com.gamemarket.entity.Player;
import com.gamemarket.entity.Wallet;
import com.gamemarket.entity.MarketOrder;
import com.gamemarket.repository.AssetRepository;
import com.gamemarket.repository.MarketOrderRepository;
import com.gamemarket.repository.PlayerRepository;
import com.gamemarket.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final PlayerRepository playerRepository;
    private final AssetRepository assetRepository;
    private final WalletRepository walletRepository;
    private final MarketOrderRepository marketOrderRepository;

    public DataLoader(PlayerRepository playerRepository, AssetRepository assetRepository, WalletRepository walletRepository, MarketOrderRepository marketOrderRepository) {
        this.playerRepository = playerRepository;
        this.assetRepository = assetRepository;
        this.walletRepository = walletRepository;
        this.marketOrderRepository = marketOrderRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (playerRepository.count() == 0) {
            Player player = new Player();
            player.setPlayerName("testuser");
            player.setLevel(1);
            playerRepository.save(player);

            Wallet wallet = new Wallet();
            wallet.setPlayerId(player.getPlayerId());
            wallet.setBalance(new BigDecimal("1000.00"));
            walletRepository.save(wallet);
        }

        if (assetRepository.count() == 0) {
            createAsset("Sword of Truth", "Weapon", new BigDecimal("500.00"));
            createAsset("Shield of Valor", "Armor", new BigDecimal("300.00"));
            createAsset("Potion of Healing", "Consumable", new BigDecimal("50.00"));
        }

        if (marketOrderRepository.count() == 0) {
            List<Asset> assets = assetRepository.findAll();
            if (!assets.isEmpty()) {
                Asset sword = assets.stream().filter(a -> a.getAssetName().contains("Sword")).findFirst().orElse(assets.get(0));
                Asset shield = assets.stream().filter(a -> a.getAssetName().contains("Shield")).findFirst().orElse(assets.get(0));

                createOrder(1, sword, "SELL", new BigDecimal("550.00"), 1);
                createOrder(1, shield, "SELL", new BigDecimal("320.00"), 1);
            }
        }
    }

    private Asset createAsset(String name, String type, BigDecimal price) {
        Asset asset = new Asset();
        asset.setAssetName(name);
        asset.setAssetType(type);
        asset.setBasePrice(price);
        return assetRepository.save(asset);
    }

    private void createOrder(Integer playerId, Asset asset, String type, BigDecimal price, Integer quantity) {
        MarketOrder order = new MarketOrder();
        order.setPlayerId(playerId);
        order.setAsset(asset);
        order.setOrderType(type);
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setStatus("OPEN");
        marketOrderRepository.save(order);
    }
}
