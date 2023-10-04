package de.jokileda.core.api.spigot.refelction;

import java.lang.reflect.Field;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftHumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

  @EventHandler
  public void setCustomPermissionBaseByLogin(PlayerLoginEvent event) {
    try {
      Field field = CraftHumanEntity.class.getDeclaredField("perm");
      field.setAccessible(true);
      field.set(event.getPlayer(), new CustomPermissionBase(event.getPlayer()));
      field.setAccessible(false);
      System.out.println("Field was changed");
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
