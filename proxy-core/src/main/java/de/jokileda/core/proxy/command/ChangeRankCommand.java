package de.jokileda.core.proxy.command;

import de.jokileda.core.api.shared.uuid.UUIDFetcher;
import de.jokileda.core.proxy.Core;
import java.util.ArrayList;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.List;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ChangeRankCommand extends Command implements TabExecutor {

    private final Core core;

    public ChangeRankCommand(Core core) {
        super("changerank");
        this.core = core;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player))
            return;
        if (!player.hasPermission("command.changerank")) {
            player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.noperms", "Keine Rechte!")));
            return;
        }
        if (args.length == 0) {
            sendHelpMap(player);
            return;
        }
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("groups")) {
                sendGroups(player);
                return;
            }
            String playerName = args[0];
            String playerUuid = UUIDFetcher.getUUID(playerName).toString();
            if(playerUuid == null) {
                player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.noplayer", "Player no")));
                return;
            }
            if(!core.getPermissionUser().userExist(playerUuid)) {
                player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.noplayerindatabase", "Player no")));
                return;
            }
            sendPlayerInformation(player, playerUuid);
            return;
        }
        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("addGroup")) {
                String playerName = args[1];
                String group = args[2];
                String playerUuid = UUIDFetcher.getUUID(playerName).toString();
                if(playerUuid == null) {
                    player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.noplayer", "Player no")));
                    return;
                }
                if(!core.getPermissionUser().userExist(playerUuid)) {
                    player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.noplayerindatabase", "Player no")));
                    return;
                }
                if(!core.getPermissionGroup().groupExist(group)) {
                    player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.nogroup", "Group no")));
                    return;
                }
                core.getPermissionUser().updateUser(playerUuid, group, -1, true);
                player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.addgroup",
                    "%addedgroup% %player%",
                    "%addedgroup%",
                    group,
                    "%player%",
                    playerName)));
                return;
            }
            if(args[0].equalsIgnoreCase("addPermission")) {
                String playerName = args[1];
                String permission = args[2];
                String playerUuid = UUIDFetcher.getUUID(playerName).toString();
                if(playerUuid == null) {
                    player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.noplayer", "Player no")));
                    return;
                }
                if(!core.getPermissionUser().userExist(playerUuid)) {
                    player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.noplayerindatabase", "Player no")));
                    return;
                }
                core.getPermissionUser().updateUser(playerUuid, permission);
                player.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.addpermission",
                    "%permission% %player%",
                    "%permission%",
                    permission,
                    "%player%",
                    playerName)));
            }
            sendHelpMap(player);
            return;
        }
        sendHelpMap(player);
    }

    private void sendHelpMap(ProxiedPlayer proxiedPlayer) {
        proxiedPlayer.sendMessage(new TextComponent(core.getMessages().getString("core.command.changerank.help", "HelpMap")));
    }

    private void sendGroups(ProxiedPlayer proxiedPlayer) {
        String group = turnListIntoOneString(core.getPermissionGroup().allGroups());
        proxiedPlayer.sendMessage(new TextComponent(
                core
                .getMessages()
                .getString("core.command.changerank.groups",
                "%groups%",
                "%groups%",
                    group)));
    }

    private void sendPlayerInformation(ProxiedPlayer proxiedPlayer, String targetUuid) {
        String playerGroup = turnListIntoOneString(core.getPermissionUser().getUserLists(targetUuid, "groups"));
        String playerPerms = turnListIntoOneString(core.getPermissionUser().getUserLists(targetUuid, "permissions"));
        proxiedPlayer.sendMessage(new TextComponent(
                core
                .getMessages()
                .getString("core.command.changerank.playerInfo",
                                "%playergroups% %playerperms%",
                                "%playergroups%",
                    playerGroup,
                    "%playerperms%",
                    playerPerms)));
    }

    private String turnListIntoOneString(List<String> permissions) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String permission : permissions) {
            stringBuilder.append(permission);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
            core.getProxy().getPlayers().forEach(all -> completions.add(all.getName()));
            completions.add("addGroup");
            completions.add("addPermission");
            completions.add("groups");
        } else if(args.length == 2) {
            core.getProxy().getPlayers().forEach(all -> completions.add(all.getName()));
        } else if(args.length == 3) {
            completions.addAll(core.getPermissionGroup().allGroups());
        }
        return completions;
    }
}