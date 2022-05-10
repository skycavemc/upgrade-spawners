package de.leonheuer.skycave.upgradespawners.enums;

import de.leonheuer.skycave.upgradespawners.UpgradeSpawners;
import de.leonheuer.skycave.upgradespawners.models.ChatMessage;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Message {

    PLACE("&7Du hast einen &aLevelbaren Spawner &7platziert."),
    NO_PLAYER("&cDu musst ein Spieler sein."),
    NO_OWNER("&cDu besitzt diesen Spawner nicht. &7Er gehört %player."),
    MUST_SNEAK("&cSneaken + Abbauen, um den Levelbaren Spawner abzubauen."),
    BREAK_SUCCESS("&aDu hast den Levelbaren Spawner erfolgreich abgebaut. &7Er wurde in dein Inventar gelegt."),
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
