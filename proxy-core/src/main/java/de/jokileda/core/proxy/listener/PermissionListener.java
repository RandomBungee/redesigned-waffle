package de.jokileda.core.proxy.listener;

import de.jokileda.core.proxy.Core;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@RequiredArgsConstructor
public class PermissionListener implements Listener {

    private final Core core;

    @EventHandler
    public void ensurePlayerHasFullPermissions(PermissionCheckEvent event) {
        if(!(event.getSender() instanceof ProxiedPlayer player))
            return;
        List<String> userPermissions = core.getPermissionUser()
            .getUserLists(player.getUniqueId().toString(), "permissions");
        List<String> userGroups = core.getPermissionUser()
            .getUserLists(player.getUniqueId().toString(), "groups");
        List<String> groupPermissions = core.getPermissionGroup()
            .getGroupPermissions(userGroups);
        if(userPermissions.contains("*")) {
            event.setHasPermission(true);
        }
        if(groupPermissions.contains("*")) {
            event.setHasPermission(true);
        }
    }
}
