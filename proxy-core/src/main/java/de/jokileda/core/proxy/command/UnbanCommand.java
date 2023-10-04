package de.jokileda.core.proxy.command;

import de.jokileda.core.api.proxy.punish.PunishType;
import de.jokileda.core.api.shared.uuid.UUIDFetcher;
import de.jokileda.core.proxy.Core;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommand extends Command {

  private final Core core;

  public UnbanCommand(Core core) {
    super("unban", "jokileda.command.unban");
    this.core = core;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if(!(sender instanceof ProxiedPlayer player))
      return;
    if(!player.hasPermission("jokileda.command.unban"))
      return;
    if(args.length == 2) {
      String targetPlayer = UUIDFetcher.getUUID(args[0]).toString();
      String punishType = args[1];
      if(!core.getPunish().isPunished(targetPlayer)) {
        player.sendMessage(new TextComponent(core.getMessages().getString("core.command.unban.notbanned", "Nix gebannt!")));
        return;
      }
      core.getPunish().removePunishByUnique(targetPlayer, PunishType.valueOf(punishType.toUpperCase()));
      player.sendMessage(new TextComponent(core.getMessages().getString("core.command.unban.successfully", "Is net mehr gebannt!")));
    }
  }
}
