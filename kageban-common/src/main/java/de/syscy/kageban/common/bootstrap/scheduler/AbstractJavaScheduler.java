package de.syscy.kageban.common.bootstrap.scheduler;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.*;

public abstract class AbstractJavaScheduler implements SchedulerAdapter {
	private final ScheduledExecutorService asyncExecutor = new AsyncExecutor();

	@Override
	public Executor async() {
		return asyncExecutor;
	}

	@Override
	public SchedulerTask asyncLater(Runnable task, long delay, TimeUnit unit) {
		ScheduledFuture<?> future = asyncExecutor.schedule(new WrappedRunnable(task), delay, unit);

		return () -> future.cancel(false);
	}

	@Override
	public SchedulerTask asyncRepeating(Runnable task, long interval, TimeUnit unit) {
		ScheduledFuture<?> future = asyncExecutor.scheduleAtFixedRate(new WrappedRunnable(task), interval, interval, unit);

		return () -> future.cancel(false);
	}

	@Override
	public void shutdown() {
		this.asyncExecutor.shutdown();

		try {
			this.asyncExecutor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	@RequiredArgsConstructor
	private static final class WrappedRunnable implements Runnable {
		private final Runnable delegate;

		@Override
		public void run() {
			try {
				delegate.run();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static final class AsyncExecutor extends ScheduledThreadPoolExecutor {
		AsyncExecutor() {
			super(4);
		}

		@Override
		public void execute(Runnable command) {
			super.execute(new WrappedRunnable(command));
		}
	}
}