package cz.Sicka_gp.ConfigurableMessages.Automessages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessages;
import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessagesPermissions;

public class ConfigurableMessagesAutomessages {
	public final ConfigurableMessagesPermissions perm = new ConfigurableMessagesPermissions();
	public static ConfigurableMessages plugin;
	public static int currentLine = 0;
	public static int tid = 0;
	public static int running = 1;
	public static long interval = 10;
	private BufferedReader br;
	private LineNumberReader lnr;
	
	public ConfigurableMessagesAutomessages(ConfigurableMessages instance){
		plugin = instance;
		init();
	}
	
	public void init(){
		interval = ConfigurableMessages.getPlugin().getConfig().getLong("Automessage.Interval", 60);
	}
	
	
	public void StartBroadcast(){
		tid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ConfigurableMessages.getPlugin(), new Runnable(){

			@Override
			public void run() {
				try{
					broadcastMessages("plugins/ConfigurableMessages/messages.txt");
				} catch(IOException e){
					
				}
				
			}
			
		}, 0, interval * 20);
	}


	protected void broadcastMessages(String filename) throws IOException {
		FileInputStream fs;
		fs = new FileInputStream(filename);
		br = new BufferedReader(new InputStreamReader(fs));
		for(int i = 0; i < currentLine; ++i){
			br.readLine();
		}
		String line = br.readLine();
		line = ChatColor.translateAlternateColorCodes("&".charAt(0), line);
		for(Player p : Bukkit.getOnlinePlayers()){
			String prefix = ConfigurableMessages.getPlugin().getConfig().getString("Automessage.prefix", "&f[&6ConfigurableMessage&f]");
			if(p.hasPermission(perm.automessageshow)){
				prefix = ChatColor.translateAlternateColorCodes("&".charAt(0), prefix);
				p.sendMessage(prefix + line);
			}
		}
		lnr = new LineNumberReader(new FileReader(new File(filename)));
		lnr.skip(Long.MAX_VALUE);
		int lastLine = lnr.getLineNumber();
		if(currentLine + 1 == lastLine + 1 ){
			currentLine = 0;
		}else{
			currentLine++;
		}
	}
}
