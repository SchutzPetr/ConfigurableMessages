package cz.Sicka_gp.ConfigurableMessages.ScoreBoard;
 
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import net.minecraft.server.v1_6_R2.Scoreboard;
import net.minecraft.server.v1_6_R2.ScoreboardBaseCriteria;
import net.minecraft.server.v1_6_R2.ScoreboardObjective;
import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessages;
import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessagesPermissions;
import cz.Sicka_gp.ConfigurableMessages.Settings.ConfMsfBooleanSetings;
import cz.Sicka_gp.ConfigurableMessages.Settings.ScoreboardItemsReplacer;
 
public class ScoreboardManager {
    ConfigurableMessages plugin;
    public Scoreboard sb =  new Scoreboard();
    String name = "Sicka_gp";
    String DisplayName;
    public Map<String, String> ITEMS = new HashMap<String, String>(14);
	private int interval;
	public final ConfigurableMessagesPermissions perm = new ConfigurableMessagesPermissions();
	ConfMsfBooleanSetings s = new ConfMsfBooleanSetings(ConfigurableMessages.getPlugin());
 
    public ScoreboardManager(ConfigurableMessages instance){
        this.plugin = instance;
        init();
    }
    
    public void init(){
    	ITEMS.clear();
    	this.DisplayName = ConfigurableMessages.getPlugin().getConfig().getString("Sidebar.Name", "Information");
    	this.DisplayName = ChatColor.translateAlternateColorCodes("&".charAt(0), DisplayName);
    	this.interval = ConfigurableMessages.getPlugin().getConfig().getInt("Sidebar.Update", 1);
    	for(String score : ConfigurableMessages.getPlugin().getConfig().getStringList("Sidebar.SettingItems")){
		    String[] scores = score.split(";", 2);
		    String scorename = scores[0];
		    String scorevaule = scores[1];
		    if(!(scorename.length() > 16)){
		        try{
		            ITEMS.put(scorename, scorevaule);
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
		    }
		}
    }
    
    public void UnregisterSidebar(Player p){
    	sb.unregisterObjective(sb.getObjective(name));
    	sb.resetPlayerScores(name);
    	ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, DisplayName, 0);
    	ConfigurableMessages.getPlugin().log.info("Remove");
    }
    
    public void CreateScoreboardObjective(){
    	sb.registerObjective(name, new ScoreboardBaseCriteria(name));
    }
    
    public void CreateSidebar(Player p) {
    	if(s.isSidebarEnable()){
    		if(p.hasPermission(perm.sidebar) || p.isOp()){
        		if(sb.getObjective(name) != null){
            		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, DisplayName, 0);
            		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardDisplayObjective(p, name, ObjectiveDisplayPosition.SIDEBAR);
            		UpdateScores(p);
            	}else{
            		ScoreboardObjective reg = sb.registerObjective(name, new ScoreboardBaseCriteria(name));
            		if(reg != null){
            			ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, DisplayName, 0);
                		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardDisplayObjective(p, name, ObjectiveDisplayPosition.SIDEBAR);
            		}
            	}
        	}
    	}
	}
    
    public void RemoveSidebar(Player p){
    	ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, DisplayName, 1);
    }
    
    public void UpdateScores(Player p){
    	for (Entry<String, String> e : ITEMS.entrySet()){
    		String scorename = e.getKey();
    		String scorevalue = e.getValue();
    		scorename = ChatColor.translateAlternateColorCodes("&".charAt(0), scorename);
    		int scoresvaule = ScoreboardItemsReplacer.getReplacedInt(scorevalue, p);
    		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardScore(p, scorename, name, scoresvaule, 0);
    	}
    }
    
    public void UpdateScoreBoard(Player p){
    	if(s.isSidebarEnable()){
    		if(p.hasPermission(perm.sidebar)){
        		if(sb.getObjective(name) != null){
            		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, DisplayName, 2);
            		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardDisplayObjective(p, name, ObjectiveDisplayPosition.SIDEBAR);
            		UpdateScores(p);
            	}else{
            		ScoreboardObjective reg = sb.registerObjective(name, new ScoreboardBaseCriteria(name));
            		if(reg != null){
            			ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, DisplayName, 2);
                		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardDisplayObjective(p, name, ObjectiveDisplayPosition.SIDEBAR);
                		UpdateScores(p);
            		}
            	}
        	}
    	}
    }
    
    public void setupTimer(){
    	Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ConfigurableMessages.getPlugin(), new Runnable(){
    		
    		@Override
			public void run(){
    			if(s.isSidebarEnable()){
    				for(Player p : Bukkit.getOnlinePlayers()){
        				Objective objective = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        				if(objective == null){
    						if(!ConfigurableMessages.getPlugin().hide.contains(p.getName())){
    							ConfigurableMessages.getPlugin().getScoreboardManager().UpdateScoreBoard(p);
    						}
    						continue;
    					}else if (!objective.getName().endsWith("Sicka_gp")){
    						continue;
    					}else{
    						ConfigurableMessages.getPlugin().getScoreboardManager().UpdateScoreBoard(p);
    					}
        			}
    			}
			}
		}, 0L, interval*20L);
		
	}
    
}