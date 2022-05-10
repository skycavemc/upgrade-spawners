package de.leonheuer.skycave.upgradespawners.codecs;

import de.leonheuer.skycave.upgradespawners.models.Spawner;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class SpawnerCodecProvider implements CodecProvider {

    @SuppressWarnings("unchecked")
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Spawner.class) {
            return (Codec<T>) new SpawnerCodec(registry);
        }
        return null;
    }

}
