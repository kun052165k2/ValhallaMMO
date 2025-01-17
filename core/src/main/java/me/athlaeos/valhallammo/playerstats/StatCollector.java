package me.athlaeos.valhallammo.playerstats;

import me.athlaeos.valhallammo.playerstats.format.StatFormat;
import org.bukkit.attribute.AttributeModifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StatCollector {
    private boolean attackerPossessive = false;
    private StatFormat format = null;
    private final Map<AttributeModifier.Operation, Collection<StatSource>> statSources = new HashMap<>();
    protected StatCollector(){}

    public void setFormat(StatFormat format) {
        this.format = format;
    }

    public void setAttackerPossessive(boolean attackerPossessive) {
        this.attackerPossessive = attackerPossessive;
    }

    public StatFormat getFormat() {
        return format;
    }

    public Map<AttributeModifier.Operation, Collection<StatSource>> getStatSources() {
        return statSources;
    }

    public void addSource(AttributeModifier.Operation operation, StatSource source){
        Collection<StatSource> sources = statSources.getOrDefault(operation, new HashSet<>());
        sources.add(source);
        statSources.put(operation, sources);
    }

    public boolean isAttackerPossessive() {
        return attackerPossessive;
    }
}
