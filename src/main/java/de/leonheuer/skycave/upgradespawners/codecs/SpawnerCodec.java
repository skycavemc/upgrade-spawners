package de.leonheuer.skycave.upgradespawners.codecs;

import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class SpawnerCodec implements Codec<Spawner> {

    private final Codec<Location> locationCodec;

    public SpawnerCodec(@NotNull CodecRegistry registry) {
        locationCodec = registry.get(Location.class);
    }

    @Override
    public Spawner decode(@NotNull BsonReader reader, DecoderContext decoderContext) {
        Spawner spawner = new Spawner();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            switch (reader.readName()) {
                case "_id" -> spawner.setId(reader.readObjectId());
                case "location" -> locationCodec.decode(reader, decoderContext);
                default -> reader.skipValue();
            }
        }
        reader.readEndDocument();
        return spawner;
    }

    @Override
    public void encode(BsonWriter writer, Spawner value, EncoderContext encoderContext) {

    }

    @Override
    public Class<Spawner> getEncoderClass() {
        return Spawner.class;
    }

}
