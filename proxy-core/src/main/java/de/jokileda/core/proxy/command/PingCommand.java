package de.jokileda.core.proxy.command;

import de.jokileda.core.proxy.Core;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PingCommand extends Command {

  private final Core core;

  public PingCommand(Core core) {
    super("ping");
    this.core = core;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer player)) {
      return;
    }
    player.sendMessage(core.getMessages()
        .getString("core.command.ping", "%ping%", "%ping%",
            String.valueOf(player.getPing())));
  }
}
