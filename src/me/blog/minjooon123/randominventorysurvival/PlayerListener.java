package me.blog.minjooon123.randominventorysurvival;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {
	
	public static RandomInventorySurvival plugin;
	public PlayerListener(RandomInventorySurvival instance) {
		plugin = instance;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(plugin.isEnabled) {
			if (RandomInventorySurvival.enableSpectate) {
				boolean temp = false;
				for(int i=0 ; i<RandomInventorySurvival.playerNum ; i++) {
					if (RandomInventorySurvival.acceptedPlayers[i].getName().equals(event.getPlayer().getName())) {
						RandomInventorySurvival.acceptedPlayers[i] = event.getPlayer();
						temp = true;
					}
				}
				if (!temp) {
					final Player p = event.getPlayer();
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							p.addPotionEffect(new PotionEffect(
									PotionEffectType.INVISIBILITY,
									Integer.MAX_VALUE, 0, true));
							p.setGameMode(GameMode.ADVENTURE);
							p.setAllowFlight(true);
							p.setFlying(true);
						}
					}, 1);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (RandomInventorySurvival.enableSpectate) {
			boolean temp = false;
			for(int i=0 ; i<RandomInventorySurvival.playerNum ; i++) {
				if(RandomInventorySurvival.acceptedPlayers[i].equals(event.getPlayer())) temp = true;
			}
			if(!temp) {
				final Player p = event.getPlayer();
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						p.addPotionEffect(new PotionEffect(
								PotionEffectType.INVISIBILITY,
								Integer.MAX_VALUE, 0, true));
						p.setGameMode(GameMode.ADVENTURE);
						p.setAllowFlight(true);
						p.setFlying(true);
					}
				}, 1);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if(RandomInventorySurvival.noDeathBeforeKillTime && !RandomInventorySurvival.isKillingTime) {
			event.setDeathMessage(ChatColor.RED + event.getEntity().getName()
					+ ChatColor.AQUA + "님께서 사망하였지만 아직 킬링타임이 아니므로 게임에서 제외되지 않았습니다."
					+ "\n죽은 장소로 다시 돌려보내드리진 않습니다. 능력껏 돌아가세요."); 
		}
		else {
			/*if(event.getEntity().getLastDamageCause() == DamageCause.FALL) {
				event.setDeathMessage()
			}
			else */
			if(!RandomInventorySurvival.broadcastDeath) {
				event.setDeathMessage("");
			}
			else if(!RandomInventorySurvival.broadcastKiller) {
				event.setDeathMessage(ChatColor.RED + event.getEntity().getName()
						+ ChatColor.AQUA + "님이 사망하였습니다. 짜이찌엔.");
			}
			else if(RandomInventorySurvival.broadcastKiller) {
				event.setDeathMessage(ChatColor.LIGHT_PURPLE + event.getEntity().getName() 
						+ ChatColor.AQUA + "님이 "
						+ ChatColor.GOLD + event.getEntity().getKiller().getName()
						+ ChatColor.AQUA + "님에 의해 잔인하게 살해당했습니다.");
			}

			//if (RandomInventorySurvival.enableSpectate) {
			//}
			
			if(RandomInventorySurvival.banOnDeath) {
				event.getEntity().kickPlayer("You Died. 유다희!");
				event.getEntity().setBanned(true);
			}

			for(int i=0 ; i<RandomInventorySurvival.playerNum ; i++) {
				if(RandomInventorySurvival.acceptedPlayers[i].equals(event.getEntity())) {
					RandomInventorySurvival.acceptedPlayers[i] = null;
					RandomInventorySurvival.acceptedPlayers = 
						RISMethod.plListRearrange(RandomInventorySurvival.acceptedPlayers);
					RandomInventorySurvival.playerNum--;
				}
			}
			if(RandomInventorySurvival.playerNum == 1) {
				Bukkit.broadcastMessage(ChatColor.GREEN + "-----------------------------------------------------");
				Bukkit.broadcastMessage(ChatColor.AQUA + "플레이어 "
						+ ChatColor.GOLD + RandomInventorySurvival.acceptedPlayers[0].getName()
						+ ChatColor.AQUA + "님이 마지막까지 살아남으셨으므로 승리하셨습니다!!!");
				Bukkit.broadcastMessage(ChatColor.GREEN + "-----------------------------------------------------");
						//+ ChatColor.GOLD + "플러그인을 다시 시작하려면 " + ChatColor.RED + "/reload"
						/*+ ChatColor.GOLD + "를 사용해주세요."*/
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onMonsterAggro(EntityTargetLivingEntityEvent event) {
		if(event.getTarget() instanceof Player && event.getEntity() instanceof Monster) {
			Player target = (Player)event.getTarget();
			boolean isAccepted = false;
			for(int i=0 ; i<RandomInventorySurvival.playerNum ; i++) {
				if(RandomInventorySurvival.acceptedPlayers[i].equals(target)) isAccepted = true;
			}
			if(!isAccepted) event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		if(event instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent ee = (EntityDamageByEntityEvent)event;
			/*if(ee.getDamager() instanceof Player){
				Arrow projectile =(Arrow) ee.getDamager();
				if(projectile.getShooter() instanceof Player){
					Player player = (Player) projectile.getShooter();
					if(ee.getEntity() instanceof LivingEntity){
						LivingEntity damagee = (LivingEntity) ee.getEntity();
						double distance = damagee.getLocation().distance(player.getLocation());
						int finalDamage = DamageCalc.getFinalDamage(distance, ee.getDamage(), player, damagee, true);
						if(Bow_the_ultimate_weapon.bowArmorPenetration == 0) {
							if(finalDamage == 0) {
								ee.setCancelled(true);
							}
							else {
								ee.setDamage(finalDamage);
								ee.setCancelled(false);
							}
						}
						else if(Bow_the_ultimate_weapon.bowArmorPenetration == 1) {
							if(finalDamage == 0) {
								ee.setCancelled(true);
							}
							else if(finalDamage == Bow_the_ultimate_weapon.bowCriticalDamage) {
								if(damagee.getHealth() - finalDamage < 0) {
									damagee.setHealth(0);
								}
								else {
									damagee.setHealth(damagee.getHealth()-finalDamage);
								}
								ee.setDamage(0);
								ee.setCancelled(false);
							}
							else {
								ee.setDamage(finalDamage);
								ee.setCancelled(false);
							}
						}
						else if(Bow_the_ultimate_weapon.bowArmorPenetration == 2) {
							if(finalDamage == 0) {
								ee.setCancelled(true);
							}
							else {
								if(damagee.getHealth() - finalDamage < 0) {
									damagee.setHealth(0);
								}
								else {
									damagee.setHealth(damagee.getHealth()-finalDamage);
								}
								ee.setDamage(0);
								ee.setCancelled(false);
							}
						}
					}
				}
			}*/
			// Player damaging other player Configuration - Permission Things
			if(!(ee.getDamager() instanceof Player) && ee.getEntity() instanceof Player) {
				Player damagee = (Player)ee.getEntity();
				boolean isAccepted = false;
				for(int i=0 ; i<RandomInventorySurvival.playerNum ; i++) {
					if(RandomInventorySurvival.acceptedPlayers[i].equals(damagee)) isAccepted = true;
				}
				if(!isAccepted) ee.setCancelled(true);
			}
			else if(ee.getDamager() instanceof Player && !(ee.getEntity() instanceof Player)) {
				Player damager = (Player)ee.getDamager();
				boolean isAccepted = false;
				for(int i=0 ; i<RandomInventorySurvival.playerNum ; i++) {
					if(RandomInventorySurvival.acceptedPlayers[i].equals(damager)) isAccepted = true;
				}
				if(!isAccepted) ee.setCancelled(true);
			}
			else if(ee.getDamager() instanceof Player && ee.getEntity() instanceof Player) {
				Player damager = (Player)ee.getDamager();
				Player damagee = (Player)ee.getEntity();
				int isAccepted = 0;
				for(int i=0 ; i<RandomInventorySurvival.playerNum ; i++) {
					if(RandomInventorySurvival.acceptedPlayers[i].equals(damager)) isAccepted++;
					if(RandomInventorySurvival.acceptedPlayers[i].equals(damagee)) isAccepted++;
				}
				if(isAccepted != 2) {
					ee.setCancelled(true);
				}
				else if(!RandomInventorySurvival.isKillingTime) {
					ee.setCancelled(true);
				}
				/*Material item = player.getItemInHand().getType();
				if(item.equals(Material.DIAMOND_SWORD)) {
					LivingEntity damagee = (LivingEntity) ee.getEntity();
					int swordDamage = DamageCalc.getFinalDamage(0, ee.getDamage(), player, damagee, false);
					if(swordDamage == 0) {
						ee.setCancelled(true);
					}
					else {
						ee.setDamage(swordDamage);
						ee.setCancelled(false);
					}
				}*/
			}
			if(ee.getDamager() instanceof Arrow && ee.getEntity() instanceof Player) {
				if(!RandomInventorySurvival.isKillingTime
						&& ((Arrow)ee.getDamager()).getShooter() instanceof Player) {
					ee.setCancelled(true);
				}
			}
		}
		/*if(event.getCause() == DamageCause.FALL ){
			EntityDamageEvent ee = (EntityDamageEvent)event;
			if(ee.getEntity() instanceof Player && ((Player)ee.getEntity()).hasPermission("bow.enable")) {
				int finalFallDamage = DamageCalc.getFallDamage(ee.getDamage());
				if(finalFallDamage == 0) {
					ee.setCancelled(true);
				}
				else {
					ee.setDamage(finalFallDamage);
					ee.setCancelled(false);
				}
			}
		}*/
	}
}