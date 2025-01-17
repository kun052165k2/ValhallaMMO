package me.athlaeos.valhallammo.playerstats;

import me.athlaeos.valhallammo.item.ItemBuilder;
import org.bukkit.entity.Entity;

public interface RelativeStatSource {
    /**
     * Fetches stats similarly to fetch(Entity, boolean), except it's used primarily when the stat is influenced by
     * properties of two entities (mainly when one is attacked by another).
     * Things like armor penetration for example are possessed by the attacker, or sometimes an attacker's damage
     * depends on the armor the victim is wearing.
     * @param victim the entity being attacked
     * @param attackedBy the attacker entity
     * @param primaryItem the primary item held, typically the attacker's weapon
     * @param use whether the stat is only meant for display purposes or actual physical execution
     * @return the stat to return
     */
    double fetch(Entity victim, Entity attackedBy, ItemBuilder primaryItem, boolean use);
}
