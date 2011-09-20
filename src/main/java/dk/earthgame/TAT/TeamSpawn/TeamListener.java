package dk.earthgame.TAT.TeamSpawn;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class TeamListener extends PlayerListener {
	private TeamSpawn plugin;
	
	public TeamListener(TeamSpawn instantiate) {
		plugin = instantiate;
	}
	
	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		if (plugin.controller.playerOnTeam(p)) {
			p.teleport(plugin.controller.getTeamOfPlayer(p).spawn);
		}
	}
}