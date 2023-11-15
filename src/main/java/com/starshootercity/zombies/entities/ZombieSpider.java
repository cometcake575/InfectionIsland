package com.starshootercity.zombies.entities;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZombieSpider extends ZombieEntity {
    protected ZombieSpider(Location loc) {
        super(EntityType.SPIDER, loc, "Spider");
    }

    protected ZombieSpider(Mob mob) {
        super(mob);
    }

    @Override
    public void inflict(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0, false));
    }
}
