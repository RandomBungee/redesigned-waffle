package de.jokileda.core.proxy.listener;

import de.jokileda.core.api.proxy.punish.PunishEntry;
import de.jokileda.core.api.proxy.punish.PunishType;
import de.jokileda.core.proxy.Core;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class JoinListener implements Listener {

    private final Core core;

    @EventHandler
    public void ensurePlayerJoin(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PunishEntry punishEntry;
        if(core.getPunish().isPunished(uuid.toString())) {
            punishEntry = core.getPunish().findByUnique(uuid.toString());
            if(!(punishEntry.getPunishType() == PunishType.BAN))
                return;
            long current = System.currentTimeMillis();
            long end = punishEntry.getTime();
            if (current < end || end == -1) {
                core.getPunishProviderExtension().punishPlayer(uuid, punishEntry.getReason(), punishEntry.getTime(), PunishType.BAN);
            } else {
                core.getPunish().removePunishByUnique(uuid.toString(), PunishType.BAN);
            }
        }

        List<String> userPermissions = core.getPermissionUser()
                .getUserLists(player.getUniqueId().toString(), "permissions");
        List<String> userGroups = core.getPermissionUser()
                .getUserLists(player.getUniqueId().toString(), "groups");
        List<String> groupPermissions = core.getPermissionGroup()
                .getGroupPermissions(userGroups);
        userPermissions.forEach(s -> {
            String[] perms = s.split(", ");
            for (String perm : perms) {
                player.setPermission(perm, true);
            }
        });
        groupPermissions.forEach(s -> {
            String[] perms = s.split(", ");
            for (String perm : perms) {
                player.setPermission(perm, true);
            }
        });
    }
}
