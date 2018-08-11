package me.blog.minjooon123.randominventorysurvival;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;


public class RISScheduler{
	//public static RandomInventorySurvival risPlugin;
	
	final static String info[] = {
		"랜덤하게 걸린다는 것을 활용하시면 상대의 멘탈을 공략할 수 있습니다.",
		"버그가 없는게 버그입니다. 그러니까 버그 많다고 징징대지 말란 말이야.",
		"아무것도 안하고 꽃이나 캐는것도 전략이 될 수 있습니다. 다만 스왑이 안된다면 안습.",
		"저는 해당 플러그인의 사용으로 인한 여러분의 빡침을 보상해드리지 않습니다.",
		"스왑되어도 울지 마세요... 역전은 언제나 가능합니다.",
		"이 메세지는 간간히 유용하면서도 쓸데없는 정보들을 알려줍니다.",
//		"다 드루와",
		"킬링타임 전에는 플레이어 간의 어떠한 공격도 서로 먹히지 않습니다.",
		"Special Thanks to: 구글신, 더러운 이클립스(Feat. 한글 인코딩)",
		"코딩할땐 영어를 씁시다. 한글 문자 인코딩 그지같네요.",
//		"지금 사용하시고 계시는 빌드는 안정성을 이유로 배포가 불가능한 0.0.1-SNAPSHOT버전입니다.",
		"제작자의 죽은 블로그 - http://minjooon123.blog.me",
//		"（´▽｀）",
		"랜덤 인벤토리 서바이벌이 현재 실행중입니다.",
//		"요즘은 어떤 플러그인이 대세인가요?",
		"제작자의 잉여잉여함의 산물, 그 두번째.",
//		"제작자의 잉여잉여함의 산물, 그 두번째.",
//		"제작자의 잉여잉여함의 산물, 그 두번째.",
		"절 닥치게 하고 싶으시면 설정 파일에서 해당 옵션을 바꾸시면 됩니다.ㅠㅠ",
		"인벤토리가 바뀌어도 바뀐 사람 외에게는 스왑되었다는 사실을 알려주지 않습니다(기본 세팅 기준).",
		"맥북 산 기념으로 만듦.",
		"문의사항은 제작자 이메일(minjooon123@naver.com)이나 Skype ID - minjooon123으로 보내주세요.",
		"버그, 오타 발견하면 minjooon123@naver.com이나 Skype ID - minjooon123로 알려주세요. 근데 귀찮아서 안 고칠수도.",
		"이 플러그인은 1.6.4-R2.0 API를 기준으로 만들었습니다. 상위 버전 버킷에서도 호환될 수 있습니다.",
		"즐겜!",
		"아무도 안 물어본 제작자 연락처:\nTwitter: @InfectedDuck_KR | Skype: minjooon123",
		"코딩은 발로 해야 제맛."};
	public static void info(Plugin plugin) {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
						new Runnable() {
							public void run() {
								Random random = new Random();
								int randomNum = random.nextInt(info.length);
								Bukkit.broadcastMessage(ChatColor.BLUE
										+ "[TIP] " + ChatColor.AQUA
										+ info[randomNum]);
							}
						}, 20 * 20L, 20 * 200L); // 시작 후 20초 후, 3분 20초마다.
	}
	
	/*public static void InventorySwapTimer(Plugin plugin) {
		Random random = new Random();
		int swapDuration;
		if(RandomInventorySurvival.debugMode) {
			swapDuration = random.nextInt(3) + 1; //1~3
		} else {swapDuration = random.nextInt(25) + 5;}// 5 ~ 30 Minutes
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					Random rand = new Random();
					boolean swapChance = rand.nextBoolean();// true / false : 50%]
					if (swapChance) {
						int randomInt1 = rand.nextInt(RandomInventorySurvival.acceptedPlayers.length);
						int randomInt2 = rand.nextInt(RandomInventorySurvival.acceptedPlayers.length);
						while (randomInt1 != randomInt2) {
							if(RandomInventorySurvival.debugMode) break;
							randomInt2 = rand.nextInt(RandomInventorySurvival.acceptedPlayers.length);
						}

						RISMethod.InventorySwap(RandomInventorySurvival.acceptedPlayers[randomInt1],
								RandomInventorySurvival.acceptedPlayers[randomInt2]);
						if(RandomInventorySurvival.debugMode) {
							Bukkit.broadcastMessage("Swaped.");
						}
					}
					RISScheduler.InventorySwapTimer((JavaPlugin)RandomInventorySurvival);
					//recall InventorySwapTimer
				}
			}, 20L * 60L * swapDuration);
	}*/
	
	public static void KillingTimer(Plugin plugin, Long duration) {
		Bukkit.getServer().getScheduler()
				.scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						Bukkit.broadcastMessage(ChatColor.GOLD + "[공지] " + ChatColor.BLUE + "킬링타임이 시작되었습니다!");
						RandomInventorySurvival.isKillingTime = true;
					}
				}, 20L * 60L * duration);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				Bukkit.broadcastMessage(ChatColor.GOLD + "[공지] " + ChatColor.AQUA + "킬링타임까지 5분 남았습니다.");
			}
		}, 20L * 60L * (duration - 5));
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				Bukkit.broadcastMessage(ChatColor.GOLD + "[공지] "
							+ ChatColor.LIGHT_PURPLE + "킬링타임까지 1분 남았습니다.");
			}
		}, 20L * 60L * (duration - 1));
	}

	public static void showAxis(Plugin plugin, Long duration) {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
						new Runnable() {
							public void run() {
								Bukkit.broadcastMessage(ChatColor.YELLOW + "--------------현재 좌표--------------");
								for (int i = 0; i < RandomInventorySurvival.playerNum; i++) {
									Bukkit.broadcastMessage(ChatColor.DARK_GRAY + ""
											+ (i + 1) + ". "
											+ ChatColor.GOLD
											+ RandomInventorySurvival.acceptedPlayers[i].getName()
											//+ ": "
											+ ChatColor.WHITE + " - x: "
											+ RandomInventorySurvival.acceptedPlayers[i]
													.getLocation().getBlockX() + " y: "
											+ RandomInventorySurvival.acceptedPlayers[i]
													.getLocation().getBlockY() + " z: "
											+ RandomInventorySurvival.acceptedPlayers[i]
													.getLocation().getBlockZ());
									if(RandomInventorySurvival.acceptedPlayers[i+1] == null) {
										break;
									}
								}
								Bukkit.broadcastMessage(ChatColor.YELLOW + "----------------------------------");
							}
						}, 20L * 60L * duration, 20L * 60L * duration); // 시작 후 5분, 5분마다.
	}
}
