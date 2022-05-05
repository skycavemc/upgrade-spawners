package de.leonheuer.skycave.upgradespawners;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.leonheuer.skycave.upgradespawners.annotations.CreateDataFolder;
import de.leonheuer.skycave.upgradespawners.annotations.Prefix;
import de.leonheuer.skycave.upgradespawners.listeners.BlockPlaceListener;
import de.leonheuer.skycave.upgradespawners.models.SkyCavePlugin;
import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

@Prefix("&6&lSpawner &8» ")
@CreateDataFolder
public final class UpgradeSpawners extends SkyCavePlugin {

    private MongoClient client;
    private MongoCollection<Spawner> spawners;

    @Override
    public void onEnable() {
        // database
        CodecRegistry registry = CodecRegistries.fromRegistries();
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(registry).build();
        client = MongoClients.create(settings);
        MongoDatabase db = client.getDatabase("upgrade_spawners");
        spawners = db.getCollection("spawners", Spawner.class);

        // listeners
        registerEvents(new BlockPlaceListener(this));
    }

    @Override
    public void onDisable() {
        client.close();
    }

    public MongoCollection<Spawner> getSpawners() {
        return spawners;
    }

}
