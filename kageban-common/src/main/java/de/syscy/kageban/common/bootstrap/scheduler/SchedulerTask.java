package de.syscy.kageban.common.bootstrap.scheduler;

public interface SchedulerTask {
	/**
	 * Cancels the task.
	 */
	void cancel();
}