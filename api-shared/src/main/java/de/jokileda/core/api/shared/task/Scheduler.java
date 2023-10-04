package de.jokileda.core.api.shared.task;

import de.jokileda.core.api.shared.IPlugin;
import java.util.concurrent.TimeUnit;

public interface Scheduler {

  SchedulerTasks schedule(IPlugin plugin, Runnable run, long time, TimeUnit unit);

  SchedulerTasks runLater(IPlugin plugin, Runnable run, long time, TimeUnit unit);

  void async(IPlugin plugin, Runnable run);

  void async(Runnable runnable);
}
