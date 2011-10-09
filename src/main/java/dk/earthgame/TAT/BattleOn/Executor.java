package dk.earthgame.TAT.BattleOn;

import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Executor implements CommandExecutor {
    private BattleOn plugin;
    int block = 0;
    int minX = -202;
    int maxX = 73;
    int minY = 3;
    int maxY = 128;
    int minZ = -129;
    int maxZ = 170;
    int curX;
    int curY;
    int curZ;
    int jobID;
    int insertBlock;

    public Executor(BattleOn instantiate) {
        plugin = instantiate;

        minX = 100;
        maxX = 112;
        minY = 100;
        maxY = 112;
        minZ = 100;
        maxZ = 112;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("battle")) {
            Player player = (Player)sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("fuck") && args.length > 1 && plugin.controller.isAdmin(player)) {
                    Date before = new Date();
                    block = 0;
                    insertBlock = Integer.parseInt(args[1]);

                    curX = minX;
                    curY = maxY;
                    curZ = minZ;

                    plugin.getServer().getWorlds().get(0).setAutoSave(false);
                    jobID = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            int run = 0;
                            while (run != 1755) {
                                block += 1;
                                run += 1;

                                plugin.getServer().getWorlds().get(0).getBlockAt(curX, curY, curZ).setTypeId(insertBlock);

                                curZ++;
                                if (curZ >= maxZ) {
                                    curZ = minZ;
                                    curX++;
                                }
                                if (curX >= maxX) {
                                    curX = minX;
                                    curY--;
                                }
                                if (curY > minY) {
                                    plugin.getServer().getScheduler().cancelTask(jobID);
                                    break;
                                }
                            }
                        }
                    }, 0, 1);
                    plugin.getServer().getWorlds().get(0).setAutoSave(true);
                    plugin.getServer().getWorlds().get(0).save();
                    Date now = new Date();
                    plugin.log.info(Material.getMaterial(Integer.parseInt(args[1])).name() + ": " + (now.getTime()-before.getTime()));
                } else if (args[0].equalsIgnoreCase("on") && plugin.controller.isAdmin(player)) {
                    if (plugin.running) {
                        sender.sendMessage("The battle is already running.");
                        return true;
                    }
                    plugin.beginBattle();
                } else if (args[0].equalsIgnoreCase("off") && plugin.controller.isAdmin(player)) {
                    if (!plugin.running) {
                        sender.sendMessage("There isn't a battle to stop.");
                        return true;
                    }
                    plugin.endBattle();
                } else if (args[0].equalsIgnoreCase("reload") && plugin.controller.isAdmin(player)) {
                    plugin.controller.loadTeams();
                    sender.sendMessage("Loaded teams");
                    plugin.controller.loadSpawns();
                    sender.sendMessage("Loaded spawns");
                } else if (args[0].equalsIgnoreCase("setspawn") && args.length > 1 && plugin.controller.isAdmin(player)) {
                    if (plugin.controller.teamExists(args[1])) {
                        plugin.controller.createDefaultConfigFiles("Spawns.yml",plugin.controller.getSpawnFileLocation());
                        plugin.controller.getTeam(args[1]).spawn = ((Player)sender).getLocation();
                        plugin.controller.saveSpawns();
                        sender.sendMessage("Spawn set to your location");
                    } else {
                        sender.sendMessage("Team not found");
                    }
                } else if (args[0].equalsIgnoreCase("move") && args.length > 2 && plugin.controller.isAdmin(player)) {
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