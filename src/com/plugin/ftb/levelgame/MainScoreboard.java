package com.plugin.ftb.levelgame;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class MainScoreboard {
	
	public static Main plugin = Main.plugin;
	
	public static int min = 1;
	public static int sec = 0; 
	
	//スコアボードを登録
	public static void registerScoreboard() {
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		
		Objective object = board.getObjective("Level Game");
		if(object != null) {
			object.unregister();
		}
		//新規オブジェクトを登録
		object = board.registerNewObjective("Level Game", "dummy");
		
		//オブジェクトの表示名を設定
		object.setDisplayName("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "≫  Level Game ≪");
		
		//オブジェクトの表示位置を設定
		object.setDisplaySlot(DisplaySlot.SIDEBAR);	
		
		//オブジェクトの登録
		String zero = sec < 10 ? "0" : "";
		object.getScore("" + ChatColor.RED + ChatColor.BOLD + "残り時間 " + ChatColor.RESET + String.format("%2d:" + zero + "%d", min, sec)).setScore(0);
	}
	
	//タイマーを開始
	public static void startTimer() {
		 new BukkitRunnable() {
            @Override
            public void run() {
            	if(sec == 0 && min == 0) {
            		this.cancel();
            	}else {
            		updateTime();
            	}
            }
        }.runTaskTimer(plugin, 20, 20);
	}
	
	//タイマーを更新
	private static void updateTime() {
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective object = board.getObjective("Level Game");
		
		String zero = sec < 10 ? "0" : "";
		board.resetScores("" + ChatColor.RED + ChatColor.BOLD + "残り時間 " + ChatColor.RESET + String.format("%2d:" + zero + "%d", min, sec));
		
		sec = sec == 0  ? 59 : sec-1;
		min = sec == 59 ? min-1 : min;
		
		zero = sec < 10 ? "0" : "";
		object.getScore("" + ChatColor.RED + ChatColor.BOLD + "残り時間 " + ChatColor.RESET + String.format("%2d:" + zero + "%d", min, sec)).setScore(0);
	}
}