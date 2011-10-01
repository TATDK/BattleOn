package dk.earthgame.TAT.BattleOn;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Team {
	private Controller controller;
	public String name;
	public String cleanname;
	public Location spawn;
	private List<String> players;
	
	public Team(String name, Location spawn, List<String> players, Controller instantiate) {
		controller = instantiate;
		this.name = name;
		this.cleanname = name.toLowerCase();
		this.spawn = spawn;
		this.players = players;
	}
	
	public void addPlayer(Player p) { addPlayer(p.getName()); }
	
	public void addPlayer(String p) {
		players.add(p.toLowerCase());
		controller.saveTeams();
	}
	
	public void removePlayer(Player p) { removePlayer(p.getName()); }
	
	public void removePlayer(String p) {
		players.remove(p.toLowerCase());
	}
	
	public boolean containPlayer(Player p) { return containPlayer(p.getName()); }
	
	public boolean containPlayer(String p) {
		return players.contains(p.toLowerCase());
	}

	public String outputPlayers() {
		String output = "";
		for (String p : players) {
			if (!output.equalsIgnoreCase(""))
				output += ",";
			output += p;
		}
		return output;
	}
}