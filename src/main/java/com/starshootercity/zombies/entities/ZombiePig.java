package com.starshootercity.zombies.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

public class ZombiePig extends ZombieEntity {
    protected ZombiePig(Location loc) {
        super(EntityType.PIG, loc, "Pig");
    }

    protected ZombiePig(Mob mob) {
        super(mob);
    }


}
