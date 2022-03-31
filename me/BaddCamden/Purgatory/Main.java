package me.BaddCamden.Purgatory;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin{
	public File datafile;
	public FileConfiguration database;
	@Override
	public void onEnable() {
		datafile = new File(this.getDataFolder()+"/database.yml");
		database = YamlConfiguration.loadConfiguration(datafile);
		PluginManager pm = getServer().getPluginManager();
		RegisterEvents listener = new RegisterEvents(this);
		pm.registerEvents(listener, this );
		this.getCommand("PurgatoryF").setExecutor(listener);

    }
	@Override
	public void onDisable() {
		
	}
}
