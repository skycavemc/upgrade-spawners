package de.leonheuer.skycave.upgradespawners.listeners;

import com.mongodb.client.model.Filters;
import de.leonheuer.skycave.upgradespawners.UpgradeSpawners;
import de.leonheuer.skycave.upgradespawners.enums.Message;
import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements Listener {

    private final UpgradeSpawners main;

    public BlockBreakListener(UpgradeSpawners main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        Block block = event.getBlock();
        if (event.isCancelled() || block.getType() != Material.SPAWNER) {
            return;
        }

        Bson filter = Filters.eq("location", block.getLocation());
        Spawner spawner = main.getSpawners().find(filter).first();
        if (spawner == null) {
            return;
        }
        Player player = event.getPlayer();
        if (spawner.getOwner().compareTo(player.getUniqueId()) != 0) {
            Message.NO_OWNER.get()
                    .replace("%player", Bukkit.getOfflinePlayer(spawner.getOwner()).getName())
                    .send(player);
            event.setCancelled(true);
            return;
        }

        if (!player.isSneaking()) {
            Message.MUST_SNEAK.get().send(player);
            event.setCancelled(true);
            return;
        }

        event.setDropItems(false);
        player.getInventory().addItem(spawner.asItem());
        Message.BREAK_SUCCESS.get().send(player);
        main.getSpawners().deleteOne(filter);
    }

}
