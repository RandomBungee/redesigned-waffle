package de.jokileda.core.api.spigot;

import de.jokileda.core.api.shared.IPlugin;

public interface ISpigotPlugin extends IPlugin {

  void load();

  void enable();

  void disable();
}
