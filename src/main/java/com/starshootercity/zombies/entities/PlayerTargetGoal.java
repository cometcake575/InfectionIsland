package com.starshootercity.zombies.entities;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.starshootercity.zombies.InfectionIsland;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.EnumSet;

public class PlayerTargetGoal implements Goal<Mob> {
    private final GoalKey<Mob> key;
    private final Mob mob;
    private Player closestPlayer;
    private final ZombieEntity entity;

    public PlayerTargetGoal(ZombieEntity entity) {
        this.key = GoalKey.of(Mob.class, new NamespacedKey(InfectionIsland.getInstance(), "target_player"));
        this.mob = entity.getBukkitEntity();
        this.entity = entity;
    }

    @Override
    public boolean shouldActivate() {
        closestPlayer = getClosestPlayer();
        return closestPlayer != null;
    }


    private Player getClosestPlayer() {
        Collection<Player> nearbyPlayers = mob.getWorld().getNearbyPlayers(mob.getLocation(), 10.0, player ->
                !player.isDead() && player.getGameMode() != GameMode.SPECTATOR && player.getGameMode() != GameMode.CREATIVE && player.isValid());
        double closestDistance = -1;
        Player closestPlayer = null;
        for (Player player : nearbyPlayers) {
            double distance = player.getLocation().distanceSquared(mob.getLocation());
            if (closestDistance != -1 && !(distance < closestDistance)) {
                continue;
            }
            closestDistance = distance;
            closestPlayer = player;
        }
        return closestPlayer;
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
        mob.setTarget(closestPlayer);
        if (mob.getLocation().distanceSquared(closestPlayer.getLocation()) < 1) {
            mob.getPathfinder().stopPathfinding();
            mob.registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            AttributeInstance instance = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (instance != null) instance.setBaseValue(entity.getStrength());
            entity.inflict(closestPlayer);
            mob.attack(closestPlayer);
        } else {
            mob.getPathfinder().moveTo(closestPlayer, entity.getSpeed());
        }
    }
}
