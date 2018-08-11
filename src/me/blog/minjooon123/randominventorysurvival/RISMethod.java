package me.blog.minjooon123.randominventorysurvival;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RISMethod {
	public static void InventorySwap(Player pl1, Player pl2) {
		ItemStack[] contentInventory = pl1.getInventory().getContents();
		ItemStack[] armourInventory = pl1.getInventory().getArmorContents();
		ItemStack[] saveContentInventory = new ItemStack[contentInventory.length];
		ItemStack[] saveArmourInventory = new ItemStack[armourInventory.length];
		for(int i=0 ; i<contentInventory.length ; i++) {
			if(contentInventory[i] != null) {
				saveContentInventory[i] = contentInventory[i].clone();
			}
		}
		for(int i=0 ; i< armourInventory.length ; i++) {
			if(armourInventory[i] != null) {
				saveArmourInventory[i] = armourInventory[i].clone();
			}
		}
		//PlayerInventory temp = null;
		//temp.setArmorContents(pl1.getInventory().getArmorContents());
		//temp.setContents(pl1.getInventory().getContents());
		
		//pl1.getInventory().clear();
		pl1.getInventory().setContents(pl2.getInventory().getContents());
		pl1.getInventory().setArmorContents(pl2.getInventory().getArmorContents());
		//pl1.updateInventory();
		//pl2.getInventory().clear();
		pl2.getInventory().setContents(saveContentInventory);
		pl2.getInventory().setArmorContents(saveArmourInventory);
		
		if(RandomInventorySurvival.broadcastSwap) {
			Bukkit.broadcastMessage(ChatColor.GOLD + pl1.getName()
					+ ChatColor.BLUE + "와 "
					+ ChatColor.GOLD + pl2.getName()
					+ ChatColor.BLUE + "의 인벤토리가 방금 교환되었습니다.");
		}
		pl1.sendMessage(ChatColor.RED + "당신의 인벤토리가 누군가와 교환되었습니다.");
		pl2.sendMessage(ChatColor.RED + "당신의 인벤토리가 누군가와 교환되었습니다.");
	}
	
	public static Player[] plListRearrange(Player[] pl) {
		for(int i=0 ; i<pl.length-1 ; i++) {
			if(pl[i] == null) {
				pl[i] = pl[i+1];
				pl[i+1] = null;
			}
		}
		return pl;
	}
}