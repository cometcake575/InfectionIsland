package com.starshootercity.zombies;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class PlayerLives implements Listener, CommandExecutor {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        removeLives(event.getPlayer(), 1);
        if (getLives(event.getPlayer()) < 1) {
            event.setCancelled(true);
            Component message = event.deathMessage();
            if (message != null) Bukkit.broadcast(message);
            for (ItemStack i : event.getPlayer().getInventory()) {
                if (i == null) continue;
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), i);
            }
            event.getPlayer().getInventory().clear();
        }
    }

    public static void removeLives(Player player, int amount) {
        setLives(player, getLives(player) - amount);
    }

    public static int getLives(Player player) {
        return ZombieInfection.getInstance().getConfig().getInt("lives.%s".formatted(player.getUniqueId().toString()), -1);
    }
    public static void setLives(Player player, int amount) {
        if (amount < 0) amount = 0;
        ZombieInfection.getInstance().getConfig().set("lives.%s".formatted(player.getUniqueId().toString()), amount);
        updateAppearance(player);
        if (getLives(player) < 1) {
            player.setGameMode(GameMode.SPECTATOR);
            player.getWorld().strikeLightningEffect(player.getLocation());
        }
        ZombieInfection.getInstance().saveConfig();
    }
    public static void updateAppearance(Player player) {
        player.playerListName(Component.text("%s".formatted(player.getName()))
                .color(getColourCode(getLives(player))));
        green.removePlayer(player);
        darkGreen.removePlayer(player);
        Team add = switch (getLives(player)) {
            case 1 -> darkGreen;
            case 2 -> green;
            case 3 -> white;
            default -> null;
        };
        if (add == null) return;
        add.addPlayer(player);
    }
    static Team white;
    static Team green;
    static Team darkGreen;

    public PlayerLives() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getMainScoreboard();

        Team oldWhite = scoreboard.getTeam("white");
        Team oldGreen = scoreboard.getTeam("green");
        Team oldDarkGreen = scoreboard.getTeam("dark-green");

        if (oldWhite != null) oldWhite.unregister();
        if (oldGreen != null) oldGreen.unregister();
        if (oldDarkGreen != null) oldDarkGreen.unregister();

        white = scoreboard.registerNewTeam("white");
        white.prefix(Component.text("[")
                .color(NamedTextColor.DARK_GRAY)
                .append(Component.text("Living")
                .color(TextColor.fromHexString("#446196")))
                .append(Component.text("] ")
                        .color(NamedTextColor.DARK_GRAY)));
        green = scoreboard.registerNewTeam("green");
        green.prefix(Component.text("[")
                .color(NamedTextColor.DARK_GRAY)
                .append(Component.text("Infected")
                .color(TextColor.fromHexString("#809B6B")))
                .append(Component.text("] ")
                        .color(NamedTextColor.DARK_GRAY)));
        darkGreen = scoreboard.registerNewTeam("dark-green");

        darkGreen.prefix(Component.text("[")
                .color(NamedTextColor.DARK_GRAY)
                .append(Component.text("Zombie")
                        .color(TextColor.fromHexString("#446135")))
                .append(Component.text("] ")
                        .color(NamedTextColor.DARK_GRAY)));
    }

    public static TextColor getColourCode(int num) {
        return TextColor.fromHexString(switch (num) {
            case 0 -> "#AAAAAA";
            case 1 -> "#446135";
            case 2 -> "#809B6B";
            default -> "#446196";
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && args[1].matches("\\d")) {
                setLives(target, Integer.parseInt(args[1]));
                return true;
            }
        }
        sender.sendMessage(Component.text("§cSpecify a player and the number of lives"));
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (getLives(event.getPlayer()) == -1) {
            setLives(event.getPlayer(), 3);
        }
        updateAppearance(event.getPlayer());
    }
}
