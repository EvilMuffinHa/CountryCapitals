package me.EvilMuffinhahaha.countrycapitals.commands;
import me.EvilMuffinhahaha.countrycapitals.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CountryCommand implements CommandExecutor {

    public static boolean tpStart = false;
    public static Player player = null;

    private Main plugin;


    public CountryCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("country").setExecutor(this);
    }

    ArrayList<Player> cooldown = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        int seconds = plugin.getConfig().getInt("cooldown");
        int delay = plugin.getConfig().getInt("delay");

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command. ");
            return true;
        }
        final Player p = (Player) sender;
        player = p;
        plugin.getLogger().info(p.getName() + " issued the command " + cmd.toString());
        if (cooldown.contains(p) && !(p.hasPermission("country.bypasscooldown"))) {
            sender.sendMessage(ChatColor.RED + "You cannot teleport yet! ");
            plugin.getLogger().info(p.getName() + " recieved [" + ChatColor.RED + "You cannot teleport yet! " + ChatColor.WHITE + "]");
        }
        if (p.hasPermission("country.use")) {
            if (args.length >= 1) {
                String input = "";
                for (String i : args) {
                    input += i + "_";
                }
                input = input.substring(0, input.length() - 1);
                input = input.toLowerCase();
                String capital = plugin.getConfig().getString("countries." + input + ".capital");
                List<Integer> coords = plugin.getConfig().getIntegerList("countries." + input + ".coords");
                coords.get(0).toString();
                int x = coords.get(0);
                int z = coords.get(2);
                int y = Bukkit.getWorld("earth").getHighestBlockAt(coords.get(0), coords.get(2)).getY() + 2;
                if (!p.hasPermission("country.bypassdelay")) {
                    p.sendMessage("Teleporting you to " + ChatColor.YELLOW + capital + ChatColor.WHITE + " in " + ChatColor.AQUA + delay + ChatColor.WHITE + " seconds...");

                    plugin.getLogger().info(p.getName() + " recieved [" + "Teleporting you to " + ChatColor.YELLOW + capital + ChatColor.WHITE + " in " + ChatColor.AQUA + delay + ChatColor.WHITE + " seconds..." + ChatColor.WHITE + "]");

                } else {
                    p.sendMessage("Teleporting you to " + ChatColor.YELLOW + capital);

                    plugin.getLogger().info(p.getName() + " recieved [" + "Teleporting you to " + ChatColor.YELLOW + capital + ChatColor.WHITE + "]");

                }
                tpStart = true;
                if (p.hasPermission("country.bypassdelay")) {
                    delay = 0;
                }
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        if (tpStart) {
                            p.teleport(new Location(Bukkit.getWorld("earth"), x, Bukkit.getWorld("earth").getHighestBlockAt(coords.get(0), coords.get(2)).getY() + 2, z));
                        }
                        tpStart = false;
                    }
                }, delay * 20L);
                cooldown.add(p);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        cooldown.remove(p);
                    }
                }, seconds * 20L);
                return true;
            } else {
                p.sendMessage(ChatColor.RED + "Incorrect number of arguments. ");

                plugin.getLogger().info(p.getName() + " recieved [" + ChatColor.RED + "Incorrect number of arguments. " + ChatColor.WHITE + "]");
            }
        } else {
            p.sendMessage(ChatColor.RED + "You do not have permission to use this command!");

            plugin.getLogger().info(p.getName() + " recieved [" + ChatColor.RED + "You do not have permission to use this command!" + ChatColor.WHITE + "]");

        }
        return false;

    }
}
