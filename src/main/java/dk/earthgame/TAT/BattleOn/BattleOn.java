package dk.earthgame.TAT.BattleOn;

import java.util.logging.Logger;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.material.Wool;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleOn extends JavaPlugin {
	private PluginDescriptionFile description;
	Logger log = Logger.getLogger("Minecraft.TeamSpawn");
	
	public Controller controller = new Controller(this);
	private PListener Plistener = new PListener(this);
	private EListener Elistener = new EListener();
	private BListener Blistener = new BListener();
	private Executor Executor = new Executor(this);
	
	boolean running;
	private int timeleft;
	private int broadcastInterval = 5;
	private int jobID;
	
	@Override
	public void onEnable() {
		description = this.getDescription();
		log.info(description.getName() + " v." + description.getVersion() + " startet!");
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Type.PLAYER_RESPAWN,    Plistener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_JOIN,       Plistener, Priority.Normal, this);
		pm.registerEvent(Type.FOOD_LEVEL_CHANGE, Elistener, Priority.Normal, this);
		pm.registerEvent(Type.ENTITY_DAMAGE,     Elistener, Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_BREAK,       Blistener, Priority.Normal, this);
		
		getCommand("battle").setExecutor(Executor);
		
		controller.createDefaultConfigFiles("Teams.yml","Teams.yml");
		controller.loadTeams();
	}

	@Override
	public void onDisable() {
		log.info(description.getName() + " v." + description.getVersion() + " stoppet!");
	}
	
	void beginBattle() {
		if (!running) {
			final BattleOn plugin = this;
			controller.loadSpawns();
			getServer().broadcastMessage("Battle begins in 5 seconds!");
			getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					running = true;
					Player[] players = getServer().getOnlinePlayers();
					for (Player p : players) {
						if (!controller.playerOnTeam(p) && !controller.isAdmin(p))
							p.kickPlayer("You aren't on any teams!");
						else {
							p.setFoodLevel(17);
							p.setHealth(20);
							if (!controller.isAdmin(p)) {
								if (!p.isDead())
									p.teleport(controller.getTeamOfPlayer(p).spawn);
								p.getInventory().clear();
								Wool armorWool = new Wool();
								armorWool.setColor(DyeColor.valueOf(controller.getTeamOfPlayer(p).name.toUpperCase()));
								p.getInventory().setHelmet(armorWool.toItemStack());
							}
						}
					}
					getServer().broadcastMessage("Battle has begun!");
					timeleft = 30+broadcastInterval;
					jobID = getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
						@Override
						public void run() {
							timeleft -= broadcastInterval;
							if (timeleft > 0)
								getServer().broadcastMessage("Battle ends in " + timeleft + " minutes!");
							else
								endBattle();
						}
					}, 0, broadcastInterval*20*60);
				}
				
			}, 5*20);
		}
	}
	
	void endBattle() {
		if (running) {
			running = false;
			getServer().getScheduler().cancelTask(jobID);
			getServer().broadcastMessage("Battle has ended!");
			Player[] players = getServer().getOnlinePlayers();
			Location finalSpawn = null;
			for (Player p : players) {
				if (finalSpawn == null)
					finalSpawn = p.getWorld().getSpawnLocation();
				p.setFoodLevel(20);
				p.setHealth(20);
				if (!controller.isAdmin(p)) {
					if (!p.isDead())
						p.teleport(finalSpawn);
					p.getInventory().clear();
				}
			}
		}
	}
}