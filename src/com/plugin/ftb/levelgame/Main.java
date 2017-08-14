package com.plugin.ftb.levelgame;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	public static Main plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		//スコアボードを登録
		MainScoreboard.registerScoreboard();
		
		//コマンドを登録
		getCommand("levelgame").setExecutor(new LevelGameCommand());
		//タブ補完登録
		getCommand("levelgame").setTabCompleter(new MainTabCompleter());
		
		//イベントリスナを登録
		getServer().getPluginManager().registerEvents(new MainListener(), this);
	}
}

