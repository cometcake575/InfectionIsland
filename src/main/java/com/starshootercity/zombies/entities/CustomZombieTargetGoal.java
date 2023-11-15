package com.starshootercity.zombies.entities;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.starshootercity.zombies.InfectionIsland;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public class CustomZombieTargetGoal implements Goal<Mob> {
    private final GoalKey<Mob> key;
    private final Mob mob;
    private LivingEntity closestEntity;
    private final ZombieEntity entity;

    private double time;

    public CustomZombieTargetGoal(ZombieEntity entity) {
        this.key = GoalKey.of(Mob.class, new NamespacedKey(InfectionIsland.getInstance(), "target_player"));
        this.mob = entity.getBukkitEntity();
        this.entity = entity;
        this.time = Instant.now().getEpochSecond();
    }

    @Override
    public boolean shouldActivate() {
        closestEntity = getClosestEntity();
        return closestEntity != null;
    }


    private LivingEntity getClosestEntity() {
        double closestDistance = -1;
        LivingEntity closestEntity = null;
        Collection<Entity> nearbyEntities = mob.getNearbyEntities(16, 16, 16);
        for (Entity nearbyEntity : nearbyEntities) {
            if (nearbyEntity instanceof Player player) {
                if (!(!player.isDead() && player.getGameMode() != GameMode.SPECTATOR && player.getGameMode() != GameMode.CREATIVE && player.isValid())) continue;
            }
            double distance = nearbyEntity.getLocation().distanceSquared(mob.getLocation());
            if (closestDistance != -1 && !(distance < closestDistance)) {
                continue;
            }
            if (nearbyEntity instanceof LivingEntity living) {
                List<Entity> attacked = CustomEntityRegister.hasAttacked.get(mob);
                if (attacked == null) attacked = new ArrayList<>();
                if (!(CustomEntityRegister.zombifiableMobs.contains(living.getType()) || attacked.contains(nearbyEntity)) || living == mob) continue;
                if (living.getPersistentDataContainer().has(CustomEntityRegister.customEntityKey)) continue;
                closestDistance = distance;
                closestEntity = living;
            }
        }
        return closestEntity;
    }

    @Override
    public @NotNull GoalKey<Mob> getKey() {
        return key;
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE, GoalType.LOOK);
    }

    @Override
    public void tick() {
        mob.setTarget(closestEntity);
        if (mob.getLocation().distanceSquared(closestEntity.getLocation()) < 2.5) {
            if (Instant.now().getEpochSecond() - time < 1) {
                return;
            }
            time = Instant.now().getEpochSecond();
            mob.getPathfinder().stopPathfinding();
            mob.registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            AttributeInstance instance = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (instance != null) instance.setBaseValue(entity.getStrength());
            entity.inflict(closestEntity);
            mob.attack(closestEntity);
        } else {
            double speed = entity.getSpeed();
            if (entity instanceof Ageable ageable) {
                if (!ageable.isAdult()) {
                    speed = Math.max(speed, 1.25);
                }
            }
            mob.getPathfinder().moveTo(closestEntity, speed);
        }
    }
}
