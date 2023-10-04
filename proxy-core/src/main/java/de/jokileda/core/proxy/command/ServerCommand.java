package de.jokileda.core.proxy.command;

import de.jokileda.core.proxy.Core;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerCommand extends Command {

  private final Core core;

  public ServerCommand(Core core) {
    super("bauserver", "jokileda.command.bauserver");
    this.core = core;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if(!(sender instanceof ProxiedPlayer player))
      return;
    if(!player.hasPermission("jokileda.command.bauserver"))
      return;
    player.connect(core.getProxy().getServerInfo("bauserver"));
  }
}
