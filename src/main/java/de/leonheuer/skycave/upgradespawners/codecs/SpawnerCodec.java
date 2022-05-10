package de.leonheuer.skycave.upgradespawners.codecs;

import de.leonheuer.skycave.upgradespawners.enums.SpawnerEntity;
import de.leonheuer.skycave.upgradespawners.enums.Upgrade;
import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.apache.commons.lang.Validate;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class SpawnerCodec implements Codec<Spawner> {

    private final Codec<Location> locationCodec;
    private final Codec<UUID> uuidCodec;

    public SpawnerCodec(@NotNull CodecRegistry registry) {
        locationCodec = registry.get(Location.class);
        uuidCodec = registry.get(UUID.class);
    }

    @Override
    public Spawner decode(@NotNull BsonReader reader, DecoderContext decoderContext) {
        Spawner spawner = new Spawner();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            switch (reader.readName()) {
                case "_id" -> spawner.setId(reader.readObjectId());
                case "location" -> locationCodec.decode(reader, decoderContext);
                case "fuel_seconds" -> spawner.setFuelSeconds(reader.readInt32());
                case "upgrades" -> {
                    reader.readStartDocument();
                    EnumMap<Upgrade, Integer> map = new EnumMap<>(Upgrade.class);
                    while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                        Upgrade upgrade;
                        try {
                            upgrade = Upgrade.valueOf(reader.readName());
                            map.put(upgrade, reader.readInt32());
                        } catch (IllegalArgumentException e) {
                            reader.skipValue();
                        }
                    }
                    reader.readEndDocument();
                    spawner.setUpgrades(map);
                }
                case "entity" -> {
                    try {
                        spawner.setEntity(SpawnerEntity.valueOf(reader.readString()));
                    } catch (IllegalArgumentException e) {
                        spawner.setEntity(SpawnerEntity.NONE);
                    }
                }
                case "owner" -> spawner.setOwner(uuidCodec.decode(reader, decoderContext));
                case "instant_kill" -> spawner.setInstantKill(reader.readBoolean());
                default -> reader.skipValue();
            }
        }
        reader.readEndDocument();
        return spawner;
    }

    @Override
    public void encode(@NotNull BsonWriter writer, @NotNull Spawner value, EncoderContext encoderContext) {
        Validate.notNull(value);
        writer.writeStartDocument();
        writer.writeName("location");
        locationCodec.encode(writer, value.getLocation(), encoderContext);
        writer.writeInt32("fuel_seconds", value.getFuelSeconds());
        writer.writeName("upgrades");
        writer.writeStartDocument();
        for (Map.Entry<Upgrade, Integer> entry : value.getUpgrades().entrySet()) {
            writer.writeInt32(entry.getKey().toString(), entry.getValue());
        }
        writer.writeEndDocument();
        writer.writeString("entity", value.getEntity().toString());
        writer.writeName("owner");
        uuidCodec.encode(writer, value.getOwner(), encoderContext);
        writer.writeName("instant_kill");
        writer.writeBoolean(value.isInstantKill());
        writer.writeEndDocument();
    }

    @Override
    public Class<Spawner> getEncoderClass() {
        return Spawner.class;
    }

}
