package dk.earthgame.TAT.TeamSpawn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class TeamController {
	private TeamSpawn plugin;
	private List<Team> teams;
	private Configuration config;
	
	public TeamController(TeamSpawn instantiate) {
		plugin = instantiate;
		teams = new ArrayList<Team>();
		config = new Configuration(new File(plugin.getDataFolder(), "Teams.yml"));
	}
	
	public boolean playerOnTeam(Player player) { return playerOnTeam(player.getName()); }
	
	public boolean playerOnTeam(String player) {
		for (Team t : teams) {
			if (t.containPlayer(player)) {
				return true;
			}
		}
		return false;
	}
	
	public Team getTeam(String name) {
		name = name.toLowerCase();
		for (Team t : teams) {
			if (t.cleanname.equalsIgnoreCase(name)) {
				return t;
			}
		}
		return null;
	}
	
	public Team getTeamOfPlayer(Player player) { return getTeam(player.getName()); }
	
	public Team getTeamOfPlayer(String player) {
		for (Team t : teams) {
			if (t.containPlayer(player)) {
				return t;
			}
		}
		return null;
	}
	
	public boolean teamExists(String name) {
		for (Team t : teams) {
			if (t.cleanname.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void createTeam(String name,Location l) {
		createTeam(name,l,"");
	}
	
	public void createTeam(String name,Location l,String players) {
		Team t = new Team(name,l,players,this);
		teams.add(t);
	}
	
	public void removeTeam(String name) {
		for (Team t : teams) {
			if (t.cleanname.equalsIgnoreCase(name)) {
				teams.remove(t);
				break;
			}
		}
	}
	
	public List<Team> getTeams() {
		return teams;
	}
	
	void createDefaultTeams() {
        String name = "Teams.yml";
        File actual = new File(plugin.getDataFolder(), name);
        if (!actual.exists()) {
            InputStream input = TeamSpawn.class.getResourceAsStream("/Config/" + name);
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	void loadTeams() {
        config.load();
        
        HashMap<String, HashMap<String,String>> teams = (HashMap<String, HashMap<String,String>>)config.getProperty("Teams");
        Iterator it = teams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, HashMap<String,String>> pairs = (Map.Entry<String, HashMap<String,String>>)it.next();
            String name = pairs.getKey();
            Location l = new Location(plugin.getServer().getWorlds().get(0),0,67,0);
            String players = "";
            Iterator it2 = pairs.getValue().entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String,String> pairs2 = (Map.Entry<String,String>)it2.next();
                if (pairs2.getKey().equalsIgnoreCase("Spawn")) {
                	String[] lv = pairs2.getValue().split(",");
                	UUID wid;
                	if (lv[0].equalsIgnoreCase("0")) {
                		wid = plugin.getServer().getWorlds().get(Integer.parseInt(lv[0])).getUID();
                	} else {
                		wid = UUID.fromString(lv[0]);
                	}
                	l = new Location(plugin.getServer().getWorld(wid),Double.parseDouble(lv[1]),Double.parseDouble(lv[2]),Double.parseDouble(lv[3]));
                }
                if (pairs2.getKey().equalsIgnoreCase("Players")) {
                	players = pairs2.getValue();
                }
            }
            createTeam(name, l, players);
        }
	}
	
	void saveTeams() {
		HashMap<String, HashMap<String,String>> output = new HashMap<String, HashMap<String,String>>();
		List<Team> teams = getTeams();
		for (Team t : teams) {
			HashMap<String, String> value = new HashMap<String, String>();
			value.put("Spawn", t.spawn.getWorld().getUID().toString() + "," + t.spawn.getX() + "," + t.spawn.getY() + "," + t.spawn.getZ());
			value.put("Players", t.outputPlayers());
			output.put(t.name, value);
		}
		config.setProperty("Teams", output);
		config.save();
	}
}