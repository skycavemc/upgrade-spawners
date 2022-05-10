package de.leonheuer.skycave.upgradespawners.commands;

import de.leonheuer.skycave.upgradespawners.enums.Message;
import de.leonheuer.skycave.upgradespawners.enums.SpawnerEntity;
import de.leonheuer.skycave.upgradespawners.enums.Upgrade;
import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class SpawnerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Message.NO_PLAYER.get().send(sender, false);
            return true;
        }
        // TODO take away money
        EnumMap<Upgrade, Integer> upgrades = new EnumMap<>(Upgrade.class);
        for (Upgrade upgrade : Upgrade.values()) {
            upgrades.put(upgrade, 0);
        }
        Spawner spawner = new Spawner(null, 0, upgrades, SpawnerEntity.NONE, player.getUniqueId(), false);
        player.getInventory().addItem(spawner.asItem());
        // TODO send messages
        return true;
    }

}
