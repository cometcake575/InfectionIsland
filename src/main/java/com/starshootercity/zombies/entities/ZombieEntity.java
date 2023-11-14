package com.starshootercity.zombies.entities;

import com.starshootercity.zombies.ZombieInfection;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftMob;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public abstract class ZombieEntity {

    private final Mob bukkitEntity;

    public Mob getBukkitEntity() {
        return bukkitEntity;
    }

    protected ZombieEntity(EntityType type, Location loc, String entityName) {
        bukkitEntity = (Mob) loc.getWorld().spawnEntity(loc, type);
        bukkitEntity.setPersistent(true);
        bukkitEntity.customName(Component.text("Zombified %s".formatted(entityName)));
        bukkitEntity.setCustomNameVisible(false);
        bukkitEntity.getPersistentDataContainer().set(new NamespacedKey(ZombieInfection.getInstance(), "CustomEntity"), PersistentDataType.BOOLEAN, true);
        AttributeInstance instance = bukkitEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (instance != null) {
            instance.setBaseValue(instance.getValue() * 2.5);
            bukkitEntity.setHealth(instance.getValue());
        }
        complete();
    }

    protected ZombieEntity(Mob mob) {
        bukkitEntity = mob;
        complete();
    }

    protected void registerGoals() {
        Bukkit.getMobGoals().removeAllGoals(bukkitEntity);
        FloatGoal floatGoal = new FloatGoal(((CraftMob) bukkitEntity).getHandle());

        PlayerTargetGoal targetGoal = new PlayerTargetGoal(this);
        Bukkit.getMobGoals().addGoal(bukkitEntity, 1, targetGoal);
        Bukkit.getMobGoals().addGoal(bukkitEntity, 0, floatGoal.asPaperVanillaGoal());
    }

    public void complete() {
        setSpeed(1.2);
        setStrength(2.0);
        registerGoals();
    }

    private double speed = 0.0;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    private double strength = 0.0;

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getStrength() {
        return strength;
    }

    public void inflict(Player player) {

    }
}
