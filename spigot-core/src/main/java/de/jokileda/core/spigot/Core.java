package de.jokileda.core.spigot;

import de.jokileda.core.api.shared.annotaion.PluginInfo;
import de.jokileda.core.api.shared.permission.IPermissionGroup;
import de.jokileda.core.api.shared.permission.IPermissionUser;
import de.jokileda.core.api.shared.util.PluginType;
import de.jokileda.core.api.spigot.AbstractSpigotPlugin;
import de.jokileda.core.api.spigot.config.SpigotConfigManager;
import de.jokileda.core.api.spigot.sidebar.Sidebar;
import de.jokileda.core.spigot.command.LocationCommand;
import de.jokileda.core.spigot.listener.ChatListener;
import de.jokileda.core.spigot.listener.JoinListener;
import de.jokileda.core.spigot.listener.PermissionListener;
import de.jokileda.core.spigot.listener.QuitListener;
import de.jokileda.core.spigot.sidebar.SidebarHandler;
import de.jokileda.shared.SharedLoader;
import de.jokileda.shared.permission.PermissionGroupHandler;
import de.jokileda.shared.permission.PermissionUserHandler;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

@PluginInfo(name = "core", authors = "RandomBungee", version = {0,0,0}, pluginType = PluginType.SPIGOT)
public class Core extends AbstractSpigotPlugin {

  @Getter
  private final SpigotConfigManager configManager = new SpigotConfigManager(getAnnotation().name(), "core");
  @Getter
  private final SpigotConfigManager messageManager = new SpigotConfigManager(getAnnotation().name(), "message");
  @Getter
  private IPermissionUser permissionUser;
  @Getter
  private IPermissionGroup permissionGroup;
  @Getter
  private Scoreboard tabScore;
  @Getter
  private Sidebar sidebar;

  @Override
  public void load() {
    SharedLoader sharedLoader = new SharedLoader();
    sharedLoader.loadShadedApi(this);
    permissionUser = new PermissionUserHandler(sharedLoader);
    permissionGroup = new PermissionGroupHandler(sharedLoader);
  }

  @Override
  public void enable() {
    getCommand("location").setExecutor(new LocationCommand(this));
    getServer().getPluginManager().registerEvents(new PermissionListener(this), this);
    getServer().getPluginManager().registerEvents(new ChatListener(this, getPermissionUser(), getPermissionGroup()), this);
    getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    getServer().getPluginManager().registerEvents(new QuitListener(this), this);
    this.tabScore = Bukkit.getScoreboardManager().getNewScoreboard();
    sidebar = new SidebarHandler(this);
    registerTeams();
  }

  private void registerTeams() {
    getPermissionGroup().allGroups().forEach(s -> {
      tabScore.registerNewTeam(getPermissionGroup().getTabTeam(s));
      tabScore.getTeam(getPermissionGroup().getTabTeam(s)).setPrefix(getPermissionGroup().getGroupPrefix(s));
    });
  }

  public void setPrefix(Player player) {
    getPermissionUser().getUserLists(player.getUniqueId().toString(), "groups").forEach(s -> {
      tabScore.getTeam(getPermissionGroup().getTabTeam(s)).addEntry(player.getName());
    });
    Bukkit.getOnlinePlayers().forEach(all -> {
      all.setScoreboard(tabScore);
    });
  }

  @Override
  public void disable() {

  }
}

