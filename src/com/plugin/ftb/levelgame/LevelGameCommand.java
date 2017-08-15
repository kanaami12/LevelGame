package com.plugin.ftb.levelgame;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class LevelGameCommand implements CommandExecutor {

	private static String prefix = ChatColor.GRAY + "[LevelGame]" + ChatColor.RESET;
	public static boolean isNightVisionMode = false;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(prefix + "このコマンドはゲーム内から実行してください。");
			return true;
		}
		
		Player player = (Player)sender;
		
		//startコマンドを実行
		if(args.length == 2 && args[0].equalsIgnoreCase("start")) {
			//時間を設定
			MainScoreboard.min = Integer.parseInt(args[1]);
			//スコアボードを登録
			MainScoreboard.registerScoreboard();
			//タイマータスクをスタート
			MainScoreboard.startTimerTask();
			//スコアタスクをスタート
			MainScoreboard.startScoreTask(1);
			Bukkit.broadcastMessage(prefix + ChatColor.BOLD + ChatColor.GREEN + "ゲームスタート！");
			return true;
		}
		
		//setNightVisionコマンドを実行
		if(args.length == 1 && args[0].equalsIgnoreCase("setNightVisionMode")) {
			if(!isNightVisionMode) {
				//エフェクトを付与
				for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10000000, 1000000));
				}
				isNightVisionMode = true;
				player.sendMessage(prefix + "ナイトビジョンを有効にしました。");
			}else {
				//エフェクトを削除
				for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					if(onlinePlayer.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
						onlinePlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
					}
				}
				isNightVisionMode = false;
				player.sendMessage(prefix + "ナイトビジョンを無効にしました。");
			}
			return true;
		}
		
		sendCommands(player);
		return true;
	}

	private void sendCommands(Player player) {
		player.sendMessage(ChatColor.WHITE + "/levelgame" + ChatColor.RED + " start" + ChatColor.WHITE + " [分]\n"
						 + ChatColor.WHITE + "/levelgame" + ChatColor.RED + " setNightVisionMode");
	}
}
