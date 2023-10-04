package de.jokileda.core.proxy.punish;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public enum TimeProvider {
  SECONDS(1, "s"),
  MINUTE(60, "m"),
  HOUR(60*60, "h"),
  DAY(24*60*60, "d"),
  WEEK(7*24*60*60, "w");

  private final int second;
  private final String shortCut;

  TimeProvider(int second, String shortCut) {
    this.second = second;
    this.shortCut = shortCut;
  }

  public static List<String> unitAsString() {
    List<String> units = new ArrayList<>();
    for(TimeProvider provider : TimeProvider.values()) {
      units.add(provider.getShortCut());
    }
    return units;
  }

  public static TimeProvider getUnit(String unit) {
    for(TimeProvider provider : TimeProvider.values()) {
      if(provider.getShortCut().equalsIgnoreCase(unit))
        return provider;
    }
    return null;
  }
}
