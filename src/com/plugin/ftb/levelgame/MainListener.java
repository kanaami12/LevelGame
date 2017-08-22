package com.plugin.ftb.levelgame;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MainListener implements Listener{

	private Main plugin = Main.plugin;
	private static int[] random = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
								   2, 2, 2, 2, 2, 2, 2, 2, 2,
								   3, 3, 3, 3, 3, 3, 3, 3,
								   4, 4, 4, 4, 4, 4, 4,
								   5, 5, 5, 5, 5, 5,
								   6, 6, 6, 6, 6,
								   7, 7, 7, 7,
								   8, 8, 8,
								   9, 9,
								   10};
	
	//リスポーン時、エフェクトを付与
	@EventHandler
	public void onSpawn(PlayerRespawnEvent event) {
		if(LevelGameCommand.isNightVisionMode) {
			final Player player = event.getPlayer();
			 new BukkitRunnable() {
		            @Override
		            public void run() {
		            	//1秒後に実行
		            	player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10000000, 1000000));
		            }
		        }.runTaskLater(plugin, 60);
		}
	}
	
	//途中参加のプレイヤーにスコアボードをセット
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if(MainScoreboard.isPlaying) {
			final Player player = event.getPlayer();
			 new BukkitRunnable() {
		            @Override
		            public void run() {
		            	//1秒後に実行しないとエラー: Cannot set Scoreboard yet
		            	MainScoreboard.setScoreboard(player);
		            }
		        }.runTaskLater(plugin, 60);
		}
	}
	
	//発行した強い敵が現れる
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getEntity() instanceof Monster) {
			if(new Random().nextInt(100) < 10) {
				//1%
				LivingEntity entity = event.getEntity();
				//発光
				entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10000000, 1000000));
				entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10000000, new Random().nextInt(3) + 3));
			}
		}
	}
	
	//経験値ボトルを1~10個落とすようにする
	@EventHandler
	public static void onDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if(entity instanceof Monster) {
			//敵対mobのみ
			event.getDrops().add(new ItemStack(Material.EXP_BOTTLE, random[new Random().nextInt(random.length)]));
			if(entity.getPotionEffect(PotionEffectType.GLOWING) != null && entity.getPotionEffect(PotionEffectType.GLOWING).getAmplifier() > 0) {
				//強い敵
				if(entity.getKiller() != null) {
					//killerが存在する場合
					//2倍の経験値を付与
					entity.getKiller().giveExp(event.getDroppedExp());
				}
			}
		}
	}
}
