package dk.earthgame.TAT.BattleOn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public class Controller {
	private BattleOn plugin;
	private List<Team> teams;
	private List<String> admins;
	private Configuration TConfig;
	private Configuration SConfig;
	
	public Controller(BattleOn instantiate) {
		plugin = instantiate;
		teams = new ArrayList<Team>();
		TConfig = new Configuration(new File(plugin.getDataFolder(), "/../../Teams.yml"));
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
	
	void createDefaultConfigFiles() {
        String name = "Teams.yml";
        File actual = new File(plugin.getDataFolder(), "/../../" + name);
        if (!actual.exists()) {
            InputStream input = BattleOn.class.getResourceAsStream("/Config/" + name);
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
	
	void loadTeams() {
		TConfig.load();
	}
	
	void saveTeams() {
		
	}
	
	void loadSpawns() {
		
	}
}