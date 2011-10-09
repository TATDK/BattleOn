package dk.earthgame.TAT.BattleOn;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.material.Wool;

public class PListener extends PlayerListener {
	private BattleOn plugin;
	
	public PListener(BattleOn instantiate) {
		plugin = instantiate;
	}
	
	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		if (plugin.running) {
			event.setRespawnLocation(plugin.controller.getTeamOfPlayer(p).spawn);
			Wool armorWool = new Wool();
			armorWool.setColor(DyeColor.valueOf(plugin.controller.getTeamOfPlayer(p).name.toUpperCase()));
			p.getInventory().setHelmet(armorWool.toItemStack());
		}
	}
	
	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (!plugin.controller.playerOnTeam(event.getPlayer()) || !plugin.controller.isAdmin(event.getPlayer())) {
			event.setResult(Result.KICK_OTHER);
			event.setKickMessage("You aren't on any team! Contant server admin!");
		}
	}
}