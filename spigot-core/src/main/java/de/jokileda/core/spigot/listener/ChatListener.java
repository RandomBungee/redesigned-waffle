package de.jokileda.core.spigot.listener;

import de.jokileda.core.api.shared.permission.IPermissionGroup;
import de.jokileda.core.api.shared.permission.IPermissionUser;
import de.jokileda.core.spigot.Core;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class ChatListener implements Listener {

  private final Core core;
  private final IPermissionUser permissionUser;
  private final IPermissionGroup permissionGroup;

  @EventHandler
  public void playerPrefixInChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    AtomicReference<String> formattedMessage = new AtomicReference<>("");

    if (player.hasPermission("jokileda.chat.color")) {
      event.setMessage(
          ChatColor.translateAlternateColorCodes('&', event.getMessage()));
    }

    permissionUser.getUserLists(player.getUniqueId().toString(), "groups")
        .forEach(groups -> {
          formattedMessage.set(core.getMessageManager()
              .getString("core.message.chatformat",
                  "%prefix% %player% %message%", "%prefix%",
                  permissionGroup.getGroupPrefix(groups), "%player%",
                  player.getDisplayName(), "%message%", event.getMessage()));
        });
    event.setFormat(formattedMessage.get());
  }
}
