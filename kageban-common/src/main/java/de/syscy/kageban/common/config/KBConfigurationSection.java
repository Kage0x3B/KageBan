package de.syscy.kageban.common.config;

import java.util.List;

public interface KBConfigurationSection {
	String getString(String path, String def);

	int getInt(String path, int def);

	boolean getBoolean(String path, boolean def);

	List<?> getList(String path, List<String> def);

	KBConfigurationSection getSection(String path);
}