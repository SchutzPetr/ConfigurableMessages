package cz.Sicka_gp.ConfigurableMessages.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessages;
import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessagesPermissions;
import cz.Sicka_gp.ConfigurableMessages.Automessages.ConfigurableMessagesAutomessages;

public class ConfigurableMessagesCommandManager implements CommandExecutor{
	public final ConfigurableMessagesPermissions perm = new ConfigurableMessagesPermissions();
	public ConfigurableMessages plugin;

	public ConfigurableMessagesCommandManager(ConfigurableMessages m){
		  this.plugin = m;
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		 String cmds = cmd.getName();
		 if (sender instanceof Player){
				Player player = (Player)sender;
				if(cmds.equalsIgnoreCase("custommessage")){
					if ((args == null) || (args.length < 1)) {
						player.sendMessage(ChatColor.DARK_GREEN + "<>-------------[ " + ChatColor.GOLD + ConfigurableMessages.getPlugin().getName() + ChatColor.DARK_GREEN + " ]-------------<>");
						player.sendMessage(ChatColor.GREEN + "Author" + ChatColor.WHITE + " : " + ChatColor.GOLD + ConfigurableMessages.getPlugin().getDescription().getAuthors());
						player.sendMessage(ChatColor.GREEN + "Version" + ChatColor.WHITE + " : " + ChatColor.GOLD + ConfigurableMessages.getPlugin().getDescription().getVersion());
						player.sendMessage(ChatColor.GREEN + "Automessage Enable/Disable " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/cm am <start/stop>");
						player.sendMessage(ChatColor.GREEN + "Reload config " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/cm conf reload");
						player.sendMessage(ChatColor.GREEN + "Generate new config file " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/cm conf new");
						player.sendMessage(ChatColor.GREEN + "Hide Sidebar " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/sidebar hide");
						player.sendMessage(ChatColor.GREEN + "View sidebar " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/sidebar show");
						return true;
					}
					if(args[0].equalsIgnoreCase("automessage") || args[0].equalsIgnoreCase("am") && args.length <= 2){
						if(player.hasPermission(perm.automessagecommand)){
							if ((args == null) || (args.length < 2)) {
								player.sendMessage(ChatColor.RED + "Use /cm am <start/stop>");
								return false;
							}
							if(args[1].equalsIgnoreCase("stop")){
								if(ConfigurableMessagesAutomessages.running == 1){
									Bukkit.getServer().getScheduler().cancelTask(ConfigurableMessagesAutomessages.tid);
									player.sendMessage(ChatColor.GREEN + "Automessage has been stopped!");
									ConfigurableMessagesAutomessages.running = 0;
								}else{
									player.sendMessage(ChatColor.RED + "Automessage not running!");
								}
								return false;
							}
							if(args[1].equalsIgnoreCase("start")){
								if(ConfigurableMessagesAutomessages.running == 1){
									player.sendMessage(ChatColor.RED + "Automessage not running!");
								}else{
									ConfigurableMessages.getPlugin().getConfigurableMessagesAutomessages().StartBroadcast();
									player.sendMessage(ChatColor.GREEN + "Automessage has been started!");
									ConfigurableMessagesAutomessages.running = 1;
								}
								return false;
							}
						}else{
							player.sendMessage(ChatColor.DARK_RED + "You dont have permission for this!");
						}
					}
					if(args[0].equalsIgnoreCase("conf")&& args.length <= 2){
						if(player.hasPermission(perm.reload)){
							if ((args == null) || (args.length < 2)) {
								player.sendMessage(ChatColor.GREEN + "Reload config " + ChatColor.WHITE + " : " + ChatColor.GREEN + "/cm conf reload");
								player.sendMessage(ChatColor.GREEN + "Generate new config file " + ChatColor.WHITE + " : " + ChatColor.GREEN + "/cm conf new");
								return false;
							}
							if(args[1].equalsIgnoreCase("reload")){
								for(Player p : ConfigurableMessages.getPlugin().getServer().getOnlinePlayers()){
									p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
								}
								ConfigurableMessages.getPlugin().reloadConfiguration();
								ConfigurableMessages.getPlugin().getScoreboardManager().init();
								player.sendMessage(ChatColor.GREEN + "Configuration reloaded");
								return false;
							}
							if(args[1].equalsIgnoreCase("new")){
								for(Player p : ConfigurableMessages.getPlugin().getServer().getOnlinePlayers()){
									p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
								}
								ConfigurableMessages.getPlugin().saveResource("config.yml", true);
								player.sendMessage(ChatColor.GREEN + "Creating config file");
								ConfigurableMessages.getPlugin().log.info("[ConfigurableMessages] Creating config file.");
								ConfigurableMessages.getPlugin().reloadConfiguration();
								return false;
							}
						}else{
							player.sendMessage(ChatColor.DARK_RED + "You dont have permission for this!");
						}
					}else{
						player.sendMessage(ChatColor.DARK_GREEN + "<>-------------[ " + ChatColor.GOLD + ConfigurableMessages.getPlugin().getName() + ChatColor.DARK_GREEN + " ]-------------<>");
						player.sendMessage(ChatColor.GREEN + "Author" + ChatColor.WHITE + " : " + ChatColor.GOLD + ConfigurableMessages.getPlugin().getDescription().getAuthors());
						player.sendMessage(ChatColor.GREEN + "Version" + ChatColor.WHITE + " : " + ChatColor.GOLD + ConfigurableMessages.getPlugin().getDescription().getVersion());
						player.sendMessage(ChatColor.GREEN + "Automessage Enable/Disable " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/cm am <start/stop>");
						player.sendMessage(ChatColor.GREEN + "Reload config " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/cm conf reload");
						player.sendMessage(ChatColor.GREEN + "Generate new config file " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/cm conf new");
						player.sendMessage(ChatColor.GREEN + "Hide Sidebar " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/sidebar hide");
						player.sendMessage(ChatColor.GREEN + "View sidebar " + ChatColor.WHITE + " : " + ChatColor.GOLD + "/sidebar show");
						return true;
					}
				}
				
				if(cmds.equalsIgnoreCase("sidebar")){
					if(player.hasPermission(perm.sidebar)){
						if ((args == null) || (args.length < 1)) {
							player.sendMessage(ChatColor.RED + "Use /side <show/hide>");
							return true;
						}
						if(args[0].equalsIgnoreCase("show")&& args.length == 1){
							if(ConfigurableMessages.getPlugin().hide.contains(player.getName())){
								ConfigurableMessages.getPlugin().hide.remove(player.getName());
								ConfigurableMessages.getPlugin().getScoreboardManager().CreateSidebar(player);
								player.sendMessage(ChatColor.GREEN + "Sidebar displayed.");
								return true;
							}else{
								player.sendMessage(ChatColor.RED + "The sidebar is already displayed!");
							}
							return true;
						}
						if(args[0].equalsIgnoreCase("hide")&& args.length == 1){
							if(!ConfigurableMessages.getPlugin().hide.contains(player.getName())){
								ConfigurableMessages.getPlugin().hide.add(player.getName());
								ConfigurableMessages.getPlugin().getScoreboardManager().RemoveSidebar(player);
								player.sendMessage(ChatColor.GREEN + "The Sidebar is hidden.");
							}else{
								player.sendMessage(ChatColor.RED + "The sidebar is already hidden!");
							}
							return true;
						}else{
							player.sendMessage(ChatColor.GREEN + "Author" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getAuthors());
							player.sendMessage(ChatColor.GREEN + "Version" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getVersion());
							player.sendMessage(ChatColor.GREEN + "Hide Sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar hide");
							player.sendMessage(ChatColor.GREEN + "View sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar show");
							return true;
						}
					}
				}
		 }return false;
	 }

}
