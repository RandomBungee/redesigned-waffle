package de.jokileda.core.api.spigot.sidebar;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public interface Sidebar {

  void createScoreboard(Player player, String title);

  void setScore(Player player, String text, int score);

  void remove(Player player);

  Scoreboard getScoreboard(Player player);
}
