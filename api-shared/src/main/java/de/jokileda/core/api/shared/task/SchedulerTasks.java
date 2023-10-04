package de.jokileda.core.api.shared.task;

public interface SchedulerTasks {

  void start();

  void stop();

  boolean isRunning();
}
