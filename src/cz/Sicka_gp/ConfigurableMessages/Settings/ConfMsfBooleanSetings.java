package cz.Sicka_gp.ConfigurableMessages.Settings;

import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessages;

public class ConfMsfBooleanSetings {
	ConfigurableMessages plugin;
	
	public ConfMsfBooleanSetings(ConfigurableMessages instance){
		this.plugin = instance;
	}
	
	public boolean isSidebarEnable(){
		return ConfigurableMessages.getPlugin().getConfig().getBoolean("Sidebar.enable");
	}
	public boolean isMessageEnable() {
		return ConfigurableMessages.getPlugin().getConfig().getBoolean("Messages.enable");
	}
	public boolean isMessageGroupEnable() {
		return ConfigurableMessages.getPlugin().getConfig().getBoolean("Messages.enableGroups");
	}
	public boolean isFirstMessageEnable() {
		return ConfigurableMessages.getPlugin().getConfig().getBoolean("Newbies.First-message-enable");
	}
	public boolean isKitsEnable() {
		return ConfigurableMessages.getPlugin().getConfig().getBoolean("Newbies.Kits");
	}
}
