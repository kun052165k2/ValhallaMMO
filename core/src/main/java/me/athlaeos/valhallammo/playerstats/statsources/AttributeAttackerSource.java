package me.athlaeos.valhallammo.playerstats.statsources;

import me.athlaeos.valhallammo.item.*;
import me.athlaeos.valhallammo.item.item_attributes.AttributeWrapper;
import me.athlaeos.valhallammo.listeners.InteractListener;
import me.athlaeos.valhallammo.playerstats.CacheableRelativeStatSource;
import me.athlaeos.valhallammo.playerstats.CacheableStatSource;
import me.athlaeos.valhallammo.playerstats.RelativeStatSource;
import me.athlaeos.valhallammo.utility.EntityUtils;
import me.athlaeos.valhallammo.utility.ItemUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AttributeAttackerSource implements CacheableRelativeStatSource {
    private final String attribute;
    private WeightClass weightClass = null;
    private String statPenalty = null;
    private final boolean negative;
    private boolean ignoreProjectiles = true;

    public AttributeAttackerSource(String attribute){
        this.attribute = attribute;
        this.negative = false;
    }
    public AttributeAttackerSource(String attribute, boolean negative){
        this.attribute = attribute;
        this.negative = negative;
    }
    /**
     * Heavily decreases the attributes gotten if the player fails to meet the item's skill requirements. The statPenalties to choose from are defined in config.yml
     */
    public AttributeAttackerSource penalty(String statPenalty){
        this.statPenalty = statPenalty;
        return this;
    }
    /**
     * Causes the source to only consider equipment if it also matches the appropriate weight class (excluding held items)
     */
    public AttributeAttackerSource weight(WeightClass weightClass){
        this.weightClass = weightClass;
        return this;
    }
    /**
     * Causes the source to also scan a projectile's attributes (if any)
     */
    public AttributeAttackerSource proj(){
        this.ignoreProjectiles = false;
        return this;
    }

    @Override
    public double fetch(Entity victim, Entity attackedBy, ItemBuilder primaryItem, boolean use) {
        if (cache.containsKey(attackedBy.getUniqueId())) return cache.get(attackedBy.getUniqueId());
        double value = 0;
        LivingEntity trueAttacker = attackedBy instanceof LivingEntity a ? a : (attackedBy instanceof Projectile p && p.getShooter() instanceof LivingEntity l ? l : null);
        if (trueAttacker == null) return value;

        // gather stats from real attacker, and include only main hand if it was a melee attack (not a projectile)
        value += (negative ? -1 : 1) * (!(trueAttacker instanceof Projectile) ?
                EntityUtils.combinedAttackerAttributeValue(trueAttacker, attribute, weightClass, statPenalty, !InteractListener.attackedWithOffhand(attackedBy)) :
                EntityUtils.combinedAttributeValue(trueAttacker, attribute, weightClass, statPenalty, false));

        if (!ignoreProjectiles && attackedBy instanceof Projectile p){
            ItemBuilder item = ItemUtils.getStoredItem(p);
            if (item == null) return value;
            AttributeWrapper wrapper = ItemAttributesRegistry.getAnyAttribute(item.getMeta(), attribute);
            if (wrapper == null) return value;
            double multiplier = 1;
            if (attribute != null && trueAttacker instanceof Player player) multiplier += ItemSkillRequirements.getPenalty(player, item.getMeta(), attribute);
            value += wrapper.getValue() * multiplier;
        }

        Collection<ArmorSet> activeSets = ArmorSetRegistry.getActiveArmorSets(trueAttacker);
        for (ArmorSet set : activeSets){
            value += (negative ? -1 : 1) * set.getSetBonus().getOrDefault(attribute, 0D);
        }
        return value;
    }

    @Override
    public double fetch(Entity statPossessor, ItemBuilder primaryItem, boolean use) {
        return 0;
    }

    private final Map<UUID, Double> cache = new HashMap<>();

    @Override
    public void reset(Entity entity, Entity attacker) {
        cache.remove(attacker.getUniqueId());
    }
}
