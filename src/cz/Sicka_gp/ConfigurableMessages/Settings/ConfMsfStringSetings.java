package cz.Sicka_gp.ConfigurableMessages.Settings;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessages;

public class ConfMsfStringSetings{
	ConfigurableMessages plugin;
	public static String message = null;
	
	public ConfMsfStringSetings(ConfigurableMessages instance){
		this.plugin = instance;
	}
	
	public static String replacer(final Player p, String path){
		message = ConfigurableMessages.getPlugin().getConfig().getString(path);
		if(message == null){
			message = null;
		}else{
			String name = p.getName();
			message = message.replace("{NAME}", name);
			if(ConfigurableMessages.chat != null){
				String prefix = ConfigurableMessages.chat.getPlayerPrefix(p);
				prefix = prefix.concat(name);
				name = prefix;
				message = message.replace("{DISPLAYNAME}", prefix);
			}
			message = message.replace("{WORLD}", p.getWorld().getName());
			message = message.replace("{BIOME}", p.getLocation().getBlock().getBiome().toString());
			message = message.replace("{ONLINE}", Bukkit.getServer().getOnlinePlayers().length + "");
			message = message.replace("{MAX_ONLINE}", Bukkit.getServer().getMaxPlayers() + "");
			message = message.replace("{SERVER_NAME}", Bukkit.getServer().getName());
			if(ConfigurableMessages.getPlugin().geo != null){
				message = message.replace("{COUNTRY}", ConfigurableMessages.getPlugin().geo.getCountry(p.getAddress().getAddress()).getName());
			}
			message = ChatColor.translateAlternateColorCodes("&".charAt(0), message);
		}
		return message;
	}
	
}
