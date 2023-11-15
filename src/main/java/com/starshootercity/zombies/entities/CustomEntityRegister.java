package com.starshootercity.zombies.entities;

import com.starshootercity.zombies.InfectionIsland;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class CustomEntityRegister implements Listener {

    public static ZombieEntity createZombieVariant(EntityType entityType, Location loc) {
        return switch (entityType) {
            case CHICKEN -> new ZombieChicken(loc);
            case COW -> new ZombieCow(loc);
            case PIG -> new ZombiePig(loc);
            case SHEEP -> new ZombieSheep(loc);
            case SPIDER -> new ZombieSpider(loc);
            default -> null;
        };
    }
    public static ZombieEntity loadZombieVariant(Mob mob) {
        return switch (mob.getType()) {
            case CHICKEN -> new ZombieChicken(mob);
            case COW -> new ZombieCow(mob);
            case PIG -> new ZombiePig(mob);
            case SHEEP -> new ZombieSheep(mob);
            case SPIDER -> new ZombieSpider(mob);
            default -> null;
        };
    }

    public static List<EntityType> zombifiableMobs = new ArrayList<>() {{
        add(EntityType.CHICKEN);
        add(EntityType.COW);
        add(EntityType.PIG);
        add(EntityType.SHEEP);
        add(EntityType.SPIDER);
    }};

    protected static NamespacedKey customEntityKey = new NamespacedKey(InfectionIsland.getInstance(), "CustomEntity");

    @EventHandler
    public void onEntitiesLoad(EntitiesLoadEvent event) {
        for (Entity entity : event.getEntities()) {
            if (entity instanceof Mob mob) {
                if (Boolean.TRUE.equals(entity.getPersistentDataContainer().get(customEntityKey, PersistentDataType.BOOLEAN))) {
                    loadZombieVariant(mob).complete();
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (Boolean.TRUE.equals(event.getEntity().getPersistentDataContainer().get(customEntityKey, PersistentDataType.BOOLEAN))) {
            for (ItemStack i : event.getDrops()) {
                i.setType(Material.ROTTEN_FLESH);
            }
            if (event.getEntityType() == EntityType.PIG) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(InfectionIsland.getInstance(), () -> event.getEntity().getLocation().createExplosion(3), 20);
            }
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                InfectionIsland.getInstance(),
                () -> {
                    if (event.getEntity().getPotionEffect(PotionEffectType.REGENERATION) != null) return;
                    ZombieEntity entity = createZombieVariant(event.getEntityType(),
                            event.getEntity().getLocation());
                    if (entity != null) entity.complete();
                    if (entity instanceof ZombieSheep zombieSheep) {
                        if (event.getEntity() instanceof Sheep sheep) {
                            zombieSheep.setColor(sheep.getColor());
                        }
                    }
                },
                20);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.ZOMBIE || event.getEntityType() == EntityType.SKELETON) {
            event.setDamage(event.getDamage() / 2.5);
        }
    }
}

