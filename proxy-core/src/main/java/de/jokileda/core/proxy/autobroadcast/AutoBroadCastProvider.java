package de.jokileda.core.proxy.autobroadcast;

import de.jokileda.core.api.proxy.broadcast.AutoBroadCast;
import de.jokileda.core.api.shared.task.Scheduler;
import de.jokileda.core.proxy.Core;
import de.jokileda.shared.task.SchedulerProvider;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.md_5.bungee.api.chat.TextComponent;

public class AutoBroadCastProvider implements AutoBroadCast {

  private final Core core;
  private final Map<UUID, AtomicInteger> messageId;

  public AutoBroadCastProvider(Core core) {
    this.core = core;
    Scheduler scheduler = new SchedulerProvider(core);
    this.messageId = new HashMap<>();
    scheduler.schedule(core, this::doBroadCast, 10L, TimeUnit.MINUTES);
  }

  private void doBroadCast() {
    List<Integer> messageAmount = core.getMessages()
        .getIntegerList("core.automessage.amount", Arrays.asList(0, 1, 2));
    core.getProxy().getPlayers().forEach(players -> {
      int id = messageId.computeIfAbsent(players.getUniqueId(), (i) -> new AtomicInteger()).getAndIncrement();
      if(!messageAmount.contains(id)) {
       id = 0;
       messageId.get(players.getUniqueId()).set(0);
      }
      sendAutoBroadCast(core.getMessages().getString("core.automessage.string." + id, "HS"));
    });
  }

  @Override
  public void sendAutoBroadCast(String message) {
    core.getProxy().getPlayers().forEach(all -> all.sendMessage(new TextComponent(message)));
  }
}
