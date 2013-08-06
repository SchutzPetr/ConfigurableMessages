package cz.Sicka_gp.ConfigurableMessages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.minecraft.server.v1_6_R2.EntityPlayer;
import nl.lolmewn.stats.api.StatsAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.CharacterManager;
import com.nisovin.magicspells.mana.ManaBar;

import cz.Sicka_gp.ConfigurableMessages.ScoreBoard.ScoreboardManager;
import cz.Sicka_gp.ConfigurableMessages.ScoreBoard.ScoreboardPacketManager;
import cz.Sicka_gp.ConfigurableMessages.Settings.ConfMsfBooleanSetings;
import cz.Sicka_gp.ConfigurableMessages.Settings.ConfMsfOtherSetings;
import cz.Sicka_gp.ConfigurableMessages.Settings.ConfMsfStringSetings;

import uk.org.whoami.geoip.GeoIPLookup;
import uk.org.whoami.geoip.GeoIPTools;

public class ConfigurableMessages extends JavaPlugin{
	public FileConfiguration config = null;
	public File configFile = null;
	public FileConfiguration badwords = null;
	public File badwordsFile = null;
	public Logger log = Logger.getLogger("Minecraft");
	public static StatsAPI api;
	public static boolean mana;
	public static ManaBar manabar;
	private static ConfigurableMessages plugin;
	public static Permission permission = null;
    public static Economy econ = null;
    public static Chat chat = null;
	public ArrayList<String> hide = new ArrayList<String>();
	public final ConfigurableMessagesListener cl = new ConfigurableMessagesListener(this);
	public final ConfigurableMessagesChat clc = new ConfigurableMessagesChat(this);
	public final ConfigurableMessagesPermissions perm = new ConfigurableMessagesPermissions();
	public final ConfMsfBooleanSetings s = new ConfMsfBooleanSetings(this);
	public final ConfMsfOtherSetings set = new ConfMsfOtherSetings(this);
	public final ScoreboardManager score = new ScoreboardManager(this);
	public final ConfMsfStringSetings cmss = new ConfMsfStringSetings(this);
	private static boolean mcmmo;
	private static CharacterManager heroes;
	public GeoIPLookup geo = null;
	private final ScoreboardPacketManager packetmanager = new ScoreboardPacketManager(this);
	
	
	public void onEnable(){
		plugin = this;
		reloadConfiguration();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.cl, this);
		pm.registerEvents(this.clc, this);
		//sbm.UpdateScore();
		File Badwords = new File(this.getDataFolder(), "badwords.yml");
		if (!Badwords.exists()) {
		   saveResource("badwords.yml", true);
		   log.info("[ConfigurableMessages] Creating badwords file");
		}
		Plugin Vault = getServer().getPluginManager().getPlugin("Vault");
		if((Vault != null)&& Vault.isEnabled()){
			log.info("[ConfigurableMessages] Vault found");
			if(!setupPermissions()){
				log.info("[ConfigurableMessages] Permission not found");
			}else{
				log.info("[ConfigurableMessages] Permission found");
				setupPermissions();
			}
			if(!setupChat()){
				log.info("[ConfigurableMessages] Chat not found");
			}else{
				log.info("[ConfigurableMessages] Chat found");
				setupChat();
			}
			if (!setupEconomy() ) {
				log.info("[ConfigurableMessages] Economy not found");
			}else{
				log.info("[ConfigurableMessages] Economy found.");
				setupEconomy();
			}
		}else{
			log.info("[ConfigurableMessages] Vault not found!");
        	log.info("[ConfigurableMessages] Groups message disable!");
        	this.getConfig().set("Messages.Groups", false);
		}
		try {
		      Metrics metrics = new Metrics(this);
		      metrics.start();
		    } catch (IOException e) {
		    }
		
		Plugin Stats = getServer().getPluginManager().getPlugin("Stats");
		if(Stats != null && Stats.isEnabled()){
			log.info("[ConfigurableMessages] Stats found");
			setupStatsAPI();
		}else{
			log.info("[ConfigurableMessages] Stats not found");
		}
		getConfig();
		if(this.getConfig().getBoolean("AutoUpdate", true)){
			new Updater(this, "configurable-messages", this.getFile(), Updater.UpdateType.DEFAULT, true);
		}
		if(this.getServer().getPluginManager().getPlugin("mcMMO") != null){
			mcmmo = pm.getPlugin("mcMMO") != null;
			log.info("[ConfigurableMessages] mcMMO found");
		}else{
			log.info("[ConfigurableMessages] mcMMO not found");
		}
		if(this.getServer().getPluginManager().getPlugin("Heroes") != null){
			heroes = ((Heroes)pm.getPlugin("Heroes")).getCharacterManager();
			log.info("[ConfigurableMessages] Heroes found");
		}else{
			log.info("[ConfigurableMessages] Heroes not found");
		}
		if(this.getServer().getPluginManager().getPlugin("GeoIPTools") != null){
			log.info("[ConfigurableMessages] GeoIPTools found");
			setGeoIPLookup();
		}else{
			log.info("[ConfigurableMessages] GeoIPTools not found");
		}
		
