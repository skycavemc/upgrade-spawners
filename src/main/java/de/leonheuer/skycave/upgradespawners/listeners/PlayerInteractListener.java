package de.leonheuer.skycave.upgradespawners.listeners;

import com.mongodb.client.model.Filters;
import de.leonheuer.mcguiapi.gui.GUI;
import de.leonheuer.mcguiapi.gui.GUIPattern;
import de.leonheuer.mcguiapi.utils.ItemBuilder;
import de.leonheuer.skycave.upgradespawners.UpgradeSpawners;
import de.leonheuer.skycave.upgradespawners.enums.Message;
import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractListener implements Listener {

    private final UpgradeSpawners main;

    public PlayerInteractListener(UpgradeSpawners main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (event.useInteractedBlock() != Event.Result.ALLOW || block == null || block.getType() != Material.SPAWNER) {
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
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            return;
        }

        GUIPattern pattern = GUIPattern.ofPattern("bbbbbbbbb", "_________", "bbbbbbbbb")
                .withMaterial('b', ItemBuilder.of(Material.BLACK_STAINED_GLASS_PANE).name("§0").asItem())
                .withMaterial('_', null)
                .startAtLine(1);
        main.getGuiFactory().createGUI(3, "§6§lLevelbarer Spawner")
                .formatPattern(pattern)
                .show(event.getPlayer());
        event.setUseInteractedBlock(Event.Result.ALLOW);
        event.setUseItemInHand(Event.Result.DENY);
    }

}
