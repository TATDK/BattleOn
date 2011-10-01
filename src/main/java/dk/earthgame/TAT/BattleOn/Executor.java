package dk.earthgame.TAT.BattleOn;

import java.util.List;

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
		if (cmd.getName().equalsIgnoreCase("team")) {
			Player player = (Player)sender;
			if (args[0].equalsIgnoreCase("join") && args.length > 1) {
				if (plugin.controller.teamExists(args[1])) {
					if (plugin.controller.playerOnTeam(player)) {
						Team old = plugin.controller.getTeamOfPlayer(player);
						old.removePlayer(player);
						sender.sendMessage("Du er fjernet fra team " + old.name);
					}
					Team newTeam = plugin.controller.getTeam(args[1]);
					newTeam.addPlayer(player);
					sender.sendMessage("Du er tilf�jet til team " + newTeam.name);
				} else {
					sender.sendMessage("Team findes ikke");
				}
			} else if (args[0].equalsIgnoreCase("leave")) {
				if (plugin.controller.teamExists(args[1])) {
					Team old = plugin.controller.getTeamOfPlayer(player);
					old.removePlayer(player);
					sender.sendMessage("Du er fjernet fra team " + old.name);
				} else {
					sender.sendMessage("Team findes ikke");
				}
			} else if (args[0].equalsIgnoreCase("list")) {
				if (args.length > 1) {
					if (plugin.controller.teamExists(args[1])) {
						sender.sendMessage("Team " + args[1]);
						sender.sendMessage(plugin.controller.getTeam(args[1]).outputPlayers());
					} else {
						sender.sendMessage("Team findes ikke");
					}
				} else {
					List<Team> teams = plugin.controller.getTeams();
					sender.sendMessage(ChatColor.GOLD + "Teams");
					for (Team t : teams) {
						sender.sendMessage(t.name);
					}
				}
			} else {
				showHelp(player);
			}
			return true;
		}
		return false;
	}
	
	public void showHelp(Player player) {
		/*if (plugin.getPerm(player) > 0) {
			player.sendMessage(ChatColor.GOLD + "Hvordan bruges TeamSpawn");
			if (plugin.getPerm(player) > 1) {
				//Admin
				player.sendMessage("/team .......");
			}
			player.sendMessage("/team join <team> - Tilslut et team");
			player.sendMessage("/team leave - Forlad team");
			//sender.sendMessage("/team .......");
		} else {
			player.sendMessage(ChatColor.DARK_RED + "Du har ikke lov til at bruge TeamSpawn!");
		}*/
		player.sendMessage(ChatColor.GOLD + "Hvordan bruges TeamSpawn");
		player.sendMessage("/team join <team> - Tilslut et team");
		player.sendMessage("/team leave - Forlad team");
		player.sendMessage("/team list - Vis teams");
		player.sendMessage("/team list <team> - Vis spillere p� team");
	}
}
