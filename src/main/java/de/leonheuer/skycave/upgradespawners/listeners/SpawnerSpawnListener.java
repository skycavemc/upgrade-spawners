package de.leonheuer.skycave.upgradespawners.listeners;

import com.mongodb.client.model.Filters;
import de.leonheuer.skycave.upgradespawners.UpgradeSpawners;
import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bson.conversions.Bson;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class SpawnerSpawnListener implements Listener {

    private final UpgradeSpawners main;

    public SpawnerSpawnListener(UpgradeSpawners main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawnerSpawn(@NotNull SpawnerSpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Bson filter = Filters.eq("location", event.getSpawner().getLocation());
        Spawner spawner = main.getSpawners().find(filter).first();
        if (spawner != null) {
            event.setCancelled(true);
        }
    }

}
