package me.athlaeos.valhallammo.playerstats.statsources;

import me.athlaeos.valhallammo.playerstats.AccumulativeStatManager;
import me.athlaeos.valhallammo.playerstats.StatSource;
import me.athlaeos.valhallammo.playerstats.RelativeStatSource;
import org.bukkit.entity.Entity;

/**
 * Yes, the name is intentionally bad, lol
 */
public class DefensiveSourceSource implements StatSource, RelativeStatSource {
    private final String source;

    public DefensiveSourceSource(String source){
        this.source = source;
    }

    @Override
    public double fetch(Entity statPossessor, boolean use) {
        return AccumulativeStatManager.getCachedStats(source, statPossessor, 10000, true);
    }

    @Override
    public double fetch(Entity victim, Entity attackedBy, boolean use) {
        return AccumulativeStatManager.getCachedRelationalStats(source, victim, attackedBy, 10000, true);
    }
}
