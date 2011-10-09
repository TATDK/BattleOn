package dk.earthgame.TAT.BattleOn;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EListener extends EntityListener {
	@Override
	public void onFoodLevelChange (FoodLevelChangeEvent event) {
		if (event.getFoodLevel() != 17)
			event.setFoodLevel(17);
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player)event.getEntity();
			boolean haveWool = true;
			if (p.getInventory().getHelmet().getTypeId() != 35)
				haveWool = false;
			switch (event.getCause()) {
				case ENTITY_ATTACK:
					if (!haveWool)
						event.setDamage(9);
					break;
				default:
					if (!haveWool)
						event.setDamage(event.getDamage()*2);
					break;
			}
		}
	}
}
