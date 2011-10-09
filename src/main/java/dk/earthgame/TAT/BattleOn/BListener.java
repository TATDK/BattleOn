package dk.earthgame.TAT.BattleOn;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class BListener extends BlockListener {
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getType().equals(Material.FENCE))
			event.setCancelled(true);
	}
}