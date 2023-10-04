package de.jokileda.core.proxy.listener;

import de.jokileda.core.api.proxy.punish.PunishEntry;
import de.jokileda.core.api.proxy.punish.PunishType;
import de.jokileda.core.proxy.Core;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

@RequiredArgsConstructor
public class ChatListener implements Listener {

    private final Core core;

    @EventHandler
    public void ensurePlayerChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        UUID uuid = player.getUniqueId();
        if(event.getMessage().startsWith("/"))
            return;
        if(!core.getPunish().isPunished(uuid.toString()))
            return;
        PunishEntry punishEntry = core.getPunish().findByUnique(uuid.toString());
        if(!(punishEntry.getPunishType() == PunishType.MUTE))
            return;
        long current = System.currentTimeMillis();
        long end = punishEntry.getTime();
        if (current < end || end == -1) {
            event.setCancelled(true);
            core.getPunishProviderExtension().punishPlayer(uuid, punishEntry.getReason(), punishEntry.getTime(), PunishType.MUTE);
        } else {
            core.getPunish().removePunishByUnique(uuid.toString(), PunishType.MUTE);
        }
    }
}
