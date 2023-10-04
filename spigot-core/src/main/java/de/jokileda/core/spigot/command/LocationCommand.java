package de.jokileda.core.spigot.command;

import de.jokileda.core.spigot.Core;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class LocationCommand implements CommandExecutor {

  private final Core core;

  @Override
  public boolean onCommand(@NotNull CommandSender sender,
      @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if(!(sender instanceof Player player))
      return true;
    if(!player.hasPermission("jokileda.command.location"))
      return true;
    Location location = player.getLocation();
    player.sendMessage("Aktuelle Location:");
    player.sendMessage("Word: " + location.getWorld().getName());
    player.sendMessage("X: " + location.getX());
    player.sendMessage("Y: " + location.getY());
    player.sendMessage("Z: " + location.getZ());
    player.sendMessage("Yaw: " + location.getYaw());
    player.sendMessage("Pitch: " + location.getPitch());
    return false;
  }
}
