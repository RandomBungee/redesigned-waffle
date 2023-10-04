package de.jokileda.core.spigot.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import de.jokileda.core.spigot.Core;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitListener implements Listener {

  private final Core core;

  @EventHandler
  public void playerQuitEvent(PlayerQuitEvent event) {
    event.setQuitMessage(null);
  }
}
