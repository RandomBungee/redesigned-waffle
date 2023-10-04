package de.jokileda.core.proxy.command;

import de.jokileda.core.api.proxy.punish.PunishEntry;
import de.jokileda.core.api.proxy.punish.PunishType;
import de.jokileda.core.api.shared.uuid.UUIDFetcher;
import de.jokileda.core.api.shared.uuid.UUIDTypeAdapter;
import de.jokileda.core.proxy.Core;
import de.jokileda.core.proxy.punish.TimeProvider;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class PunishCommand extends Command implements TabExecutor {

    private final Core core;
    private final List<String> reasons;

    public PunishCommand(Core core) {
        super("punish", "jokileda.command.punish", "ban", "fuckoff");
        this.core = core;
        this.reasons = core.getCoreConfig()
            .getStringList("core.punish.reason",
                Collections.singletonList("hacks"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer player))
            return;
       if(!player.hasPermission("jokileda.command.punish"))
           return;
        if(args.length == 2) {
            UUID targetPlayer = UUIDFetcher.getUUID(args[0]);
            String reason = args[1];
            if(!reasons.contains(reason)) {
                player.sendMessage(new TextComponent(core.getMessages().getString("core.command.punish.notreason", "Nix Grund geben!")));
                return;
            }
            PunishEntry punishEntry = punishPlayer(
                getPunishTypeFromReason(reason), player.getName(), reason,
                targetPlayer.toString(), getTimeFromReason(reason));
            core.getPunish().punishPlayer(punishEntry, getPunishTypeFromReason(reason), true);
            core.getPunishProviderExtension().punishPlayer(targetPlayer, reason, getTimeFromReason(reason), getPunishTypeFromReason(reason));
            return;
        }
        if(args.length == 5) {
            if(!player.hasPermission("jokileda.command.punish.custom"))
                return;
            UUID targetPlayer = UUIDFetcher.getUUID(args[0]);
            String reason = args[1];
            int time = 0;
            String unit = args[3];
            String type = args[4];
            try {
                time = Integer.parseInt(args[2]);
            } catch (NumberFormatException exception) {
                player.sendMessage(new TextComponent(core.getMessages().getString("core.command.punish.validtime", "NIX ZAHL DU DEPP!")));
                return;
            }
            List<String> listUnit = TimeProvider.unitAsString();
            if(!listUnit.contains(unit)) {
                player.sendMessage(new TextComponent(core.getMessages().getString("core.command.punish.notunit", "NIX UNIT DU DEPP!")));
                return;
            }
            TimeProvider provider = TimeProvider.getUnit(unit);
            long finalTime = (long) time * provider.getSecond();
            PunishEntry punishEntry = punishPlayer(
                PunishType.valueOf(type), player.getName(), reason,
                targetPlayer.toString(), finalTime);
            core.getPunish().punishPlayer(punishEntry, PunishType.valueOf(type), true);
            core.getPunishProviderExtension().punishPlayer(targetPlayer, reason, finalTime, PunishType.valueOf(type));
            return;
        }
        sendHelpMap(player);
    }

    private void sendHelpMap(ProxiedPlayer player) {
        player.sendMessage(new TextComponent(core.getMessages().getString("core.command.punish.help", "HelpMap")));
    }

    private PunishEntry punishPlayer(PunishType punishType, String author, String reason, String uuid, long time) {
        return new PunishEntry()
            .newBuilder()
            .setPunishType(punishType)
            .setAuthor(author)
            .setReason(reason)
            .setUuid(uuid)
            .setTime(time)
            .build();
    }

    private long getTimeFromReason(String reason) {
        return core.getCoreConfig().getLong("core.punish.time." + reason, 100);
    }

    private PunishType getPunishTypeFromReason(String reason) {
        return PunishType.valueOf(core.getCoreConfig().getString("core.punish.type." + reason, "BAN"));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
            core.getProxy().getPlayers().forEach(all -> completions.add(all.getName()));
        } else if(args.length == 2) {
            completions.addAll(reasons);
        }
        return completions;
    }
}
