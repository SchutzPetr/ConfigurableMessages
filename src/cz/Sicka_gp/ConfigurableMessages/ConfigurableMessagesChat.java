package cz.Sicka_gp.ConfigurableMessages;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ConfigurableMessagesChat implements Listener {
	
	private ConfigurableMessages plugin;

	public ConfigurableMessagesChat(ConfigurableMessages m){
		  this.plugin = m;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		String message = event.getMessage(); 
	    List<String> badWords = this.plugin.getbadwords().getStringList("badwords");
	    List<String> ips = this.plugin.getbadwords().getStringList("IP");
	    //badwords
	    for(String word : badWords){
	    	if(message.toLowerCase().contains(word)){
	    		message = message.replace(word, "***");
	    		player.chat(message);
	    		event.setCancelled(true);
	    		break;
	    	}
	    }
	    for(String ip : ips){
	    	if(message.toLowerCase().contains(ip)){
	    		for(Player op : Bukkit.getOnlinePlayers()){
	    			if(op.isOp()){
	    				op.sendMessage(ChatColor.DARK_RED + player.getName() + ": " + message);
	    			}
	    		}
	    		event.setCancelled(true);
	    		break;
	    	}
	    }
	}

}
