package com.starshootercity.zombies;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ResourcePackSender implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String url = InfectionIsland.getInstance().getConfig().getString("resource-pack.url");
        boolean forceAccept = InfectionIsland.getInstance().getConfig().getBoolean("resource-pack.enforce");
        String packMessage = InfectionIsland.getInstance().getConfig().getString("resource-pack.message");
        Bukkit.broadcast(Component.text(1));
        if (url == null || url.equals("") || packMessage == null) return;
        event.getPlayer().setResourcePack(url, null, Component.text(packMessage), forceAccept);
    }
}
