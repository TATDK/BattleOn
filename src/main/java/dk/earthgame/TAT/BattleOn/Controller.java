package dk.earthgame.TAT.BattleOn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class Controller {
	private BattleOn plugin;
	private List<Team> teams;
	private Configuration TConfig;
	private Configuration SConfig;
	
	public Controller(BattleOn instantiate) {
		plugin = instantiate;
		teams = new ArrayList<Team>();
		TConfig = new Configuration(new File("Teams.yml"));
	}
	
	public boolean playerOnTeam(Player player) { return playerOnTeam(player.getName()); }
	
	public boolean playerOnTeam(String player) {
		for (Team t : teams)
			if (t.containPlayer(player))
				return true;
		return false;
	}
	
	public Team getTeam(String name) {
		name = name.toLowerCase();
		for (Team t : teams)
			if (t.name.equalsIgnoreCase(name))
				return t;
		return null;
	}
	
	public Team getTeamOfPlayer(Player player) { return getTeamOfPlayer(player.getName()); }
	
	public Team getTeamOfPlayer(String player) {
		for (Team t : teams)
			if (t.containPlayer(player)) {
				return t;
			}
		return null;
	}
	
	public boolean teamExists(String name) {
		for (Team t : teams)
			if (t.name.equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public boolean isAdmin(Player player) {
		if (player.isOp())
			return true;
		return false;
	}
	
	public List<Team> getTeams() {
		return teams;
	}
	
	void createDefaultConfigFiles(String filename,String saveas) {
        File actual = new File(saveas);
        if (!actual.exists()) {
            InputStream input = BattleOn.class.getResourceAsStream("/Config/" + filename);
            if (input != null) {
                FileOutputStream output = null;

                try {
                    output = new FileOutputStream(actual);
                    byte[] buf = new byte[8192];
                    int length = 0;
                    while ((length = input.read(buf)) > 0) {
                        output.write(buf, 0, length);
                    }
                    plugin.log.info("Default team file created!");
                } catch (IOException e) {
                    e.printStackTrace();
                    plugin.log.info("Error creating team file!");
                } finally {
                    try {
                        if (input != null)
                            input.close();
                    } catch (IOException e) {}

                    try {
                        if (output != null)
                            output.close();
                    } catch (IOException e) {}
                }
            }
        } else {
        	plugin.log.info("Config file found!");
        }
    }

	@SuppressWarnings("unchecked")
	void loadTeams() {
		TConfig.load();
		
		HashMap<String, List<String>> loadedTeams = (HashMap<String, List<String>>)TConfig.getProperty("Teams");
		
		Iterator<Entry<String, List<String>>> it = loadedTeams.entrySet().iterator();
		DyeColor[] validTeamNames = DyeColor.values();
		while (it.hasNext()) {
	        Map.Entry<String, List<String>> pairs = (Map.Entry<String, List<String>>)it.next();
	        
	        boolean valid = false;
	        for (DyeColor t : validTeamNames) {
	        	if (pairs.getKey().equalsIgnoreCase(t.name()))
	        		valid = true;
	        }
	        
	        if (!valid)
	        	plugin.log.warning("[BattleOn] UNKNOWN TEAMNAME! " + pairs.getKey());
	        else
	        	teams.add(new Team(pairs.getKey(),new Location(plugin.getServer().getWorlds().get(0),0,0,0),pairs.getValue(),this));
		}
	}
	
	void saveTeams() {
		HashMap<String, List<String>> savedTeams = new HashMap<String, List<String>>();
		
		for (Team t : teams) {
			savedTeams.put(t.name, t.players);
		}
		
		TConfig.setProperty("Teams", savedTeams);
		TConfig.save();
	}
	
	String getSpawnFileLocation() {
		return plugin.getServer().getWorlds().get(0).getName() + "/Spawns.yml";
	}
	
	void loadSpawns() {
		SConfig = new Configuration(new File(getSpawnFileLocation()));
		loadSpawnsWorker();
	}
	
	private void loadSpawnsWorker() {
		SConfig.load();
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> loadedSpawns = (HashMap<String, String>)SConfig.getProperty("Spawns");
		
		Iterator<Entry<String, String>> it = loadedSpawns.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
	        
	        boolean valid = false;
        	for (Team t : teams) {
        		if (pairs.getKey().toLowerCase().equalsIgnoreCase(t.name)) {
        			String[] lvalue = pairs.getValue().split(",");
        			Location l = new Location(plugin.getServer().getWorld(lvalue[0]),Double.parseDouble(lvalue[1]),Double.parseDouble(lvalue[2]),Double.parseDouble(lvalue[3]),Float.parseFloat(lvalue[4]),Float.parseFloat(lvalue[5]));
        			t.spawn = l;
        			valid = true;
        		}
        	}
	        if (!valid) {
	        	plugin.log.info("Unknown team name " + pairs.getKey());
	        }
		}
	}
	
	void saveSpawns() {
		if (SConfig == null)
			SConfig = new Configuration(new File(getSpawnFileLocation()));
		HashMap<String, String> savedSpawns = new HashMap<String, String>();
		for (Team t : teams)
			savedSpawns.put(t.name, t.spawn.getWorld().getName() + "," + t.spawn.getX() + "," + t.spawn.getY() + "," + t.spawn.getZ() + "," + t.spawn.getYaw() + "," + t.spawn.getPitch());
		SConfig.setProperty("Spawns", savedSpawns);
		SConfig.save();
	}
}