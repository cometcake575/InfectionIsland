package com.starshootercity.zombies.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

public class ZombieChicken extends ZombieEntity {
    protected ZombieChicken(Location loc) {
        super(EntityType.CHICKEN, loc, "Chicken");
    }

    protected ZombieChicken(Mob mob) {
        super(mob);
    }

    @Override
    public void complete() {
        super.complete();
        setSpeed(1.5);
    }
}
