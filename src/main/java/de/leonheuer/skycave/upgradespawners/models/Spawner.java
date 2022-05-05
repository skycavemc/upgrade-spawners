package de.leonheuer.skycave.upgradespawners.models;

import de.leonheuer.mcguiapi.utils.ItemBuilder;
import de.leonheuer.skycave.upgradespawners.UpgradeSpawners;
import de.leonheuer.skycave.upgradespawners.enums.SpawnerEntity;
import de.leonheuer.skycave.upgradespawners.enums.Upgrade;
import de.leonheuer.skycave.upgradespawners.utils.Utils;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class Spawner {

    @BsonId
    private ObjectId id;
    private Location location;
    @BsonProperty("fuel_seconds")
    private int fuelSeconds;
    private EnumMap<Upgrade, Integer> upgrades;
    private SpawnerEntity entity;
    private UUID owner;
    @BsonProperty("instant_kill")
    private boolean instantKill;

    public Spawner() {
    }

    public Spawner(Location location, int fuelSeconds, EnumMap<Upgrade, Integer> upgrades, SpawnerEntity entity, UUID owner, boolean instantKill) {
        this.location = location;
        this.fuelSeconds = fuelSeconds;
        this.upgrades = upgrades;
        this.entity = entity;
        this.owner = owner;
        this.instantKill = instantKill;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getFuelSeconds() {
        return fuelSeconds;
    }

    public void setFuelSeconds(int fuelSeconds) {
        this.fuelSeconds = fuelSeconds;
    }

    public EnumMap<Upgrade, Integer> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(EnumMap<Upgrade, Integer> upgrades) {
        this.upgrades = upgrades;
    }

    public SpawnerEntity getEntity() {
        return entity;
    }

    public void setEntity(SpawnerEntity entity) {
        this.entity = entity;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public boolean isInstantKill() {
        return instantKill;
    }

    public void setInstantKill(boolean instantKill) {
        this.instantKill = instantKill;
    }

    public ItemStack asItem() {
        ItemStack result = ItemBuilder.of(Material.SPAWNER)
                .name("&b&lLevelbarer Spawner")
                .description(
                        "&8 • &7Besitzer: &6" + Bukkit.getOfflinePlayer(owner).getName(),
                        "&8 • &7Entity: &6" + entity.getName()
                ).asItem();
        for (Map.Entry<Upgrade, Integer> upgrade : upgrades.entrySet()) {
            Utils.storeItemData(result, upgrade.getKey().toString(), PersistentDataType.INTEGER, upgrade.getValue());
        }
        Utils.storeItemData(result, "fuelSeconds", PersistentDataType.INTEGER, fuelSeconds);
        Utils.storeItemData(result, "entity", PersistentDataType.STRING, entity.toString());
        Utils.storeItemData(result, "owner", PersistentDataType.BYTE_ARRAY, Utils.uuidToBytes(owner));
        Utils.storeItemData(result, "instantKill", PersistentDataType.INTEGER, instantKill ? 1 : 0);
        return result;
    }

    public static @Nullable Spawner fromItem(@NotNull ItemStack item) {
        Spawner spawner = new Spawner();
        spawner.setUpgrades(new EnumMap<>(Upgrade.class));
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        UpgradeSpawners main = JavaPlugin.getPlugin(UpgradeSpawners.class);

        for (Upgrade upgrade : Upgrade.values()) {
            NamespacedKey key = new NamespacedKey(main, upgrade.toString());
            if (!container.has(key)) {
                return null;
            }
            spawner.getUpgrades().put(upgrade, container.getOrDefault(key, PersistentDataType.INTEGER, 0));
        }

        NamespacedKey fuelSecondsKey = new NamespacedKey(main, "fuelSeconds");
        if (!container.has(fuelSecondsKey)) {
            return null;
        }
        spawner.setFuelSeconds(container.getOrDefault(fuelSecondsKey, PersistentDataType.INTEGER, 0));

        NamespacedKey entityKey = new NamespacedKey(main, "entity");
        if (!container.has(entityKey)) {
            return null;
        }
        SpawnerEntity entity = SpawnerEntity.valueOf(container.get(entityKey, PersistentDataType.STRING));
        spawner.setEntity(entity);

        NamespacedKey ownerKey = new NamespacedKey(main, "owner");
        if (!container.has(ownerKey)) {
            return null;
        }
        byte[] uuidBytes = container.get(ownerKey, PersistentDataType.BYTE_ARRAY);
        if (uuidBytes == null) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(uuidBytes);
        spawner.setOwner(new UUID(buffer.getLong(), buffer.getLong()));

        NamespacedKey instantKillKey = new NamespacedKey(main, "instantKill");
        if (!container.has(instantKillKey)) {
            return null;
        }
        spawner.setInstantKill(container.getOrDefault(instantKillKey, PersistentDataType.INTEGER, 0) == 1);

        return spawner;
    }

}
