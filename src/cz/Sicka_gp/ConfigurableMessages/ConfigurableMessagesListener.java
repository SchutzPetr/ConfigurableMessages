package cz.Sicka_gp.ConfigurableMessages;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import cz.Sicka_gp.ConfigurableMessages.Settings.ConfMsfBooleanSetings;
import cz.Sicka_gp.ConfigurableMessages.Settings.ConfMsfStringSetings;

public class ConfigurableMessagesListener implements Listener{
	public static ConfigurableMessages plugin;
	public ConfigurableMessagesListener(ConfigurableMessages instance) {
		plugin = instance;
	}
	ConfMsfBooleanSetings s = new ConfMsfBooleanSetings(ConfigurableMessages.getPlugin());
	ConfMsfStringSetings str = new ConfMsfStringSetings(ConfigurableMessages.getPlugin());

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void  onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(s.isSidebarEnable()){
			ConfigurableMessages.getPlugin().getScoreboardManager().CreateSidebar(p);
		}
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(p.getName());
		if(!(offlinePlayer.hasPlayedBefore())){
			if(s.isFirstMessageEnable()){
				for(Player pl : Bukkit.getServer().getOnlinePlayers()){
					if(pl.canSee(p)){
						String message = ConfMsfStringSetings.replacer(p, "Newbies.First-message");
						pl.sendMessage(message);
					}
				}
			}
			if(s.isKitsEnable()){
				setKits(p);
			}
		}
		if(s.isMessageEnable()){
			if(s.isMessageGroupEnable() && ConfigurableMessages.chat != null && ConfigurableMessages.permission != null){
				for(Player pl : Bukkit.getServer().getOnlinePlayers()){
					if(pl.canSee(p)){
						String jmessage = "Messages.Groups." + ConfigurableMessages.permission.getPrimaryGroup(p) + ".join-message";
						String message = ConfMsfStringSetings.replacer(p, jmessage);
						if(message != null){
							pl.sendMessage(message);
						}
					}
				}
			}else{
				for(Player pl : Bukkit.getServer().getOnlinePlayers()){
					if(pl.canSee(p)){
						String message = ConfMsfStringSetings.replacer(p, "Messages.Default.join-message");
						if(message != null){
							pl.sendMessage(message);
						}
					}
				}
			}
		}
		plugin.log.info("[ConfigurableMessages] " + p.getName() + " has join the game.");
		e.setJoinMessage(null);
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(s.isSidebarEnable()){
			ConfigurableMessages.getPlugin().getScoreboardManager().RemoveSidebar(p);
		}
		if(s.isMessageEnable()){
			if(s.isMessageGroupEnable() && ConfigurableMessages.chat != null && ConfigurableMessages.permission != null){
				for(Player pl : Bukkit.getServer().getOnlinePlayers()){
					if(pl.canSee(p)){
						String jmessage = "Messages.Groups." + ConfigurableMessages.permission.getPrimaryGroup(p) + ".quit-message";
						String message = ConfMsfStringSetings.replacer(p, jmessage);
						if(message != null){
							pl.sendMessage(message);
						}
					}
				}
			}else{
				for(Player pl : Bukkit.getServer().getOnlinePlayers()){
					if(pl.canSee(p)){
						String message = ConfMsfStringSetings.replacer(p, "Messages.Default.quit-message");
						if(message != null){
							pl.sendMessage(message);
						}
					}
				}
			}
		}
		plugin.log.info("[ConfigurableMessages] " + p.getName() + " has quit the game.");
		e.setQuitMessage(null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent e){
		Player p = e.getPlayer();
		if(s.isSidebarEnable()){
			//score.unregPl(p);
		}
		if(s.isMessageEnable()){
			if(s.isMessageGroupEnable() && ConfigurableMessages.chat != null && ConfigurableMessages.permission != null){
				for(Player pl : Bukkit.getServer().getOnlinePlayers()){
					if(pl.canSee(p)){
						String jmessage = "Messages.Groups." + ConfigurableMessages.permission.getPrimaryGroup(p) + ".kick-message";
						String message = ConfMsfStringSetings.replacer(p, jmessage);
						if(message != null){
							pl.sendMessage(message);
						}
					}
				}
			}else{
				for(Player pl : Bukkit.getServer().getOnlinePlayers()){
					if(pl.canSee(p)){
						String message = ConfMsfStringSetings.replacer(p, "Messages.Default.kick-message");
						if(message != null){
							pl.sendMessage(message);
						}
					}
				}
			}
		}
		plugin.log.info("[ConfigurableMessages] " + p.getName() + " has quit the game.");
		e.setLeaveMessage(null);
	}
	
	public void setKits(Player p){
		for(String item : plugin.getConfig().getStringList("Newbies.Items")){
            String[] itemdata = item.split("-",2);
            int id, amount, itemsdatabyte;
            String[] itemsdata = itemdata[0].split(":", 2);
            try{
            	itemsdatabyte = Integer.parseInt(itemsdata[1]);
                id = Integer.parseInt(itemsdata[0]);
                amount = Integer.parseInt(itemdata[1]);
                p.getInventory().addItem(new ItemStack(id, amount, (byte)itemsdatabyte));
            }
            catch(NumberFormatException e){
                plugin.log.info("Incorrect item format '"+item+"', skipping!");
            }
        }
		
		
	}
}