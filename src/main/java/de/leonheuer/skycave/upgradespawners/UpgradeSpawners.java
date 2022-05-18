package de.leonheuer.skycave.upgradespawners;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.leonheuer.mcguiapi.gui.GUIFactory;
import de.leonheuer.skycave.upgradespawners.annotations.CreateDataFolder;
import de.leonheuer.skycave.upgradespawners.annotations.Prefix;
import de.leonheuer.skycave.upgradespawners.codecs.LocationCodec;
import de.leonheuer.skycave.upgradespawners.codecs.SpawnerCodecProvider;
import de.leonheuer.skycave.upgradespawners.codecs.UUIDCodec;
import de.leonheuer.skycave.upgradespawners.commands.SpawnerCommand;
import de.leonheuer.skycave.upgradespawners.enums.Upgrade;
import de.leonheuer.skycave.upgradespawners.listeners.BlockBreakListener;
import de.leonheuer.skycave.upgradespawners.listeners.BlockPlaceListener;
import de.leonheuer.skycave.upgradespawners.listeners.PlayerInteractListener;
import de.leonheuer.skycave.upgradespawners.listeners.SpawnerSpawnListener;
import de.leonheuer.skycave.upgradespawners.models.SkyCavePlugin;
import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

@Prefix("&6&lSpawner &8» ")
@CreateDataFolder
public final class UpgradeSpawners extends SkyCavePlugin {

    private MongoClient client;
    private MongoCollection<Spawner> spawners;
    private GUIFactory guiFactory;

    @Override
    public void onEnable() {
        super.onEnable();
        guiFactory = new GUIFactory(this);

        // database
        CodecRegistry registry = CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(new LocationCodec(), new UUIDCodec()),
                CodecRegistries.fromProviders(new SpawnerCodecProvider()),
                MongoClientSettings.getDefaultCodecRegistry()
        );
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(registry).build();
        client = MongoClients.create(settings);
        MongoDatabase db = client.getDatabase("upgrade_spawners");
        spawners = db.getCollection("spawners", Spawner.class);

        // listeners
        registerEvents(
                new BlockPlaceListener(this),
                new PlayerInteractListener(this),
                new BlockBreakListener(this),
                new SpawnerSpawnListener(this)
        );

        // commands
        registerCommand("spawner", new SpawnerCommand());

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Spawner spawner : this.spawners.find()) {
                Location loc = spawner.getLocation();
                int radius = spawner.getUpgrades().get(Upgrade.RADIUS); // TODO get real radius
                Collection<Player> players = loc.getNearbyEntitiesByType(Player.class, radius);
                if (players.isEmpty()) continue;

                int amount = spawner.getUpgrades().get(Upgrade.AMOUNT); // TODO get real amount
                for (int i = 0; i < amount; i++) {
                    // TODO random location
                    loc.getWorld().spawnEntity(loc, spawner.getEntity().getType());
                }
            }
        }, 0, 20 * 20);
    }

    @Override
    public void onDisable() {
        client.close();
    }

    public MongoCollection<Spawner> getSpawners() {
        return spawners;
    }

    public GUIFactory getGuiFactory() {
        return guiFactory;
    }

}