		if(this.getServer().getPluginManager().getPlugin("MagicSpells") != null){
			mana = pm.getPlugin("mcMMO") != null;
			log.info("[ConfigurableMessages] MagicSpells found");
		}else{
			log.info("[ConfigurableMessages] MagicSpells not found");
		}
		
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new ConfigurableMessagesTPS(), 100L, 1L);
		ConfigurableMessages.getPlugin().getScoreboardManager().setupTimer();
		for(String score : ConfigurableMessages.getPlugin().getConfig().getStringList("Sidebar.SettingItems")){
		    String[] scores = score.split(";", 2);
		    String scorename = scores[0];
		    String scorevaule = scores[1];
		    if(!(scorename.length() > 16)){
		        try{
		            this.getScoreboardManager().ITEMS.put(scorename, scorevaule);
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
		    }
		}
		log.info("[ConfigurableMessages] version "+ plugin.getDescription().getVersion());
		log.info("[ConfigurableMessages] Authors " + plugin.getDescription().getAuthors());
		log.info("[ConfigurableMessages] is Enable");
	}
	
	public void onDisable(){
		this.saveConfiguration();
		this.saveBadwords();
		this.getServer().getScheduler().cancelTasks(this);
	}
	
	public static Economy getEconomy() {
        return econ;
    }

    public static boolean isMcmmo() {
        return mcmmo;
    }

    public static CharacterManager getHeroes() {
        return heroes;
    }
    
    public static StatsAPI getAPI() {
        return api;
    }
    public static boolean isMagicSpells(){
		return mana;
    	
    }
    public static ManaBar getManaBar(){
    	return manabar;
    }
	
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		 String cmds = cmd.getName();
		 if (sender instanceof Player){
				Player player = (Player)sender;
				if(cmds.equalsIgnoreCase("custommessage")){
					if ((args == null) || (args.length < 1)) {
						player.sendMessage(ChatColor.DARK_GREEN + "Author" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getAuthors());
						player.sendMessage(ChatColor.DARK_GREEN + "Version" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getVersion());
						player.sendMessage(ChatColor.DARK_GREEN + "Reload config " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/cm conf reload");
						player.sendMessage(ChatColor.DARK_GREEN + "Generate new config file " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/cm conf new");
						player.sendMessage(ChatColor.DARK_GREEN + "Hide Sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar hide");
						player.sendMessage(ChatColor.DARK_GREEN + "View sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar show");
						return true;
					}
					if(args[0].equalsIgnoreCase("conf")&& args.length <= 2){
						if(player.hasPermission(perm.reload)){
							if ((args == null) || (args.length < 2)) {
								player.sendMessage(ChatColor.DARK_GREEN + "Reload config " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/cm conf reload");
								player.sendMessage(ChatColor.DARK_GREEN + "Generate new config file " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/cm conf new");
								return false;
							}
							if(args[1].equalsIgnoreCase("reload")){
								for(Player p : ConfigurableMessages.getPlugin().getServer().getOnlinePlayers()){
									p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
								}
								reloadConfiguration();
								player.sendMessage(ChatColor.DARK_RED + "Configuration reloaded");
								return false;
							}
							if(args[1].equalsIgnoreCase("new")){
								for(Player p : ConfigurableMessages.getPlugin().getServer().getOnlinePlayers()){
									p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
								}
								saveResource("config.yml", true);
								player.sendMessage(ChatColor.DARK_GREEN + "Creating config file");
								log.info("[ConfigurableMessages] Creating config file.");
								reloadConfiguration();
								return false;
							}
						}else{
							player.sendMessage(ChatColor.DARK_RED + "You dont have permission for this!");
						}
					}else{
						player.sendMessage(ChatColor.DARK_GREEN + "Author" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getAuthors());
						player.sendMessage(ChatColor.DARK_GREEN + "Version" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getVersion());
						player.sendMessage(ChatColor.DARK_GREEN + "Reload config " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/cm conf reload");
						player.sendMessage(ChatColor.DARK_GREEN + "Generate new config file " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/cm conf new");
						player.sendMessage(ChatColor.DARK_GREEN + "Hide Sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar hide");
						player.sendMessage(ChatColor.DARK_GREEN + "View sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar show");
						return true;
					}
				}
				
				if(cmds.equalsIgnoreCase("sidebar")){
					if(player.hasPermission(perm.sidebar)){
						if ((args == null) || (args.length < 1)) {
							player.sendMessage(ChatColor.DARK_GREEN + "Author" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getAuthors());
							player.sendMessage(ChatColor.DARK_GREEN + "Version" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getVersion());
							player.sendMessage(ChatColor.DARK_GREEN + "Hide Sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar hide");
							player.sendMessage(ChatColor.DARK_GREEN + "View sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar show");
							return true;
						}
						if(args[0].equalsIgnoreCase("show")&& args.length == 1){
							if(hide.contains(player.getName())){
								hide.remove(player.getName());
								this.getScoreboardManager().CreateSidebar(player);
								player.sendMessage(ChatColor.DARK_GREEN + "Sidebar displayed.");
								return true;
							}else{
								player.sendMessage(ChatColor.RED + "The sidebar is already displayed!");
							}
							return true;
						}
						if(args[0].equalsIgnoreCase("hide")&& args.length == 1){
							if(!hide.contains(player.getName())){
								hide.add(player.getName());
								this.getScoreboardManager().RemoveSidebar(player);
								player.sendMessage(ChatColor.DARK_GREEN + "The Sidebar is hidden.");
							}else{
								player.sendMessage(ChatColor.RED + "The sidebar is already hidden!");
							}
							return true;
						}else{
							player.sendMessage(ChatColor.DARK_GREEN + "Author" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getAuthors());
							player.sendMessage(ChatColor.DARK_GREEN + "Version" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + ConfigurableMessages.getPlugin().getDescription().getVersion());
							player.sendMessage(ChatColor.DARK_GREEN + "Hide Sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar hide");
							player.sendMessage(ChatColor.DARK_GREEN + "View sidebar " + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + "/sidebar show");
							return true;
						}
					}
				}
		 }return false;
	 }
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	private boolean setupStatsAPI(){
	    RegisteredServiceProvider<StatsAPI> stats = getServer().getServicesManager().getRegistration(StatsAPI.class);
	    if(stats == null){
	    	return false;
	    }
	    api = stats.getProvider();
	    return api != null;
	}
	
	private boolean setupPermissions(){
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

	private boolean setupChat(){
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
        return (chat != null);
    }
    
    public void setGeoIPLookup() {
    	try{
          Plugin plugin = getServer().getPluginManager().getPlugin("GeoIPTools");

          if (plugin != null){
        	  this.geo = ((GeoIPTools)plugin).getGeoIPLookup();
          }
        }
        catch (NullPointerException e) {
          getLogger().warning("Exception happened during GeoIPTools load: " + e.getMessage());
          getLogger().warning("Please contact the developers");
        }
    }
    
	
    public void reloadConfiguration() {
    	if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
    	      saveDefaultConfig();
    	      log.info("[ConfigurableMessages] Creating config file");
    	    }
    	    try{
    	      reloadConfig();
    	    } catch (Exception e) {
    	      e.printStackTrace();
    	    }
    }
    public void saveConfiguration() {
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
          saveDefaultConfig();
        }
        saveConfig();
    }
    
    public void reloadBadwords() {
	    if (badwordsFile == null) {
	    badwordsFile = new File(getDataFolder(), "badwords.yml");
	    }
	    badwords = YamlConfiguration.loadConfiguration(badwordsFile);
	    InputStream defConfigStream = this.getResource("badwords.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        badwords.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getbadwords() {
	    if (badwords == null) {
	        this.reloadBadwords();
	    }
	    return badwords;
	}
	
    public void saveBadwords() {
        if (badwordsFile == null) {
        	badwordsFile = new File(getDataFolder(), "customConfig.yml");
        }
        if (!badwordsFile.exists()) {            
             this.saveResource("badwords.yml", false);
         }
    }
    
    public ScoreboardPacketManager getScoreBoardPacketManager(){
		return packetmanager;
    	
    }
    public ScoreboardManager getScoreboardManager(){
    	return score;
    }

	public static ConfigurableMessages getPlugin() {
		return plugin;
	}

	public static void setPlugin(ConfigurableMessages plugin) {
		ConfigurableMessages.plugin = plugin;
	}
	
    public int getPing(Player p) {
		CraftPlayer cp = (CraftPlayer) p; EntityPlayer ep = cp.getHandle();
		return ep.ping; 
	}
   
	
}