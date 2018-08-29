package de.syscy.kageban.common.storage.dao;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import de.syscy.kageban.common.KageBanPlugin;
import de.syscy.kageban.common.config.KBConfigurationSection;
import de.syscy.kageban.common.punishment.Punishment;
import de.syscy.kageban.common.punishment.PunishmentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RethinkDBDao extends AbstractPunishmentDao {
	private static final RethinkDB r = RethinkDB.r;

	private final String USER_PUNISHMENTS_TABLE;
	private final String PUNISHMENTS_HISTORY_TABLE;

	private final KBConfigurationSection databaseSection;

	private Connection connection = null;

	public RethinkDBDao(KageBanPlugin plugin, String prefix, KBConfigurationSection databaseSection) {
		super(plugin, "RethinkDB");

		prefix += prefix.endsWith("_") ? "" : "_";
		USER_PUNISHMENTS_TABLE = prefix + "user_punishments";
		PUNISHMENTS_HISTORY_TABLE = prefix + "punishment_history";

		this.databaseSection = databaseSection;
	}

	@Override
	public void init() {
		if(getConnection() == null) {
			throw new RuntimeException("Could not connect to RethinkDB database!");
		}
	}

	public Connection getConnection() {
		if(connection == null || !connection.isOpen()) {
			connection = createConnection();
		}

		return connection;
	}

	private Connection createConnection() {
		String hostname = databaseSection.getString("address", "127.0.0.1");
		int port = databaseSection.getInt("port", 28015); //Default port
		String databaseName = databaseSection.getString("databaseName", "kageban");

		if(hostname.contains(":")) {
			String[] hostnameSplit = hostname.split(":", 2);
			hostname = hostnameSplit[0];
			port = Integer.parseInt(hostnameSplit[1]);
		}

		Connection.Builder connectionBuilder = r.connection();
		connectionBuilder.hostname(hostname).port(port).db(databaseName);

		String username = databaseSection.getString("username", null);
		String password = databaseSection.getString("password", null);

		if(username != null && !username.isEmpty()) {
			connectionBuilder.user(username, password);
		}

		return connectionBuilder.connect();
	}

	@Override
	public void shutdown() {
		if(connection != null && connection.isOpen()) {
			connection.close();
		}
	}

	@Override
	public List<Punishment> loadPunishments(UUID playerId) {
		List<Punishment> punishments = new ArrayList<>();

		Cursor<Map<String, Object>> punishmentsCursor = r.table(USER_PUNISHMENTS_TABLE).filter(r.hashMap("affectedId", playerId.toString())).run(getConnection());

		if(punishmentsCursor != null) {
			for(Map<String, Object> punishmentDataMap : punishmentsCursor) {
				UUID punishmentId = UUID.fromString((String) punishmentDataMap.get("uuid"));
				PunishmentType type = PunishmentType.fromString((String) punishmentDataMap.get("type"));
				long issuedTime = (long) punishmentDataMap.get("issuedTime");
				long endTime = (long) punishmentDataMap.get("endTime");
				UUID issuerId = UUID.fromString((String) punishmentDataMap.get("issuerId"));
				String reason = (String) punishmentDataMap.get("reason");
				reason = reason.equals("empty") ? null : reason;

				punishments.add(new Punishment(punishmentId, playerId, type, issuedTime, endTime, issuerId, reason));
			}
		}

		return punishments;
	}

	@Override
	public void savePunishment(Punishment punishment) {
		MapObject insertData = r.hashMap("uuid", punishment.getPunishmentId().toString());
		insertData.with("affectedId", punishment.getAffectedId().toString());
		insertData.with("type", punishment.getType().name());
		insertData.with("issuedTime", punishment.getIssuedTime());
		insertData.with("endTime", punishment.getEndTime());
		insertData.with("issuerId", punishment.getIssuerId().toString());
		insertData.with("reason", punishment.getReason().orElse("empty"));

		if(punishment.getType() != PunishmentType.KICK) {
			Object result = r.table(USER_PUNISHMENTS_TABLE).insert(insertData).run(getConnection());
			System.out.println(result);
		}

		r.table(PUNISHMENTS_HISTORY_TABLE).insert(insertData).run(getConnection());
	}

	@Override
	public void removePunishment(Punishment punishment) {
		r.table(USER_PUNISHMENTS_TABLE).filter(r.hashMap("uuid", punishment.getPunishmentId().toString())).delete().runNoReply(getConnection());
	}

	@Override
	public void performMaintenanceTasks() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}