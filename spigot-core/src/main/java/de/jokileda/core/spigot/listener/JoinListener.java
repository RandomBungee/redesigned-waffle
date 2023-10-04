package de.jokileda.core.spigot.listener;

import de.jokileda.core.spigot.Core;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class JoinListener implements Listener {

  private final Core core;

  @EventHandler
  public void playerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    event.setJoinMessage(null);
    core.setPrefix(player);
  }
}
