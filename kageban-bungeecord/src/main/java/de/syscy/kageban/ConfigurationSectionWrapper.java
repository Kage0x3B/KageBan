package de.syscy.kageban;

import de.syscy.kageban.common.config.KBConfigurationSection;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.config.Configuration;

import java.util.List;

@RequiredArgsConstructor
public class ConfigurationSectionWrapper implements KBConfigurationSection {
	private final Configuration configSection;

	@Override
	public String getString(String path, String def) {
		return configSection.getString(path, def);
	}

	@Override
	public int getInt(String path, int def) {
		return configSection.getInt(path, def);
	}

	@Override
	public boolean getBoolean(String path, boolean def) {
		return configSection.getBoolean(path, def);
	}

	@Override
	public List<?> getList(String path, List<String> def) {
		return configSection.getList(path, def);
	}

	@Override
	public KBConfigurationSection getSection(String path) {
		return new ConfigurationSectionWrapper(configSection.getSection(path));
	}
}