package de.leonheuer.skycave.upgradespawners.enums;

import de.leonheuer.skycave.upgradespawners.UpgradeSpawners;
import de.leonheuer.skycave.upgradespawners.models.ChatMessage;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Message {

    PLACE("&7Du hast einen &aLevelbaren Spawner &7platziert."),
    ;

    private final String string;

    Message(String string) {
        this.string = string;
    }

    @Contract(" -> new")
    public @NotNull ChatMessage get() {
        return new ChatMessage(JavaPlugin.getPlugin(UpgradeSpawners.class), string);
    }
}
