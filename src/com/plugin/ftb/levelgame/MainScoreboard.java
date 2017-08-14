package com.plugin.ftb.levelgame;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	public static int min = 50;
	public static int sec = 0; 
	
	//ゲーム中=true, ゲーム外=false
	public static boolean isPlaying = false;
	
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
		object.getScore("" + ChatColor.RED + ChatColor.BOLD + "残り時間 " + ChatColor.RESET + String.format("%2d:" + zero + "%d", min, sec)).setScore(-1);
	}
	
	//タイマーを開始
	public static void startTimerTask() {
		 new BukkitRunnable() {
           @Override
           public void run() {
	           	if(sec == 0 && min == 0) {
	           		//タイマーが0:00の場合終了する
	           		isPlaying = false;
	           		this.cancel();
	           	}else {
	           		isPlaying = true;
	           		updateTime();
	           	}
           }
       }.runTaskTimer(plugin, 20, 20);
	}
	
	//スコアタスクを開始
	public static void startScoreTask(int second) {
		 new BukkitRunnable() {
           @Override
           public void run() {
	           	if(isPlaying) {
	           		updateScore();
	           	}
           }
       }.runTaskTimer(plugin, 0, second * 20);
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
		object.getScore("" + ChatColor.RED + ChatColor.BOLD + "残り時間 " + ChatColor.RESET + String.format("%2d:" + zero + "%d", min, sec)).setScore(-1);
	}
	
	//スコアを更新
	private static void updateScore() {
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective object = board.getObjective("Level Game");

		//過去のtopList
		List<Map.Entry<String,Integer>> pastTopList = MainUtils.topList;
		
		//経験値を取得し、ソートする
		MainUtils.getLevels();
		
		//現在のtopList
		List<Map.Entry<String,Integer>> topList = MainUtils.topList;
		
		//過去のスコアを削除
		for(Entry<String,Integer> list : pastTopList) {
			board.resetScores("" + ChatColor.GREEN + ChatColor.BOLD + list.getKey());
		}
		
		//新しいスコアを設定
		for(Entry<String,Integer> list : topList) {
			object.getScore("" + ChatColor.GREEN + ChatColor.BOLD + list.getKey()).setScore(list.getValue());
		}
	}
}