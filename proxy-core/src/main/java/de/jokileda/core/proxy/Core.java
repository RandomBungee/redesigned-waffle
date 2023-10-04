package de.jokileda.core.proxy;

import de.jokileda.core.api.proxy.broadcast.AutoBroadCast;
import de.jokileda.core.api.proxy.config.ProxyConfigManager;
import de.jokileda.core.api.shared.annotaion.PluginInfo;
import de.jokileda.core.api.shared.permission.IPermissionGroup;
import de.jokileda.core.api.shared.permission.IPermissionUser;
import de.jokileda.core.api.shared.util.PluginType;
import de.jokileda.core.api.proxy.AbstractProxyPlugin;
import de.jokileda.core.api.proxy.punish.Punish;
import de.jokileda.core.proxy.autobroadcast.AutoBroadCastProvider;
import de.jokileda.core.proxy.command.ChatClearCommand;
import de.jokileda.core.proxy.command.HelpCommand;
import de.jokileda.core.proxy.command.KickCommand;
import de.jokileda.core.proxy.command.PingCommand;
import de.jokileda.core.proxy.command.PrayCommand;
import de.jokileda.core.proxy.command.SentToLobbyCommand;
import de.jokileda.core.proxy.command.ServerCommand;
import de.jokileda.core.proxy.command.UnbanCommand;
import de.jokileda.core.proxy.listener.PermissionListener;
import de.jokileda.core.proxy.punish.PunishProvider;
import de.jokileda.core.proxy.punish.PunishProviderExtension;
import de.jokileda.core.proxy.command.ChangeRankCommand;
import de.jokileda.core.proxy.command.PunishCommand;
import de.jokileda.core.proxy.listener.ChatListener;
import de.jokileda.core.proxy.listener.JoinListener;
import de.jokileda.shared.SharedLoader;
import de.jokileda.shared.permission.PermissionGroupHandler;
import de.jokileda.shared.permission.PermissionUserHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@PluginInfo(name = "Core", authors = "RandomBungee", version = {0,0,0}, pluginType = PluginType.PROXY)
public class Core extends AbstractProxyPlugin {

    private Punish punish;
    private IPermissionUser permissionUser;
    private IPermissionGroup permissionGroup;
    private AutoBroadCast autoBroadCast;
    private PunishProviderExtension punishProviderExtension;
    private ProxyConfigManager messages = new ProxyConfigManager("core", "messages");
    private ProxyConfigManager coreConfig = new ProxyConfigManager("core", "core");

    @Override
    public void load() {
        SharedLoader sharedLoader = new SharedLoader();
        sharedLoader.loadShadedApi(this);
        punish = new PunishProvider(sharedLoader, this);
        punishProviderExtension = new PunishProviderExtension(this);
        permissionUser = new PermissionUserHandler(sharedLoader);
        permissionGroup = new PermissionGroupHandler(sharedLoader);
        autoBroadCast = new AutoBroadCastProvider(this);
    }

    @Override
    public void enable() {
        getProxy().getPluginManager().registerCommand(this, new PunishCommand(this));
        getProxy().getPluginManager().registerCommand(this, new ChangeRankCommand(this));
        getProxy().getPluginManager().registerCommand(this, new KickCommand(this));
        getProxy().getPluginManager().registerCommand(this, new ChatClearCommand(this));
        getProxy().getPluginManager().registerCommand(this, new HelpCommand(this));
        getProxy().getPluginManager().registerCommand(this, new PingCommand(this));
        getProxy().getPluginManager().registerCommand(this, new SentToLobbyCommand(this));
        getProxy().getPluginManager().registerCommand(this, new PrayCommand(this));
        getProxy().getPluginManager().registerCommand(this, new ServerCommand(this));
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand(this));
        getProxy().getPluginManager().registerListener(this, new JoinListener(this));
        getProxy().getPluginManager().registerListener(this, new ChatListener(this));
        getProxy().getPluginManager().registerListener(this, new PermissionListener(this));
    }

    @Override
    public void disable() {}
}
