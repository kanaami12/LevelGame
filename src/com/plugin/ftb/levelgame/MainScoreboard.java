package com.plugin.ftb.levelgame;

import java.util.HashMap;
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
		for(Player player : Bukkit.getOnlinePlayers()) {
			Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
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
			player.setScoreboard(board);
		}
	}
	
	//タイマーを開始
	public static void startTimerTask() {
		 new BukkitRunnable() {
           @Override
           public void run() {
	           	if(sec == 0 && min == 0) {
	           		//タイマーが0:00の場合終了する
	           		updateScore();
	           		showScores();
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
		int pastSec = sec;
		int pastMin = min;
		sec = sec == 0  ? 59 : sec-1;
		min = sec == 59 ? min-1 : min;
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			Scoreboard board = player.getScoreboard();
			Objective object = board.getObjective("Level Game");
			
			//前回のスコアを削除
			String zero = pastSec < 10 ? "0" : "";
			board.resetScores("" + ChatColor.RED + ChatColor.BOLD + "残り時間 " + ChatColor.RESET + String.format("%2d:" + zero + "%d", pastMin, pastSec));
			
			//新しいスコアを登録
			zero = sec < 10 ? "0" : "";
			object.getScore("" + ChatColor.RED + ChatColor.BOLD + "残り時間 " + ChatColor.RESET + String.format("%2d:" + zero + "%d", min, sec)).setScore(-1);
			player.setScoreboard(board);
		}
	}
	
	//スコアを更新
	private static void updateScore() {	
		//過去のtopList
		HashMap<String, Integer> pastTopList = MainUtils.topList;
		
		//経験値を取得し、ソートする
		MainUtils.getLevels();
		
		//現在のtopList
		HashMap<String, Integer> topList = MainUtils.topList;
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			Scoreboard board = player.getScoreboard();
			Objective object = board.getObjective("Level Game");
			
			//過去のスコアを削除
			for(String name : pastTopList.keySet()) {
				board.resetScores("" + ChatColor.GREEN + ChatColor.BOLD + name);
			}
			
			//新しいスコアを設定
			for(String name : topList.keySet()) {
				object.getScore("" + ChatColor.GREEN + ChatColor.BOLD + name).setScore(topList.get(name));
			}
			
			if(topList.containsKey(player.getName())) {
				board.resetScores("" + ChatColor.WHITE + "現在のスコア");
			}else {
				object.getScore("" + ChatColor.WHITE + "現在のスコア").setScore(player.getLevel());
			}
			
			player.setScoreboard(board);
		}
	}
	
	//結果を表示
	private static void showScores() {
		HashMap<String, Integer> topList = MainUtils.topList;
		
		Bukkit.broadcastMessage(ChatColor.AQUA + "-----------------終了-----------------");
		
		for(int i=0; i<topList.keySet().size(); i++) {
			String rank;
			if(i == 0) rank = "" + ChatColor.GOLD + ChatColor.BOLD + 1 + "位 ";
			else if(i == 1) rank = "" + ChatColor.GRAY + ChatColor.BOLD + 2 + "位 ";
			else if(i == 2) rank = "" + ChatColor.YELLOW + ChatColor.BOLD + 3 + "位 ";
			else rank = "" + ChatColor.WHITE + i;
			
			Bukkit.broadcastMessage(rank + ChatColor.RESET + topList.keySet().toArray()[i] + ChatColor.GREEN + " " + topList.get(topList.keySet().toArray()[i]) + "レベル");
		}
		
		Bukkit.broadcastMessage(ChatColor.AQUA + "--------------------------------------");
	}
}