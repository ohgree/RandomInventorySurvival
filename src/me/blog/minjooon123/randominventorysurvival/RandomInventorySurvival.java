package me.blog.minjooon123.randominventorysurvival;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class RandomInventorySurvival extends JavaPlugin {

	//public static RandomInventorySurvival risPlugin;
	public Player[] pl;
	public static Permission Permissions;

	/*****************************************************/
	public static boolean broadcastDeath;
	public static boolean broadcastKiller;
	public static boolean broadcastSwap;
	public static boolean isAxisShown;
	public static Long axisShownFrequency;
	public static Long killingTime;
	public static boolean banOnDeath;
	public static boolean enableSpectate;
	public static boolean autoWhitelist;
	public static boolean noDeathBeforeKillTime;
	public static boolean debugMode;
	public static int playerNum = 0;
	public static boolean shutUp;
	public static boolean disableKillTime;
	public static int swapSetDuration;
	public static int swapSetChance;
	/*
	 * public static int bowNormalDamageAdd; public static int
	 * bowCriticalChance; public static int bowCriticalDamage;
	 * 
	 * public static int bowMissChance; public static int bowMissDamage; public
	 * static boolean bowDistanceCriticalEnabled; public static int
	 * bowDistanceCriticalMinimalRange; public static int
	 * bowDistanceCriticalDivider; public static int bowArmorPenetration; public
	 * static int bowConfusionDuration; public static int bowBlindnessDuration;
	 * public static int swordCriticalDamage; public static int
	 * swordCriticalChance; public static int swordBlockChance; public static
	 * int fallDamageDivider; public static boolean giveItem; public static int
	 * swordChance; public static int arrowAmount; public static int
	 * stringAmount; public static int potionAmount; public static int
	 * goldenAppleAmount; public static int steakAmount; public static int
	 * flintAmount; public static int featherAmount; public static int
	 * gravelAmount; public static boolean beginOnStart; public static long
	 * AxisShownFrequency;
	 */
	/*****************************************************/

	public final Logger logger = Logger.getLogger("Minecraft");
	private final PlayerListener plListener = new PlayerListener(this);
	public static Player[] acceptedPlayers;
	FileConfiguration config;
	boolean isEnabled = false;
	public static boolean isKillingTime = false;
	//public static PluginDescriptionFile pdfFile = this.getDescription();

	//@EventHandler(priority = EventPriority.HIGHEST)
	/*
	 * private void givePlayerItem() { pl = new Player[Bukkit.getMaxPlayers()];
	 * pl = Bukkit.getOnlinePlayers(); acceptedPlayers = new
	 * Player[Bukkit.getMaxPlayers()]; int cnt = 0; for(int i=0 ; i<pl.length ;
	 * i++) { if(pl[i].hasPermission("bow.enable")) { acceptedPlayers[cnt] =
	 * pl[i]; cnt++;
	 * 
	 * pl[i].getPlayer().setHealth(20); pl[i].getPlayer().setFoodLevel(20);
	 * Random random = new Random(); int randomNum = random.nextInt(100); if(0
	 * <= randomNum && randomNum < swordChance) pl[i].getInventory().addItem(new
	 * ItemStack(Material.DIAMOND_SWORD, 1)); ItemStack item = new
	 * ItemStack(Material.POTION, potionAmount); item.setDurability((short)
	 * 8229); pl[i].getInventory().addItem(new ItemStack(Material.BOW, 1));
	 * pl[i].getInventory().addItem(new ItemStack(Material.ARROW, arrowAmount));
	 * pl[i].getInventory().addItem(new ItemStack(Material.STRING,
	 * stringAmount)); pl[i].getInventory().addItem(item);
	 * pl[i].getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE,
	 * goldenAppleAmount)); pl[i].getInventory().addItem(new
	 * ItemStack(Material.COOKED_BEEF, steakAmount));
	 * pl[i].getInventory().addItem(new ItemStack(Material.FLINT, flintAmount));
	 * pl[i].getInventory().addItem(new ItemStack(Material.FEATHER,
	 * featherAmount)); pl[i].getInventory().addItem(new
	 * ItemStack(Material.GRAVEL, gravelAmount)); } }
	 * Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE +
	 * "[Items had been supplied due to the config file settings]"); }
	 */
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[RandomInventorySurvival] Disabled RandomInventorySurvival v"
				+ pdfFile.getVersion());
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.getConfig().options().copyDefaults(true);
		broadcastDeath = this.getConfig().getBoolean("Notifications.Broadcast Death", true);
		broadcastKiller = this.getConfig().getBoolean("Notifications.Broadcast Killer", true);
		broadcastSwap = this.getConfig().getBoolean("Notifications.Broadcast Swap", false);
		isAxisShown = this.getConfig().getBoolean("Notifications.Show Axis Regularly", true);
		shutUp = this.getConfig().getBoolean("Notifications.Shut Up InfectedDuck!", false);
		axisShownFrequency = this.getConfig().getLong("Numerics.Axis Shown Frequency(Minutes)", 5);
		killingTime = this.getConfig().getLong("Numerics.Killing Time(Minimum of 5 minutes)", 30);
		disableKillTime = this.getConfig().getBoolean("Player.Disable Killing Time", false);
		banOnDeath = this.getConfig().getBoolean("Player.Ban On Death", false);
		enableSpectate = this.getConfig().getBoolean("Player.Enable Excluded Players To Spectate", true);
		autoWhitelist = this.getConfig().getBoolean("Player.Auto Whitelist", false);
		noDeathBeforeKillTime = this.getConfig().getBoolean("Player.Forgive Deaths Before Killing Time", false);
		swapSetDuration = this.getConfig().getInt("Secret Settings(WIP).Option 1", 30);
		swapSetChance = this.getConfig().getInt("Secret Settings(WIP).Option 2", 50);
		debugMode = this.getConfig().getBoolean("Debug.Debugging Mode", false);
		if(debugMode) {
			this.logger.warning("[RandomInventorySurvival] Now running in Debug Mode. Command: /ris debug");
		}
		/*
		 * bowNormalDamageAdd = this.getConfig().getInt("활 설정.활 추가데미지(반하트 = 1)",
		 * 6); bowCriticalChance = this.getConfig().getInt("활 설정.활 크리티컬 확률(%)",
		 * 40); bowCriticalDamage =
		 * this.getConfig().getInt("활 설정.활 크리티컬 데미지(반하트 = 1)", 28);
		 * bowMissChance = this.getConfig().getInt("활 설정.제압 확률(%)", 20);
		 * bowMissDamage =
		 * this.getConfig().getInt("활 설정.제압 당할 시 받는 데미지(반하트 = 1)", 2);
		 * bowDistanceCriticalEnabled =
		 * this.getConfig().getBoolean("활 설정.Bow_distanceCriticalEnabled",
		 * false); bowDistanceCriticalMinimalRange =
		 * this.getConfig().getInt("활 설정.Bow_distanceCriticalMinimalRange", 10);
		 * bowDistanceCriticalDivider =
		 * this.getConfig().getInt("활 설정.Bow_distanceCriticalDivider", 5);
		 * bowArmorPenetration = this.getConfig().getInt(
		 * "활 설정.방어구 관통 옵션(0 -> 없음, 1 -> 크리티컬시에만 관통, 2 -> 모든 방어구 관통)", 1);
		 * bowConfusionDuration =
		 * this.getConfig().getInt("활 설정.Bow_confusionDuration", 15);
		 * bowBlindnessDuration =
		 * this.getConfig().getInt("활 설정.Bow_blindnessDuration", 10);
		 * swordCriticalDamage =
		 * this.getConfig().getInt("칼 설정.다이아 칼 크리티컬 데미지량(반하트 = 1)", 40);
		 * swordCriticalChance =
		 * this.getConfig().getInt("칼 설정.다이아 칼 크리티컬 확률(%)", 80);
		 * swordBlockChance = this.getConfig().getInt("칼 설정.칼로 화살을 막는 확률(%)",
		 * 70); fallDamageDivider =
		 * this.getConfig().getInt("플레이어 설정.낙하 데미지 감소(\"낙뎀/설정값\" 으로 계산됩니다)", 2);
		 * giveItem = this.getConfig().getBoolean("아이템 설정.시작시 아이템 자동 지급", true);
		 * swordChance = this.getConfig().getInt("아이템 설정.다이아 칼 지급 확률(%)", 60);
		 * arrowAmount = this.getConfig().getInt("아이템 설정.화살 기본 지급량", 19);
		 * stringAmount = this.getConfig().getInt("아이템 설정.실 지급량", 3);
		 * potionAmount = this.getConfig().getInt("아이템 설정.체력 회복 포션 지급량", 1);
		 * goldenAppleAmount = this.getConfig().getInt("아이템 설정.황금사과 지급량", 3);
		 * steakAmount = this.getConfig().getInt("아이템 설정.스테이크 지급량", 5);
		 * flintAmount = this.getConfig().getInt("아이템 설정.부싯돌(화살 재료) 지급량", 5);
		 * featherAmount = this.getConfig().getInt("아이템 설정.깃털(화살 재료) 지급량", 3);
		 * gravelAmount = this.getConfig().getInt("아이템 설정.자갈(부싯돌 재료) 지급량", 20);
		 * beginOnStart =
		 * this.getConfig().getBoolean("기타.서버 시작시 자동시작(자동 아이템 지급 불가능)", false);
		 * AxisShownFrequency = this.getConfig().getLong("기타.플레이어 좌표 표시 간격(분)",
		 * 5);
		 */
		this.saveConfig();
		this.logger.info("[RandomInventorySurvival] Enabled RandomInventorySurvival v"
				+ pdfFile.getVersion());
		/*
		 * if(beginOnStart == true) { PluginManager pm =
		 * getServer().getPluginManager(); pm.registerEvents(ds, this);
		 * BowScheduler.info(this); if(AxisShownFrequency != 0) {
		 * BowScheduler.showAxis(this, AxisShownFrequency); } isEnabled = true;
		 * this.logger.info(
		 * "[Bow, the Ultimate Weapon] Autostarting \"Bow, the Ultimate Weapon\" v"
		 * + pdfFile.getVersion()); }
		 */
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		} // checks whether the sender is a player or a console
		if (cmd.getName().equals("ris")) {
			if (args.length == 0) { // no args -> Shows its usage
				if (player != null) {
					player.sendMessage(ChatColor.GREEN
							+ "[Random Inventory Survival]");
					// player.sendMessage(ChatColor.GREEN +
					// "제작: 감염오리(InfectedDuck) - minjooon123@naver.com");
					// player.sendMessage(ChatColor.GREEN +
					// "Skype ID: minjooon123");
					// player.sendMessage(ChatColor.GREEN +
					// "Blog: http://minjooon123.blog.me");
					player.sendMessage(ChatColor.RED + "Commands:");
					player.sendMessage(ChatColor.GOLD + "/ris help"
							+ ChatColor.GRAY + ": 플러그인에 대한 설명을 보여줍니다.");
					player.sendMessage(ChatColor.GOLD + "/ris start"
							+ ChatColor.GRAY + ": 플러그인을 시작합니다.");
					player.sendMessage(ChatColor.GOLD + "/ris exclude <PLAYER>"
							+ ChatColor.GRAY + ": 시작 후, 해당 플레이어를 제외시킵니다.");
					// player.sendMessage(ChatColor.GOLD + "/ris options" +
					// ChatColor.GRAY +": 플러그인에 대한 서버의 현재 설정값을 보여줍니다.");
					return true;
				} else {
					return false;
				}
			} else if (args.length == 1) {
				if (args[0].equals("help")) {
					if (player != null) { // Msg for Player
						player.sendMessage(ChatColor.GOLD
								+ "[Random Inventory Survival]");
						player.sendMessage(ChatColor.GREEN
								+ "맥북 산 기념으로 잉여력을 발현해보았습니다.");
						player.sendMessage(ChatColor.GREEN
								+ "랜덤으로 플레이어 둘의 인벤토리가 서로 바뀌게 됩니다.");
						player.sendMessage(ChatColor.GREEN
								+ "제작: 감염오리(minjooon123@naver.com)");
						player.sendMessage(ChatColor.GREEN
								+ "연락처: Skype - minjooon123");
						player.sendMessage(ChatColor.GREEN
								+ "제작자 블로그: http://minjooon123.blog.me");
						// player.sendMessage(ChatColor.RED + "\n명령어 설명:");
						// player.sendMessage(ChatColor.GOLD + "/bow help" +
						// ChatColor.GRAY + ": 플러그인에 대한 설명을 보여줍니다.");
						// player.sendMessage(ChatColor.GOLD + "/bow start" +
						// ChatColor.GRAY +
						// ": 플러그인을 시작합니다(\"/reload\" 명령어로 종료합니다.");
						// player.sendMessage(ChatColor.GOLD + "/bow options" +
						// ChatColor.GRAY +": 플러그인에 대한 서버의 현재 설정값을 보여줍니다.");
					} else { // Msg for cmd
						this.logger.info("[Random Inventory Survival]");
						this.logger.info("맥북 산 기념으로 잉여력을 발현해보았습니다.");
						this.logger.info("랜덤으로 플레이어 둘의 인벤토리가 서로 바뀌게 됩니다.");
						this.logger.info("제작: 감염오리(minjooon123@naver.com)");
						this.logger.info("연락처: Skype - minjooon123");
						this.logger.info("제작자 블로그: http://minjooon123.blog.me");
						// this.logger.info("\n명령어 설명:");
						// this.logger.info("/ris help: 플러그인에 대한 설명을 보여줍니다.");
						// this.logger.info("/bow start: 플러그인을 시작합니다(\"/reload\" 명령어로 종료합니다.");
						// this.logger.info("/bow exclude: 플러그인에 대한 서버의 현재 설정값을 보여줍니다.");
					}
					return true;
				} else if (args[0].equals("start")) {
					if (player != null) { // if the sender is a player
						if(debugMode) {
							player.sendMessage("디버그 모드가 켜져 있습니다. 설정 파일에서 디버그 모드를 비활성화해주세요.");
							return true;
						}
						if (player.hasPermission("ris.start") == true
								|| player.isOp()) {
							if (isEnabled == false) {
								if (Bukkit.getOnlinePlayers().length < 2) {
									player.sendMessage(ChatColor.RED
											+ "2명 이상이 있어야 게임을 시작할 수 있습니다.");
									return true;
								}
								
								PluginManager pm = getServer().getPluginManager();
								
								pl = new Player[Bukkit.getMaxPlayers()];
								pl = Bukkit.getOnlinePlayers();
								//playerNum = Bukkit.getOnlinePlayers().length;
								acceptedPlayers = new Player[Bukkit.getMaxPlayers()];
								//int cnt = 0;
								for(int i=0 ; i<pl.length ; i++) {
									if(!pl[i].hasPermission("ris.exclude")) {
										acceptedPlayers[playerNum] = pl[i];
										playerNum++;
									}
								}
								pm.registerEvents(plListener, this);
								if(!shutUp) RISScheduler.info(this);

								if(!disableKillTime)
									RISScheduler.KillingTimer(this, killingTime);
								else
									isKillingTime = true;

								//RISScheduler.InventorySwapTimer(this);
								Random random = new Random();
								int swapDuration;
								//if(RandomInventorySurvival.debugMode) {
								//	swapDuration = random.nextInt(3) + 1; //1~3
								//} else {
								swapDuration = random.nextInt(swapSetDuration*20*60) + 1;//0~20 Minutes
								//}// 5 ~ 30 Minutes
								BukkitTask task = new InventorySwapTask(this).runTaskLater(this, swapDuration);
								
								if(axisShownFrequency != 0 && isAxisShown) {
									RISScheduler.showAxis(this, axisShownFrequency);
								}
								
								if(autoWhitelist) {
									for(Player p: pl) {
										p.setWhitelisted(true);
									}
									Bukkit.setWhitelist(true);
								}
								
								// pm.registerEvents(ds, this);
								// BowScheduler.info(this);
								// if(AxisShownFrequency != 0) {
								// BowScheduler.showAxis(this,
								// AxisShownFrequency);
								// }
								// if(this.getConfig().getBoolean("ItemConfig.Item_giveItem",
								// true) == true){
								// givePlayerItem();
								// }
								Bukkit.broadcastMessage(ChatColor.YELLOW
										+ "플레이어 " + ChatColor.ITALIC
										+ player.getName() + ChatColor.YELLOW
										+ "(이)가 " + ChatColor.DARK_PURPLE
										+ "[랜덤 인벤토리 서바이벌] " + ChatColor.YELLOW
										+ "플러그인을 시작했습니다.");
								Bukkit.broadcastMessage(ChatColor.GOLD
										+ "플러그인 설명: " + ChatColor.LIGHT_PURPLE
										+ "/ris help");
								Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Made by. 감염오리");
								if(disableKillTime) Bukkit.broadcastMessage(ChatColor.AQUA
										+ "킬링타임이 바로 시작됩니다!");
								//Bukkit.broadcastMessage(ChatColor.RED + "지금 쓰시고 계신 0.0.1-SNAPSHOT 버전은 안정성을 이유로 배포를 불허합니다.\n"
								//		+ "문의사항은 minjooon123@naver.com으로 보내주시면 감사하겠습니다.");
								isEnabled = true;
							} else {
								player.sendMessage(ChatColor.RED
										+ "이미 플러그인이 실행중입니다. 멍청아.");
							}
							return true;
						}
						player.sendMessage(ChatColor.RED
								+ "넌 이 명령어를 위한 Permission이 없다. 어서 얻어와라.");
						return true;
					} else { //if sender is console
						if(debugMode) {
							this.logger.severe("디버그 모드가 켜져 있습니다. Config 파일에서 비활성화 해 주시기 바랍니다.");
							return true;
						}
						if (isEnabled == false) {
							if (Bukkit.getOnlinePlayers().length < 2) {
								this.logger.warning("적어도 2명이 있어야 시작이 가능하답니다.");
								return true;
							}
							PluginManager pm = getServer().getPluginManager();
							
							pl = new Player[Bukkit.getMaxPlayers()];
							pl = Bukkit.getOnlinePlayers();
							//playerNum = Bukkit.getOnlinePlayers().length;
							acceptedPlayers = new Player[Bukkit.getMaxPlayers()];
							//int cnt = 0;
							for(int i=0 ; i<pl.length ; i++) {
								if(!pl[i].hasPermission("ris.exclude")) {
									acceptedPlayers[playerNum] = pl[i];
									playerNum++;
								}
							}
							
							pm.registerEvents(plListener, this);
							if(!shutUp) RISScheduler.info(this);
							
							if(!disableKillTime)
								RISScheduler.KillingTimer(this, killingTime);
							else
								isKillingTime = true;

							//RISScheduler.InventorySwapTimer(this);
							Random random = new Random();
							int swapDuration;
							//if(RandomInventorySurvival.debugMode) {
							//	swapDuration = 1; //1 min
							//} else {
								swapDuration = random.nextInt(swapSetDuration*20*60) + 1;//0~20
							//}// 5 ~ 30 Minutes
							BukkitTask task = new InventorySwapTask(this).runTaskLater(this, swapDuration);
							
							if(axisShownFrequency != 0 && isAxisShown) {
								RISScheduler.showAxis(this, axisShownFrequency);
							}

							if(autoWhitelist) {
								for(Player p: pl) {
									p.setWhitelisted(true);
								}
								Bukkit.setWhitelist(true);
							}
							
							// pm.registerEvents(ds, this);
							// BowScheduler.info(this);
							// if(AxisShownFrequency != 0) {
							// BowScheduler.showAxis(this, AxisShownFrequency);
							// }
							// if(this.getConfig().getBoolean("ItemConfig.Item_giveItem",
							// true) == true){
							// givePlayerItem();
							// }
							Bukkit.broadcastMessage(ChatColor.YELLOW + "서버에서 "
									+ ChatColor.DARK_PURPLE + "[랜덤 인벤토리 서바이벌] "
									+ ChatColor.YELLOW + "플러그인을 시작했습니다.");
							Bukkit.broadcastMessage(ChatColor.GOLD
									+ "플러그인 설명: " + ChatColor.LIGHT_PURPLE
									+ "/ris help");
							Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Made by. 감염오리");
							if(disableKillTime) Bukkit.broadcastMessage(ChatColor.AQUA
									+ "킬링타임이 바로 시작됩니다!");
							//Bukkit.broadcastMessage(ChatColor.RED + "지금 쓰시고 계신 0.0.1-SNAPSHOT 버전은 안정성을 이유로 배포를 불허합니다.\n"
							//		+ "문의사항은 minjooon123@naver.com으로 보내주시면 감사하겠습니다.");
							isEnabled = true;
						} else {
							this.logger.warning("이미 플러그인이 시작되었습니다. 관리자놈아.");
						}
						return true;
					}
				} else if (args[0].equals("exclude")) {
					if(player!=null && !player.isOp()) {
						player.sendMessage(ChatColor.RED + "오피부터 얻고와라.");
					}
					else if (isEnabled == false) {
						if (player != null) {
							player.sendMessage(ChatColor.RED
									+ "/ris start로 플러그인 시작부터 해야한다.");
						} else {
							this.logger.info("먼저 플러그인을 /ris start로 시작해야 한다는걸 잊었니?");
						}
						return true;
					}
					else {
						if (args[1].isEmpty()) {
							if (player != null) {
								player.sendMessage(ChatColor.RED
										+ "/ris exclude <플레이어>");
								player.sendMessage(ChatColor.RED
										+ "제외할 플레이어 이름을 적으라고 멍충아.");
							} else {
								this.logger.info("/ris exclude <플레이어>");
								this.logger.info("제외할 플레이어 이름을 적으라고 멍청한 관리자 녀석아.");
							}
						}
						else if(!acceptedPlayers.toString().contains(args[1])) {//없는 플레이어 이름일 때
							if(player != null) {
								player.sendMessage(ChatColor.RED + "플레이어 이름을 똑바로 쓰셔야겠죠?");
							} else {
								this.logger.info("플레이어 이름을 똑바로 써야하지 않겠니?");
							}
						}
						else {
							for(int i=0 ; i<playerNum ; i++) {
								if(acceptedPlayers[i].getName().equals(args[1])) {

									String name = acceptedPlayers[i].getName();
									acceptedPlayers[i].setGameMode(GameMode.ADVENTURE);
									acceptedPlayers[i].setAllowFlight(true);
									acceptedPlayers[i].setFlying(true);
									acceptedPlayers[i].addPotionEffect(new PotionEffect(
											PotionEffectType.INVISIBILITY,
											Integer.MAX_VALUE, 0, true));
									
									acceptedPlayers[i] = null;
									
									playerNum--;
									
									acceptedPlayers = RISMethod.plListRearrange(acceptedPlayers);
									
									Bukkit.broadcastMessage(ChatColor.BLUE + name + ChatColor.AQUA + "님이 게임에서 제외되었습니다.");
									
									break;
								}
							}
						}
						/*
						 * if(player != null) {
						 * player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "플러그인 시작시 아이템 지급: " + ChatColor.GOLD + giveItem);
						 * player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "활 공격에 추가되는 데미지: " + ChatColor.GOLD +
						 * bowNormalDamageAdd + "(반하트)");
						 * player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "활 크리티컬 데미지: " + ChatColor.GOLD + bowCriticalDamage +
						 * "(반하트)"); player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "화살 회피 확률: " + ChatColor.GOLD + bowMissChance + "%");
						 * player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "화살 회피 시 받는 데미지: " + ChatColor.GOLD + bowMissDamage +
						 * "(반하트)"); player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "Whether distance-based critical is enabled: " +
						 * ChatColor.GOLD + bowDistanceCriticalEnabled);
						 * player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "다이아 검의 크리티컬 데미지: " + ChatColor.GOLD +
						 * swordCriticalDamage + "(반하트)");
						 * player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "다이아 검의 크리티컬 확률: " + ChatColor.GOLD +
						 * swordCriticalChance + "%");
						 * player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "칼로 화살을 막는 확률: " + ChatColor.GOLD + swordBlockChance
						 * + "%"); player.sendMessage(ChatColor.LIGHT_PURPLE +
						 * "방어구 관통 옵션: " + ChatColor.GOLD +
						 * bowArmorPenetration); } else {
						 * this.logger.info("플러그인 시작시 아이템 지급: " + giveItem);
						 * this.logger.info("활 공격에 추가되는 데미지: " +
						 * bowNormalDamageAdd + "(반하트)");
						 * this.logger.info("활 크리티컬 데미지: " + bowCriticalDamage +
						 * "(반하트)"); this.logger.info("화살 회피 확률: " +
						 * bowMissChance + "%");
						 * this.logger.info("화살 회피 시 받는 데미지: " + bowMissDamage +
						 * "(반하트)"); this.logger.info(
						 * "Whether distance-based critical is enabled: " +
						 * bowDistanceCriticalEnabled);
						 * this.logger.info("다이아 검의 크리티컬 데미지: " +
						 * swordCriticalDamage + "(반하트)");
						 * this.logger.info("다이아 검의 크리티컬 확률: " +
						 * swordCriticalChance + "%");
						 * this.logger.info("칼로 화살을 막는 확률: " + swordBlockChance
						 * + "%"); this.logger.info("방어구 관통 옵션: " +
						 * bowArmorPenetration); }
						 */
						return true;
					}
				} else if(args[0].equals("debug")) {
					if(!debugMode) {
						player.sendMessage(ChatColor.RED + "아니 이 커맨드를 어떻게 알았죠? 이 기능은 디버깅 용도입니다!");
					}
					else {
						player.sendMessage("<Debugging Mode>");
						if (isEnabled == false) {
							/*if (Bukkit.getOnlinePlayers().length < 2) {
								player.sendMessage(ChatColor.RED
										+ "2명 이상이 있어야 게임을 시작할 수 있습니다.");
								return true;
							}*/
							
							PluginManager pm = getServer().getPluginManager();
							
							pl = new Player[Bukkit.getMaxPlayers()];
							pl = Bukkit.getOnlinePlayers();
							acceptedPlayers = new Player[Bukkit.getMaxPlayers()];
							for(int i=0 ; i<pl.length ; i++) {
								//if(pl[i].hasPermission("ris.enable")) {
									acceptedPlayers[playerNum] = pl[i];
									playerNum++;
								//}
							}
							Bukkit.broadcastMessage("Players: " + playerNum);
							for(int i=0 ; i<playerNum ; i++) {
								Bukkit.broadcastMessage((i+1) + ". " + acceptedPlayers[i]);
							}
							
							pm.registerEvents(plListener, this);
							if(!shutUp) RISScheduler.info(this);

							if(!disableKillTime)
								RISScheduler.KillingTimer(this, 5L);
							else
								isKillingTime = true;
							//RISScheduler.InventorySwapTimer(this);
							
							Random random = new Random();
							int swapDuration;
							//if(RandomInventorySurvival.debugMode) {
								swapDuration = random.nextInt(1*60*20) + 1; //0Minutes ~ 1Minutes
							//} else {
							//	swapDuration = random.nextInt(25) + 5;
							//}// 5 ~ 30 Minutes
							BukkitTask task = new InventorySwapTask(this).runTaskLater(this, swapDuration);
							
							//if(axisShownFrequency != 0 && isAxisShown) {
								RISScheduler.showAxis(this, 1L);
							//}
							
							if(autoWhitelist) {
								for(Player p: pl) {
									p.setWhitelisted(true);
								}
								Bukkit.setWhitelist(true);
							}
							Bukkit.broadcastMessage(ChatColor.YELLOW
									+ "플레이어 " + ChatColor.ITALIC
									+ player.getName() + ChatColor.YELLOW
									+ "가 " + ChatColor.DARK_PURPLE
									+ "[랜덤 인벤토리 서바이벌] " + ChatColor.YELLOW
									+ "플러그인을 시작했습니다.");
							Bukkit.broadcastMessage(ChatColor.GOLD
									+ "플러그인 설명: " + ChatColor.LIGHT_PURPLE
									+ "/ris help");
							Bukkit.broadcastMessage("섞임 주기: 1분 | 킬링타임: 5분 | Axis Shown Frequency: 1분");
							isEnabled = true;
						} else {player.sendMessage("Plugin Has Already Been Started :P");}
					}
					return true;
				}
				/*
				 * else if(args[0].equals("stop")) { PluginManager pm =
				 * getServer().getPluginManager(); if(player != null) {
				 * if(player.hasPermission("bow.stop")){ if(isEnabled == true) {
				 * pm.disablePlugin(this); pm.enablePlugin(this);
				 * Bukkit.broadcastMessage(ChatColor.YELLOW +
				 * "[Bow, the Ultimate Weapon] The plugin was successfully deactivated by "
				 * + ChatColor.ITALIC + player.getName()); } else {
				 * player.sendMessage(ChatColor.RED +
				 * "The plugin has not been initiated yet.");
				 * player.sendMessage(ChatColor.RED + "Use " + ChatColor.BLUE +
				 * "/bow start" + " to initiate."); } return true; } } else {
				 * pm.disablePlugin(this); pm.enablePlugin(this);
				 * Bukkit.broadcastMessage(ChatColor.YELLOW +
				 * "[Bow, the Ultimate Weapon] The plugin was successfully deactivated by the console"
				 * ); } return true; }
				 */
			}
		}
		return false;
	}
}