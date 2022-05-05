package de.leonheuer.skycave.upgradespawners.enums;

public enum SpawnerEntity {

    NONE(0, "&cKeins"),
    CREEPER(25000, "Creeper"),
    SKELETON(25000, "Skelett"),
    CAVE_SPIDER(25000, "Höhlenspinne"),
    ZOMBIE(25000, "Zombie"),
    WITCH(200000, "Hexe"),
    IRON_GOLEM(500000, "Eisengolem"),
    ;

    private final int cost;
    private final String name;

    SpawnerEntity(int cost, String name) {
        this.cost = cost;
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }
}
