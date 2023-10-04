package de.jokileda.core.api.proxy;

import de.jokileda.core.api.shared.IPlugin;

public interface IProxyPlugin extends IPlugin {

  void load();

  void enable();

  void disable();

}
