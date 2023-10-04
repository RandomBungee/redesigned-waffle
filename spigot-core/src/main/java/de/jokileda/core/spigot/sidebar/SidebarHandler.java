package de.jokileda.core.spigot.sidebar;

import de.jokileda.core.api.spigot.sidebar.Sidebar;
import de.jokileda.core.spigot.Core;
import java.nio.charset.CoderResult;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class SidebarHandler implements Sidebar {

  private final Core core;
  private final Scoreboard scoreboard;
  private Map<Player, Scoreboard> playerScoreboards = new HashMap<>();

  public SidebarHandler(Core core) {
    this.core = core;
    scoreboard = core.getTabScore();
  }

  @Override
  public void createScoreboard(Player player, String title) {
    Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy", title);
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    player.setScoreboard(scoreboard);
    playerScoreboards.put(player, scoreboard);
  }

  @Override
  public void setScore(Player player, String text, int score) {
    Scoreboard scoreboard = playerScoreboards.get(player);
    if (scoreboard == null) {
      return;
    }

    Objective objective = scoreboard.getObjective("scoreboard");
    Score scoreboardScore = objective.getScore(text);
    scoreboardScore.setScore(score);
  }

  @Override
  public void remove(Player player) {
    player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    playerScoreboards.remove(player);
  }

  @Override
  public Scoreboard getScoreboard(Player player) {
    return playerScoreboards.get(player);
  }
}
