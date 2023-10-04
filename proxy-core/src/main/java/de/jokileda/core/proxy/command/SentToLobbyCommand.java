package de.jokileda.core.proxy.command;

import de.jokileda.core.proxy.Core;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SentToLobbyCommand extends Command {

  private final Core core;

  public SentToLobbyCommand(Core core) {
    super("l", null, "hub", "lobby", "h", "hubschrauber");
    this.core = core;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if(!(sender instanceof ProxiedPlayer player))
      return;
    player.connect(core.getProxy().getServerInfo("lobby"));
  }
}
