package dk.earthgame.TAT.BattleOn;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleOn extends JavaPlugin {
	private PluginDescriptionFile description;
	Logger log = Logger.getLogger("Minecraft.TeamSpawn");
	
	public TeamController controller = new TeamController(this);
	private TeamListener TSlistener = new TeamListener(this);
	private TeamExecutor TSexecutor = new TeamExecutor(this);
	
	@Override
	public void onEnable() {
		description = this.getDescription();
		log.info(description.getName() + " v." + description.getVersion() + " startet!");
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_RESPAWN, TSlistener, Priority.Normal, this);
		
		getCommand("team").setExecutor(TSexecutor);
		
		controller.createDefaultConfigFiles();
		controller.loadTeams();
		controller.loadSpawns();
	}

	@Override
	public void onDisable() {
		log.info(description.getName() + " v." + description.getVersion() + " stoppet!");
	}
}