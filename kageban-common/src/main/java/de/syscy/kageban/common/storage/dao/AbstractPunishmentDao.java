package de.syscy.kageban.common.storage.dao;

import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.punishment.Punishment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractPunishmentDao {
    protected final @Getter KageBanPlugin plugin;
    public final @Getter String name;

    public abstract void init() throws Exception;

    public abstract void shutdown();

    public abstract List<Punishment> loadPunishments(UUID playerId);

    public abstract void savePunishment(Punishment punishment);

    public abstract void removePunishment(Punishment punishment);

    public abstract void performMaintenanceTasks();

}