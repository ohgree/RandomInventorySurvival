package me.blog.minjooon123.randominventorysurvival;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class InventorySwapTask extends BukkitRunnable{
	private final JavaPlugin plugin;
	
	public InventorySwapTask(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		Random random = new Random();
		int swapDuration;
		if(RandomInventorySurvival.debugMode) {
			swapDuration = 1 * 60 * 20; //1~3
		} else {
			swapDuration = random.nextInt(RandomInventorySurvival.swapSetDuration*20*60) + 1;//0 ~ 30 Minutes
		}// 5 ~ 30 Minutes
		int swapChance = random.nextInt(100);// (WIP)true / false : 50%]
		if (swapChance < RandomInventorySurvival.swapSetChance
				|| RandomInventorySurvival.debugMode) {
			int randomInt1 = random.nextInt(RandomInventorySurvival.playerNum);
			int randomInt2 = random.nextInt(RandomInventorySurvival.playerNum);
			/*while(RandomInventorySurvival.acceptedPlayers[randomInt1] != null) {
				randomInt1 = random.nextInt(RandomInventorySurvival.acceptedPlayers.length);
			}*/
			while (randomInt1 == randomInt2) {
				if(RandomInventorySurvival.debugMode && RandomInventorySurvival.playerNum == 1) break;
				randomInt2 = random.nextInt(RandomInventorySurvival.playerNum);
			}
		
			RISMethod.InventorySwap(RandomInventorySurvival.acceptedPlayers[randomInt1],
					RandomInventorySurvival.acceptedPlayers[randomInt2]);//error
			if(RandomInventorySurvival.debugMode) {
				Bukkit.broadcastMessage(ChatColor.RED
						+ "[RIS-DEBUG] Swaped the inventory of "
						+ RandomInventorySurvival.acceptedPlayers[randomInt1].getName()
						+ " with "
						+ RandomInventorySurvival.acceptedPlayers[randomInt2].getName()
						+ ".");
			}
		}
		BukkitTask task = new InventorySwapTask(this.plugin).runTaskLater(this.plugin, swapDuration);
		//RISScheduler.InventorySwapTimer((JavaPlugin)RandomInventorySurvival);
			//recall InventorySwapTimer
		//}, 20L * 60L * swapDuration);
	}
}
