package de.jokileda.core.api.spigot.refelction;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;

public class CustomPermissionBase extends PermissibleBase {

  public CustomPermissionBase(Player opable) {
    super(opable);
  }

  @Override
  public boolean hasPermission(String inName) {
    if(super.hasPermission("*")) {
      return true;
    }
    if(super.isOp()) {
      return false;
    }
    return super.hasPermission(inName);
  }
}
