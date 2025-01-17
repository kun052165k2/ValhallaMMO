package me.athlaeos.valhallammo.playerstats.statsources;

import me.athlaeos.valhallammo.configuration.ConfigManager;
import me.athlaeos.valhallammo.item.WeightClass;
import me.athlaeos.valhallammo.playerstats.StatSource;
import me.athlaeos.valhallammo.playerstats.EntityCache;
import me.athlaeos.valhallammo.playerstats.EntityProperties;
import me.athlaeos.valhallammo.playerstats.RelativeStatSource;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class LightWeaponsDualWieldingImmunityReductionBuffSource implements StatSource, RelativeStatSource {
    private final boolean enabled;
    private final double immunityReductionBuff;

    public LightWeaponsDualWieldingImmunityReductionBuffSource(){
        YamlConfiguration config = ConfigManager.getConfig("skills/light_weapons.yml").reload().get();
        enabled = config.getBoolean("dual_wielding_bonus", true);
        immunityReductionBuff = config.getDouble("dual_wielding_immunity_reduction", 0.2);
    }

    @Override
    public double fetch(Entity p, boolean use) {
        return 0;
    }

    @Override
    public double fetch(Entity victim, Entity attackedBy, boolean use) {
        if (attackedBy instanceof Player pl && enabled){
            EntityProperties properties = EntityCache.getAndCacheProperties(pl);
            if (properties.getOffHand() == null || properties.getMainHand() == null) return 0; // one of the hands is empty, no need to buff
            if (WeightClass.getWeightClass(properties.getOffHand().getMeta()) == WeightClass.LIGHT &&
                    WeightClass.getWeightClass(properties.getMainHand().getMeta()) == WeightClass.LIGHT) return immunityReductionBuff;
        }
        return 0;
    }
}
