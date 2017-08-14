package com.plugin.ftb.levelgame;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MainListener implements Listener{

	private Main plugin = Main.plugin;
	
	//リスポーン時、エフェクトを付与
	@EventHandler
	public void onSpawn(PlayerRespawnEvent event) {
		if(LevelGameCommand.isNightVisionMode) {
			 new BukkitRunnable() {
		            @Override
		            public void run() {
		            	//1秒後に実行
		            	event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10000000, 1000000));
		            }
		        }.runTaskLater(plugin, 20);
		}
	}
}
