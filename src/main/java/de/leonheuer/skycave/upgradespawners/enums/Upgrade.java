package de.leonheuer.skycave.upgradespawners.enums;

import org.bukkit.Material;

public enum Upgrade {

    AMOUNT(Material.MINECART, "Anzahl"),
    DISTANCE(Material.FIREWORK_ROCKET, "Aktivierungsradius"),
    MAX_FUEL(Material.LAVA_BUCKET, "Treibstoff Speicher");

    private final Material icon;
    private final String friendlyName;

    Upgrade(Material icon, String friendlyName) {
        this.icon = icon;
        this.friendlyName = friendlyName;
    }

    public Material getIcon() {
        return icon;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

}
