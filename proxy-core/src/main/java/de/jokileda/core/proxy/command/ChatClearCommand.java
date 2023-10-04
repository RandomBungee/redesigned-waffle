package de.jokileda.core.proxy.command;

import de.jokileda.core.proxy.Core;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ChatClearCommand extends Command {

  private final Core core;

  public ChatClearCommand(Core core) {
    super("cc", "jokileda.command.chatclear", "chatclear", "machweg");
    this.core = core;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if(!(sender instanceof ProxiedPlayer player))
      return;
    if(!player.hasPermission("jokileda.command.chatclear"))
      return;
    for (int i = 0; i < 100; i++) {
      player.sendMessage(new TextComponent(""));
    }
    player.sendMessage(new TextComponent(core.getMessages().getString("core.command.chatclear", "GUT")));
  }
}
