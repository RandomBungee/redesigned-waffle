package de.jokileda.core.proxy.command;

import de.jokileda.core.proxy.Core;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class PrayCommand extends Command {

  private final Core core;

  public PrayCommand(Core core) {
    super("pray");
    this.core = core;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    sender.sendMessage(new TextComponent(core.getMessages().getString("core.command.pray", "NUTTE")));
  }
}
