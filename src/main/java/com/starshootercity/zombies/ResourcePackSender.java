package com.starshootercity.zombies;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ResourcePackSender implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String url = ZombieInfection.getInstance().getConfig().getString("pack-url");
        boolean forceAccept = ZombieInfection.getInstance().getConfig().getBoolean("enforce-pack");
        String packMessage = ZombieInfection.getInstance().getConfig().getString("pack-message");
        if (url == null || url.equals("") || packMessage == null) return;
        event.getPlayer().setResourcePack(url, null, Component.text(packMessage), forceAccept);
    }
}
