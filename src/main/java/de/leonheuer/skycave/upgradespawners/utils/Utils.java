package de.leonheuer.skycave.upgradespawners.utils;

import de.leonheuer.skycave.upgradespawners.UpgradeSpawners;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Utils {

    public static byte @NotNull [] uuidToBytes(@NotNull UUID uuid) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getLeastSignificantBits());
        buffer.putLong(uuid.getMostSignificantBits());
        return buffer.array();
    }

    public static <T, Z> void storeItemData(@NotNull ItemStack item, String key, PersistentDataType<T, Z> type, Z value) {
        NamespacedKey namespacedKey = new NamespacedKey(JavaPlugin.getPlugin(UpgradeSpawners.class), key);
        item.editMeta(meta -> meta.getPersistentDataContainer()
                .set(namespacedKey, type, value));
    }

}
