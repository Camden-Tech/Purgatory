package me.BaddCamden.Purgatory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import me.BaddCamden.PurgatoryAPI.APIUtilities;
import me.BaddCamden.PurgatoryAPI.events.PlayerEnterPurgatoryEvent;
import me.BaddCamden.PurgatoryAPI.events.PlayerFailPurgatoryEvent;


public class RegisterEvents implements Listener,CommandExecutor, TabCompleter{
	Main mainPlugin;
	APIUtilities api = new APIUtilities();
	
	public RegisterEvents(Main main) {
		mainPlugin = main;
	}
	
	@EventHandler
	public void PlayerDeathEvent(PlayerRespawnEvent e) {
		if(api.getWorlds().contains(e.getPlayer().getWorld().getName())) {
			PlayerFailPurgatoryEvent event = new PlayerFailPurgatoryEvent(e.getPlayer(), e.getPlayer().getWorld(), "death");
			Bukkit.getServer().getPluginManager().callEvent(event);
			e.getPlayer().sendMessage(ChatColor.RED+"You failed purgatory!");
		} else {
	        Bukkit.getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {
	            @Override
	            public void run() { 
	            	api.sendPlayerToPurgatory(e.getPlayer(), true);
	            }
	         }, 1);
			
		}
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
        List<String> list = new ArrayList<String>();
        List<String> results = new ArrayList<String>();
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("PurgatoryF")) {
                if (args.length == 0) {
                    list.add("setExitLocation");
                    list.add("removeExitLocation");
                    Collections.sort(list);
                    return list;
                } else if (args.length == 1) {

                    list.add("setExitLocation");
                    list.add("removeExitLocation");
                    
                    for (String s : list){
                        if (s.toLowerCase().startsWith(args[0].toLowerCase())){
                        	results.add(s);
                        }
                    }
                    Collections.sort(results);
                    return results;
                } else if(args.length == 2) {
                	
                    for (String s : list){
                        if (s.toLowerCase().startsWith(args[1].toLowerCase())){
                            results.add(s);
                        }
                    }
                	Collections.sort(results);
                	return results;
                }
            }
        }
        return list;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0.isOp()) {
			if(arg2.equalsIgnoreCase("PurgatoryF")) {
				if(arg3[0].equalsIgnoreCase("setExitLocation")) {
					Player p = (Player)arg0;
					String w = p.getWorld().getName();
					if(api.getTag(w, "exists") != null) {
						arg0.sendMessage(ChatColor.GREEN+"Location has been set!");
						api.setTag(w, "location.x", ""+p.getLocation().getX());
						api.setTag(w, "location.y", ""+p.getLocation().getY());
						api.setTag(w, "location.z", ""+p.getLocation().getZ());
						
					} else {
						arg0.sendMessage(ChatColor.DARK_RED+"Not purgatory world!");
					}
				} else if(arg3[0].equalsIgnoreCase("removeExitLocation")) {
					Player p = (Player)arg0;
					String w = p.getWorld().getName();
					api.setTag(w, "location", null);
				}
			}
		}

		return false;
	}
	
	
	
	@EventHandler
	public void PlayerCrouchToggleEvent(PlayerToggleSneakEvent e) {
		if(api.getTag(e.getPlayer().getWorld().getName(), "location") != null) {
			if(e.getPlayer().getLocation().distance(new Location(e.getPlayer().getWorld(),Double.parseDouble(api.getTag(e.getPlayer().getWorld().getName(), "location.x")),Double.parseDouble(api.getTag(e.getPlayer().getWorld().getName(), "location.y")),Double.parseDouble(api.getTag(e.getPlayer().getWorld().getName(), "location.z")))) < 2) {
				api.exitPurgatory(e.getPlayer());
				
			}	
		}

	}
	
	
}
