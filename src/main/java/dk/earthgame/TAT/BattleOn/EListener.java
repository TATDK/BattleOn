package dk.earthgame.TAT.BattleOn;

import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EListener extends EntityListener {
	@Override
	public void onFoodLevelChange (FoodLevelChangeEvent event) {
		if (event.getFoodLevel() != 17)
			event.setFoodLevel(17);
	}
}
