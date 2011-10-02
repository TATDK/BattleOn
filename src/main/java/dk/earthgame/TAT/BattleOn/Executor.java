package dk.earthgame.TAT.BattleOn;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Executor implements CommandExecutor {
	private BattleOn plugin;
	
	public Executor(BattleOn instantiate) {
		plugin = instantiate;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		if (cmd.getName().equalsIgnoreCase("battle")) {
			Player player = (Player)sender;
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("on") && plugin.controller.isAdmin((Player)sender)) {
					if (plugin.running) {
						sender.sendMessage("The battle is already running.");
						return true;
					}
					plugin.beginBattle();
				} else if (args[0].equalsIgnoreCase("off") && plugin.controller.isAdmin((Player)sender)) {
					if (!plugin.running) {
						sender.sendMessage("There isn't a battle to stop.");
						return true;
					}
					plugin.endBattle();
				} else if (args[0].equalsIgnoreCase("reload") && plugin.controller.isAdmin((Player)sender)) {
					plugin.controller.loadTeams();
					sender.sendMessage("Loaded teams");
					plugin.controller.loadSpawns();
					sender.sendMessage("Loaded spawns");
				} else if (args[0].equalsIgnoreCase("setspawn") && args.length > 1 && plugin.controller.isAdmin((Player)sender)) {
					if (plugin.controller.teamExists(args[1])) {
						plugin.controller.createDefaultConfigFiles("Spawns.yml",plugin.controller.getSpawnFileLocation());
						plugin.controller.getTeam(args[1]).spawn = ((Player)sender).getLocation();
						plugin.controller.saveSpawns();
						sender.sendMessage("Spawn set to your location");
					} else {
						sender.sendMessage("Team not found");
					}
				} else if (args[0].equalsIgnoreCase("move") && args.length > 2 && plugin.controller.isAdmin((Player)sender)) {
					if (!plugin.controller.teamExists(args[1])) {
						if (plugin.controller.playerOnTeam(args[1]))
							plugin.controller.getTeamOfPlayer(args[1]).removePlayer(args[1]);
						plugin.controller.getTeam(args[2]).addPlayer(args[1]);
						plugin.controller.saveTeams();
					} else {
						sender.sendMessage("Team not found");
					}
				} else {
					showHelp(player);
				}
			} else {
				showHelp(player);
			}
			return true;
		}
		return false;
	}
	
	public void showHelp(Player player) {
		player.sendMessage(ChatColor.GOLD + "BattleOn commands");
		if (plugin.controller.isAdmin(player)) {
			player.sendMessage("/battle on - Start battle");
			player.sendMessage("/battle off - End battle");
			player.sendMessage("/battle reload - Reload teams and spawns from files");
			player.sendMessage("/battle setspawn <team> - Set spawn for team");
			player.sendMessage("/battle move <player> <team> - Move player to team");
		}
	}
}
