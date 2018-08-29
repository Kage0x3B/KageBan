package de.syscy.kageban;

import de.syscy.kageban.common.bootstrap.scheduler.AbstractJavaScheduler;
import de.syscy.kageban.common.bootstrap.scheduler.SchedulerAdapter;

import java.util.concurrent.Executor;

public class BukkitSchedulerAdapter extends AbstractJavaScheduler implements SchedulerAdapter {
    private final Executor sync;

    public BukkitSchedulerAdapter(KageBanSpigot bootstrap) {
        this.sync = r -> bootstrap.getServer().getScheduler().scheduleSyncDelayedTask(bootstrap, r);
    }

    @Override
    public Executor sync() {
        return this.sync;
    }
}