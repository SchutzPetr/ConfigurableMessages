package cz.Sicka_gp.ConfigurableMessages.ScoreBoard;

import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import cz.Sicka_gp.ConfigurableMessages.ConfigurableMessages;

import net.minecraft.server.v1_6_R2.Packet;
import net.minecraft.server.v1_6_R2.Packet206SetScoreboardObjective;
import net.minecraft.server.v1_6_R2.Packet207SetScoreboardScore;
import net.minecraft.server.v1_6_R2.Packet208SetScoreboardDisplayObjective;

public class ScoreboardPacketManager {
	Packet208SetScoreboardDisplayObjective displaypacket;
	Packet206SetScoreboardObjective objectivepacket;
	Packet207SetScoreboardScore scorepacket;
	ConfigurableMessages plugin;
	
	public ScoreboardPacketManager(ConfigurableMessages instance){
        this.plugin = instance;
    }
	
	public static void sendPacket(Player player, Packet packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
	
	public void ScoreBoardObjective(Player p, String name, String displayName, int RemoveUpdate){
		this.objectivepacket = new Packet206SetScoreboardObjective();
		this.objectivepacket.a = name;
		this.objectivepacket.b = displayName;
		this.objectivepacket.c = RemoveUpdate;
		sendPacket(p, objectivepacket);
	}
	
	public void ScoreBoardScore(Player p, String name, String scoreboardName, int value, int addRemove){
		this.scorepacket = new Packet207SetScoreboardScore();
		this.scorepacket.a = name;
		this.scorepacket.b = scoreboardName;
		this.scorepacket.c = value;
		this.scorepacket.d = addRemove;
		sendPacket(p, scorepacket);
		
		
	}
	
	public void ScoreBoardDisplayObjective(Player p, String scoreboardName, ObjectiveDisplayPosition display){
		this.displaypacket = new Packet208SetScoreboardDisplayObjective();
		this.displaypacket.a = display.ordinal();
		this.displaypacket.b = scoreboardName;
		sendPacket(p, displaypacket);
	}

}

