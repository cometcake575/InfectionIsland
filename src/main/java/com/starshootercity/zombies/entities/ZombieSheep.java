package com.starshootercity.zombies.entities;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZombieSheep extends ZombieEntity {
    protected ZombieSheep(Location loc) {
        super(EntityType.SHEEP, loc, "Sheep");
    }

    public void setColor(DyeColor color) {
        ((Sheep) getBukkitEntity()).setColor(color);
    }

    protected ZombieSheep(Mob mob) {
        super(mob);
    }

    @Override
    public void inflict(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 240, 2, false));
    }
}
