package com.starshootercity.zombies;

import com.starshootercity.zombies.entities.CustomEntityRegister;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class ZombieInfection extends JavaPlugin {
    private static ZombieInfection instance;

    public static ZombieInfection getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        PluginCommand command = getCommand("set-lives");
        PlayerLives playerLives = new PlayerLives();
        Bukkit.getPluginManager().registerEvents(playerLives, this);
        Bukkit.getPluginManager().registerEvents(new ResourcePackSender(), this);
        Bukkit.getPluginManager().registerEvents(new CustomEntityRegister(), this);
        if (command != null) command.setExecutor(playerLives);
    }
}