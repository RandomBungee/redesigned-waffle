package de.jokileda.core.spigot.listener;

import de.jokileda.core.spigot.Core;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;

@RequiredArgsConstructor
public class PermissionListener implements Listener {

  private final Core core;

  @EventHandler
  public void ensurePlayerPermissionJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    List<String> userPermissions = core.getPermissionUser()
        .getUserLists(player.getUniqueId().toString(), "permissions");
    List<String> userGroups = core.getPermissionUser()
        .getUserLists(player.getUniqueId().toString(), "groups");
    List<String> groupPermissions = core.getPermissionGroup()
        .getGroupPermissions(userGroups);
    userPermissions.forEach(s -> {
      String[] perms = s.split(", ");
      for (String perm : perms) {
        changePermissionAttachment(player, perm, true);
      }
    });
    groupPermissions.forEach(s -> {
      String[] perms = s.split(", ");
      for (String perm : perms) {
        changePermissionAttachment(player, perm, true);
      }
    });
  }

  private void changePermissionAttachment(Player player, String permission,
      boolean add) {
    PermissionAttachment permissionAttachment = player.addAttachment(core);
    if (add) {
      permissionAttachment.setPermission(permission, true);
      return;
    }
    permissionAttachment.unsetPermission(permission);
  }
}
