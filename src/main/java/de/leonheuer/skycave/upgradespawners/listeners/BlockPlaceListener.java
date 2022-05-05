package de.leonheuer.skycave.upgradespawners.listeners;

import de.leonheuer.skycave.upgradespawners.UpgradeSpawners;
import de.leonheuer.skycave.upgradespawners.enums.Message;
import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockPlaceListener implements Listener {

    private final UpgradeSpawners main;

    public BlockPlaceListener(UpgradeSpawners main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        ItemStack spawnerItem = event.getItemInHand();
        if (event.isCancelled() || spawnerItem.getType() != Material.SPAWNER) {
            return;
        }
        Spawner spawner = Spawner.fromItem(spawnerItem);
        if (spawner == null) {
            return;
        }
        spawner.setLocation(event.getBlock().getLocation());
        main.getSpawners().insertOne(spawner);

        Player player = event.getPlayer();
        Message.PLACE.get().send(player);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
    }

}
