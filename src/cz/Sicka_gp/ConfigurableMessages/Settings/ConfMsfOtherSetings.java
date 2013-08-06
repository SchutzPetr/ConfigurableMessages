package cz.Sicka_gp.ConfigurableMessages.Settings;

import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessages;

public class ConfMsfOtherSetings {
	ConfigurableMessages plugin;
	static String string;
	
	public ConfMsfOtherSetings(ConfigurableMessages instance){
		this.plugin = instance;
	}
	
	public int getSidebarUpdateInt(){
		return ConfigurableMessages.getPlugin().getConfig().getInt("Sidebar.Update", 5);
	}
}
