package de.leonheuer.skycave.upgradespawners.enums;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum SpawnerEntity {

    NONE(0, "&cKeins", null, Material.BARRIER),
    CREEPER(25000, "Creeper", EntityType.CREEPER, Material.CREEPER_SPAWN_EGG),
    SKELETON(25000, "Skelett", EntityType.SKELETON, Material.SKELETON_SPAWN_EGG),
    CAVE_SPIDER(25000, "Höhlenspinne", EntityType.CAVE_SPIDER, Material.CAVE_SPIDER_SPAWN_EGG),
    ZOMBIE(25000, "Zombie", EntityType.ZOMBIE, Material.ZOMBIE_SPAWN_EGG),
    WITCH(200000, "Hexe", EntityType.WITCH, Material.WITCH_SPAWN_EGG),
    IRON_GOLEM(500000, "Eisengolem", EntityType.IRON_GOLEM, Material.GHAST_SPAWN_EGG),
    ;

    private final int cost;
    private final String name;
    private final EntityType type;
    private final Material icon;

    SpawnerEntity(int cost, String name, EntityType type, Material icon) {
        this.cost = cost;
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public Material getIcon() {
        return icon;
    }
}
