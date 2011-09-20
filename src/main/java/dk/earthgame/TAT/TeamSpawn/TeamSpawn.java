package dk.earthgame.TAT.TeamSpawn;

import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamSpawn extends JavaPlugin {
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
		
		this.getDataFolder().mkdirs();
		controller.createDefaultTeams();
		controller.loadTeams();
	}

	@Override
	public void onDisable() {
		log.info(description.getName() + " v." + description.getVersion() + " stoppet!");
	}
}