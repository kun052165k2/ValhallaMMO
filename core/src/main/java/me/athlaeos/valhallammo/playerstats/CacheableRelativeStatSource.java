package me.athlaeos.valhallammo.playerstats;

import org.bukkit.entity.Entity;

public interface CacheableRelativeStatSource extends RelativeStatSource, StatSource {
    void reset(Entity primaryEntity, Entity relativeEntity);
}
