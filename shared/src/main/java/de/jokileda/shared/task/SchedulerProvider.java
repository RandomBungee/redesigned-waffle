package de.jokileda.shared.task;

import de.jokileda.core.api.shared.IPlugin;
import de.jokileda.core.api.shared.task.Scheduler;
import de.jokileda.core.api.shared.task.SchedulerTasks;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SchedulerProvider implements Scheduler {

  private final Executor executor = Executors.newFixedThreadPool(128);
  private final IPlugin plugin;

  @Override
  public SchedulerTasks schedule(IPlugin plugin, Runnable run, long time,
      TimeUnit unit) {
    final SchedulerTasks task = new SchedulerTasks() {
      private boolean running = true;

      @Override
      public void start() {
        async(plugin, () -> {
          while (running) {
            run.run();
            try {
              Thread.sleep(unit.toMillis(time));
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        });
      }

      @Override
      public void stop() {
        this.running = false;
      }

      @Override
      public boolean isRunning() {
        return this.running;
      }
    };
    task.start();
    return task;
  }

  @Override
  public SchedulerTasks runLater(IPlugin plugin, Runnable run, long time,
      TimeUnit unit) {
    final SchedulerTasks task = new SchedulerTasks() {
      private boolean running = true;

      @Override
      public void start() {
        async(plugin, () -> {
          try {
            Thread.sleep(unit.toMillis(time));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          if (this.running) run.run();
          this.running = false;
        });
      }

      @Override
      public void stop() {
        this.running = false;
      }

      @Override
      public boolean isRunning() {
        return this.running;
      }
    };
    task.start();
    return task;
  }

  @Override
  public void async(IPlugin plugin, Runnable run) {
    executor.execute(run);
  }

  @Override
  public void async(Runnable runnable) {
    this.async(plugin, runnable);
  }
}
