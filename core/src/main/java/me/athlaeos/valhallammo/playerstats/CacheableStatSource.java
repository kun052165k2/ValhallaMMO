package me.athlaeos.valhallammo.playerstats;

import org.bukkit.entity.Entity;

public interface CacheableStatSource extends StatSource {
    double get(Entity entity);
    void set(Entity entity, double value);
    boolean hasCachedEntry(Entity entity);

    void reset(Entity entity);
}
