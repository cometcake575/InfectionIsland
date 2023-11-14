package com.starshootercity.zombies.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
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
    public void inflict(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0, false));
    }
}
