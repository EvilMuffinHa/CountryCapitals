package me.EvilMuffinhahaha.countrycapitals;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.EvilMuffinhahaha.countrycapitals.commands.CountryCommand;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("Country command loading...");
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        new CountryCommand(this);
        getLogger().info("Country command loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Country command shutting down...");
        getLogger().info("Bye!");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (CountryCommand.tpStart && p.equals(CountryCommand.player)) {
            p.sendMessage(ChatColor.RED + "You moved! Teleportation cancelled. ");
            CountryCommand.tpStart = false;
        }
    }


}
