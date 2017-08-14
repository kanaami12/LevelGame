package com.plugin.ftb.levelgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class MainUtils {
	
	//プレイヤーごとの経験値リスト
	public static HashMap<String, Integer> levelList = new HashMap<>();
	//トップ10(降順)のプレイヤーリスト
	public static HashMap<String,Integer> topList = new HashMap<>();
	
	//経験値を取得し、ソートする
	public static void getLevels() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			levelList.put(player.getName(), player.getLevel());
		}
		
		//降順でソートし、hashmapに代入
		topList = new HashMap<>();
		for(Entry<String,Integer> list : sort(levelList)) {
			topList.put(list.getKey(), list.getValue());
		}
	}
	
	//valueに応じて降順でソート
	private static List<Map.Entry<String,Integer>> sort(HashMap<String, Integer> hashMap) {
		//ソート後の配列を生成
        List<Map.Entry<String,Integer>> entries = 
              new ArrayList<Map.Entry<String,Integer>>(hashMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(
                  Entry<String,Integer> entry1, Entry<String,Integer> entry2) {
                return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
            }
        });
        
        //0~9番目の要素のみを返す
        int size = entries.size() >= 10 ? 10 : entries.size();
        return entries.subList(0, size);
	}
}
