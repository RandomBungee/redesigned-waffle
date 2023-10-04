package de.jokileda.core.proxy.punish;

import de.jokileda.core.api.proxy.AbstractProxyPlugin;
import de.jokileda.core.api.proxy.config.ProxyConfigManager;
import de.jokileda.core.api.proxy.punish.PunishType;
import javax.annotation.Nullable;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class PunishProviderExtension {

    private final AbstractProxyPlugin abstractProxyPlugin;
    private final ProxyConfigManager configManager;

    public PunishProviderExtension(AbstractProxyPlugin abstractProxyPlugin) {
        this.abstractProxyPlugin = abstractProxyPlugin;
        this.configManager = new ProxyConfigManager("core", "punishMessage");
    }

    public void punishPlayer(UUID uniqueId, String reason, long time, PunishType punishType) {
        if (abstractProxyPlugin.getProxy().getPlayer(uniqueId) == null)
            return;
        ProxiedPlayer proxiedPlayer = abstractProxyPlugin.getProxy().getPlayer(uniqueId);
        if (punishType == PunishType.BAN || punishType == PunishType.KICK) {
            proxiedPlayer.disconnect(new TextComponent(sendPlayerDisconnectScreen(reason, punishType, time)));
        } else if(punishType == PunishType.MUTE) {
            proxiedPlayer.sendMessage(new TextComponent(sendPlayerDisconnectScreen(reason, punishType, time)));
        }
    }

    public String sendPlayerDisconnectScreen(String reason, PunishType punishType, long time) {
        String timeString = convertMillis(time);
        String disconnectScreen = "";
        if (punishType == PunishType.KICK) {
            disconnectScreen = configManager.getString("core.punish.kickScreen", "Du wurdest gekickt %reason%", "reason", reason);
        } else if (punishType == PunishType.BAN) {
            disconnectScreen = configManager.getString(
                    "core.punish.banScreen",
                    "Du wwurdest gebannt %reason% %time%", "reason", reason, "%time%", timeString);
        }  else if (punishType == PunishType.MUTE) {
            disconnectScreen = configManager.getString(
                    "core.punish.mute.text",
                    "Du wwurdest gebannt %reason% %time%", "reason", reason, "%time%", timeString);
        }

        return disconnectScreen;
    }

    public String convertMillis(long time) {
        long current = System.currentTimeMillis();
        if (time == -1) {
            return configManager.getString("core.punish.time.permanent", "§4Permanent");
        }
        long millis = time - current;
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        long weeks = 0;
        while (millis > 1000) {
            millis -= 1000;
            seconds++;
        }
        while (seconds > 59) {
            seconds -= 59;
            minutes++;
        }
        while (minutes > 59) {
            minutes -= 60;
            hours++;
        }
        while (hours > 23) {
            hours -= 24;
            days++;
        }
        while (days > 6) {
            days -= 7;
            weeks++;
        }
        return configManager.getString("core.punish.time.convertedTime",
                "%weeks% %days% %hours% %minutes% %seconds%",
                "%weeks%",
            String.valueOf(weeks),
            "%days%",
            String.valueOf(days),
            "%hours%",
            String.valueOf(hours),
            "%minutes%",
            String.valueOf(minutes),
            "%seconds%",
            String.valueOf(seconds));
    }
}
