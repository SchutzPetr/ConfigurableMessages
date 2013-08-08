package cz.Sicka_gp.ConfigurableMessages.Settings;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gmail.nossr50.api.ExperienceAPI;
import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessages;
import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessagesTPS;

public class ScoreboardItemsReplacer {
	//base
	 public static int getReplacedInt(String key, Player p){
		 if(ConfigurableMessages.getEconomy() != null){
			 int score = getEconomyItems(key, p);
			 if(score != -1){
				 return score;
			 }
		 }
		 if(ConfigurableMessages.getHeroes() != null){
			 int score =  getHeroesItems(key, p);
			 if(score != -1){
				 return score;
			 }
		 }
		 if(ConfigurableMessages.isMcmmo()){
			 int score =  getMcmmoItems(key, p);
			 if(score != -1){
				 return score;
			 }
		 }
		 if(ConfigurableMessages.getAPI() != null){
			 int score =  getStatsItems(key, p);
			 if(score != -1){
				 return score;
			 }
		 }
		 /*if(ConfigurableMessages.isMagicSpells()){
			 ConfigurableMessages.getPlugin().log.info("!null");
			 int score =  getMagicSpellItems(key, p);
			 if(score != -1){
				 return score;
			 }
		 }*/
		return getLocalItems(key, p);
		 
	 }
	 
	 //ConfigurableMessages
	private static int getLocalItems(String key, Player p){
		if(ScoreboardItemsList.TPS.equals(key)){
			return (int) ConfigurableMessagesTPS.getTPS(20);
		}
		if(ScoreboardItemsList.Online.equals(key)){
			return Bukkit.getOnlinePlayers().length;
		}
		if(ScoreboardItemsList.PING.equals(key)){
			return ConfigurableMessages.getPlugin().getPing(p);
		}
		if (ScoreboardItemsList.FREE_RAM.equals(key)) {
            return (int) Runtime.getRuntime().freeMemory();
        }

        if (ScoreboardItemsList.MAX_RAM.equals(key)) {
            return (int) Runtime.getRuntime().maxMemory();
        }

        if (ScoreboardItemsList.USED_RAM.equals(key)) {
        	return (int) (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory());
        }

        if (ScoreboardItemsList.USED_RAM_PERCENT.equals(key)) {
        	return (int) ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().maxMemory());
        }
        if (ScoreboardItemsList.EXP.equals(key)) {
            return p.getTotalExperience();
        }
        if (ScoreboardItemsList.XPTOLEVEL.equals(key)) {
            return p.getExpToLevel();
        }
        /*if (ScoreboardItemsList.Healt.equals(key)) {
            return p.getHealth();
        }*/
		return -1;
	}
	
	 //Economy
	private static int getEconomyItems(String key, Player p){
		if(ScoreboardItemsList.Balance.equals(key)){
			return (int) ConfigurableMessages.getEconomy().getBalance(p.getName());
		}
		return -1;
	}
	
	//Heroes
	private static int getHeroesItems(String key, Player p) {
		if (ScoreboardItemsList.LEVEL.equals(key)) {
	            return ConfigurableMessages.getHeroes().getHero( p).getLevel();
	    }
        if (ScoreboardItemsList.MANA.equals(key)) {
            return ConfigurableMessages.getHeroes().getHero(p).getMana();
        }
        if (ScoreboardItemsList.MAX_MANA.equals(key)) {
            return ConfigurableMessages.getHeroes().getHero(p).getMaxMana();
        }

        if (ScoreboardItemsList.MANA_REGEN.equals(key)) {
            return ConfigurableMessages.getHeroes().getHero(p).getManaRegen();
        }

        return -1;
    }
	
	//mcMMO
	private static int getMcmmoItems(String key, Player p) {
        if (ScoreboardItemsList.POWLVL.equals(key)) {
            return ExperienceAPI.getPowerLevel(p);
        }

        if (ScoreboardItemsList.WOODCUTTING.equals(key)) {
            return ExperienceAPI.getLevel(p, "WOODCUTTING");
        }

        if (ScoreboardItemsList.ACROBATICS.equals(key)) {
            return ExperienceAPI.getLevel(p, "ACROBATICS");
        }

        if (ScoreboardItemsList.ARCHERY.equals(key)) {
            return ExperienceAPI.getLevel(p, "ARCHERY");
        }

        if (ScoreboardItemsList.AXES.equals(key)) {
            return ExperienceAPI.getLevel(p, "AXES");
        }

        if (ScoreboardItemsList.EXCAVATION.equals(key)) {
            return ExperienceAPI.getLevel(p, "EXCAVATION");
        }

        if (ScoreboardItemsList.FISHING.equals(key)) {
            return ExperienceAPI.getLevel(p, "FISHING");
        }

        if (ScoreboardItemsList.HERBALISM.equals(key)) {
            return ExperienceAPI.getLevel(p, "HERBALISM");
        }

        if (ScoreboardItemsList.MINING.equals(key)) {
            return ExperienceAPI.getLevel(p, "MINING");
        }

        if (ScoreboardItemsList.REPAIR.equals(key)) {
            return ExperienceAPI.getLevel(p, "REPAIR");
        }
        
        if (ScoreboardItemsList.TAMING.equals(key)) {
            return ExperienceAPI.getLevel(p, "TAMING");
        }

        if (ScoreboardItemsList.UNARMED.equals(key)) {
            return ExperienceAPI.getLevel(p, "UNARMED");
        }

        if (ScoreboardItemsList.SMELTING.equals(key)) {
            return ExperienceAPI.getLevel(p, "SMELTING");
        }

        if (ScoreboardItemsList.SWORDS.equals(key)) {
            return ExperienceAPI.getLevel(p, "SWORDS");
        }

        return -1;
    }

	private static int getStatsItems(String key, Player p){
		if(ScoreboardItemsList.PlayTime.equals(key)){
			int playtime = ConfigurableMessages.getAPI().getPlaytime(p.getName());
			int a = playtime/3600;
			return a;
		}
		if(ScoreboardItemsList.TotalBlockBreake.equals(key)){
			return ConfigurableMessages.getAPI().getTotalBlocksBroken(p.getName());
		}
		if(ScoreboardItemsList.TotalBlockPlace.equals(key)){
			return ConfigurableMessages.getAPI().getTotalBlocksPlaced(p.getName());
		}
		if(ScoreboardItemsList.TotalDeaths.equals(key)){
			return ConfigurableMessages.getAPI().getTotalDeaths(p.getName());
		}
		if(ScoreboardItemsList.TotalKills.equals(key)){
			return ConfigurableMessages.getAPI().getTotalKills(p.getName());
		}
		return -1;
		
	}
	
	/*private static int getMagicSpellItems(String key, Player p){
		if(ScoreboardItemsList.MagicMAX_MANA.equals(key)){
			return MagicSpells.getManaHandler().getMaxMana(p);
			
		}
		if(ScoreboardItemsList.MagicMANA_REGEN.equals(key)){
			return MagicSpells.getManaHandler().getRegenAmount(p);
			
		}
		if(ScoreboardItemsList.MagicMANA.equals(key)){
			//return ConfigurableMessages.getManaBar().getMana();
			ManaBar bar = ConfigurableMessages.getManaSystem().
			bar.getMana();
			MagicSpells.getManaHandler().
		}
		return -1;
	}*/
}
