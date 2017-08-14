package com.plugin.ftb.levelgame;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	public static Main plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		MainScoreboard.registerScoreboard();
		MainScoreboard.startTimerTask();
		MainScoreboard.startScoreTask(1);
	}
}

