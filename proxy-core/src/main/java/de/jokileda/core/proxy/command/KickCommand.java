package de.jokileda.core.proxy.command;

import de.jokileda.core.api.proxy.punish.PunishType;
import de.jokileda.core.api.shared.uuid.UUIDFetcher;
import de.jokileda.core.proxy.Core;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class KickCommand extends Command implements TabExecutor {

  private final Core core;

  public KickCommand(Core core) {
    super("kick");
    this.core = core;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if(!(sender instanceof ProxiedPlayer player))
      return;
    if(!player.hasPermission("jokileda.command.punish"))
      return;
    if(args.length == 0) {
      sendHelpMap(player);
      return;
    }
    if(args.length >= 2) {
      UUID target = UUIDFetcher.getUUID(args[0]);
      String reason = "";
      for(int i = 1; i < args.length; i++) {
        reason += args[i] + " ";
      }
      core.getPunishProviderExtension().punishPlayer(target, reason, 0, PunishType.KICK);
    }
  }

  private void sendHelpMap(ProxiedPlayer player) {
    player.sendMessage(new TextComponent(core.getMessages().getString("core.command.kick.help", "HelpMap")));
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
    List<String> completions = new ArrayList<>();
    if(args.length == 1) {
      core.getProxy().getPlayers().forEach(all -> completions.add(all.getName()));
    }
    return completions;
  }
}
