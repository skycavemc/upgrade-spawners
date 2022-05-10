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
import de.leonheuer.skycave.upgradespawners.listeners.BlockBreakListener;
import de.leonheuer.skycave.upgradespawners.listeners.BlockPlaceListener;
import de.leonheuer.skycave.upgradespawners.listeners.PlayerInteractListener;
import de.leonheuer.skycave.upgradespawners.models.SkyCavePlugin;
import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

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
                new BlockBreakListener(this)
        );

        // commands
        registerCommand("spawner", new SpawnerCommand());
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
