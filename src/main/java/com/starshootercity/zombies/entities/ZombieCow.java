package com.starshootercity.zombies.entities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

public class ZombieCow extends ZombieEntity {
    protected ZombieCow(Location loc) {
        super(EntityType.COW, loc, "Cow");
    }

    protected ZombieCow(Mob mob) {
        super(mob);
    }

    @Override
    public void complete() {
        super.complete();
        setSpeed(1.1);
        setStrength(4.0);
    }
}
