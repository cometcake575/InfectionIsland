package com.starshootercity.zombies;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ResourcePackSender implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String url = InfectionIsland.getInstance().getConfig().getString("resource-pack.url");
        boolean forceAccept = InfectionIsland.getInstance().getConfig().getBoolean("resource-pack.enforce");
        String packMessage = InfectionIsland.getInstance().getConfig().getString("resource-pack.message");
        String sha1 = InfectionIsland.getInstance().getConfig().getString("resource-pack.sha1");
        if (url == null || url.equals("") || packMessage == null) return;
        if (sha1 == null) {
            event.getPlayer().setResourcePack(url, null, Component.text(packMessage), forceAccept);
        } else event.getPlayer().setResourcePack(url, sha1, forceAccept, Component.text(packMessage));
    }
}
