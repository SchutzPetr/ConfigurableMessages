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
import cz.Sicka_gp.ConfigurableMessages.Settings.ScoreboardItemsReplacer;
 
public class ScoreboardManager {
    ConfigurableMessages plugin;
    Scoreboard sb =  new Scoreboard();
    String name = "Sicka_gp";
    ScoreboardObjective so = new ScoreboardObjective(sb, name, new ScoreboardBaseCriteria(name));
    public Map<String, String> map = new HashMap<String,String>();
    public Map<String, String> ITEMS = new HashMap<String, String>(14);
 
    public ScoreboardManager(ConfigurableMessages instance){
        this.plugin = instance;
    }
    
    public void UnregisterSidebar(Player p){
    	sb.unregisterObjective(sb.getObjective(name));
    	sb.resetPlayerScores(name);
    	ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, "Information", 0);
    	ConfigurableMessages.getPlugin().log.info("Remove");
    }
    
    public void CreateSidebar(Player p) {
    	if(sb.getObjective(name) != null){
    		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, "Information", 0);
    		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardDisplayObjective(p, name, ObjectiveDisplayPosition.SIDEBAR);
    		ConfigurableMessages.getPlugin().log.info("update");
    		UpdateScores(p);
    	}else{
    		ScoreboardObjective reg = sb.registerObjective(name, new ScoreboardBaseCriteria(name));
    		if(reg != null){
    			ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, "Information", 0);
        		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardDisplayObjective(p, name, ObjectiveDisplayPosition.SIDEBAR);
        		ConfigurableMessages.getPlugin().log.info("create");
        		UpdateScores(p);
    		}
    	}
	}
    
    public void RemoveSidebar(Player p){
    	ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, "Information", 1);
    }
    
    public void UpdateScoress(Player p){
    	for(String score : ConfigurableMessages.getPlugin().getConfig().getStringList("Sidebar.SettingItems")){
		    String[] scores = score.split(";", 2);
		    String scorename = scores[0];
		    String scorevaule = scores[1];
		    scorename = ChatColor.translateAlternateColorCodes("&".charAt(0), scorename);
		    if(!(scorename.length() > 16)){
		        try{
		            int scoresvaule = ScoreboardItemsReplacer.getReplacedInt(scorevaule, p);
		            ITEMS.put(scorename, scorevaule);
		            ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardScore(p, scorename, name, scoresvaule, 0);
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
		    }
		}
    }
    public void UpdateScores(Player p){
    	for (Entry<String, String> e : ITEMS.entrySet()){
    		String scorename = e.getKey();
    		String scorevaule = e.getValue();
    		ConfigurableMessages.getPlugin().log.info(scorename + "" + scorevaule);
    		int scoresvaule = ScoreboardItemsReplacer.getReplacedInt(scorevaule, p);
    		ConfigurableMessages.getPlugin().log.info(scorename + "" + scoresvaule);
    		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardScore(p, scorename, name, scoresvaule, 0);
    	}
    }
    
    public void UpdateScoreBoard(Player p){
    	if(sb.getObjective(name) != null){
    		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, "Information", 2);
    		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardDisplayObjective(p, name, ObjectiveDisplayPosition.SIDEBAR);
    		UpdateScores(p);
    	}else{
    		ScoreboardObjective reg = sb.registerObjective(name, new ScoreboardBaseCriteria(name));
    		if(reg != null){
    			ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardObjective(p, name, "Information", 2);
        		ConfigurableMessages.getPlugin().getScoreBoardPacketManager().ScoreBoardDisplayObjective(p, name, ObjectiveDisplayPosition.SIDEBAR);
        		UpdateScores(p);
    		}
    	}
    }
    
    public void setupTimer(){
    	Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ConfigurableMessages.getPlugin(), new Runnable(){
    		
    		@Override
			public void run(){
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
		}, 0L, 5*20L);
		
	}
    
}