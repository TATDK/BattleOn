package dk.earthgame.TAT.BattleOn;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
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
			p.teleport(plugin.controller.getTeamOfPlayer(p).spawn);
			ItemStack[] armor = new ItemStack[4];
			armor[0] = new ItemStack(Material.AIR);
			Wool armorWool = new Wool();
			armorWool.setColor(DyeColor.valueOf(plugin.controller.getTeamOfPlayer(p).name.toUpperCase()));
			armor[1] = armorWool.toItemStack();
			armor[2] = new ItemStack(Material.AIR);
			armor[3] = new ItemStack(Material.AIR);
			p.getInventory().setArmorContents(armor);
		}
	}
	
	@Override
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (!plugin.controller.playerOnTeam(event.getPlayer())) {
			event.setResult(Result.KICK_OTHER);
			event.setKickMessage("You aren't on any team! Contant server admin!");
		}
	}
}