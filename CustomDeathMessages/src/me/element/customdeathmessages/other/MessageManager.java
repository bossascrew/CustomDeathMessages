package me.element.customdeathmessages.other;

public class MessageManager {

	//  private CustomDeathMessages plugin;

	//	private EntityType entities[] = { EntityType.ARROW, EntityType.BEE,
	//			EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER,
	//			EntityType.DOLPHIN, EntityType.DRAGON_FIREBALL, EntityType.DROWNED,
	//			EntityType.ELDER_GUARDIAN, EntityType.ENDER_CRYSTAL, EntityType.ENDER_DRAGON,
	//			EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER,
	//			EntityType.EVOKER_FANGS, EntityType.FALLING_BLOCK, EntityType.FIREBALL,
	//			EntityType.FIREWORK, EntityType.GHAST, EntityType.GUARDIAN,
	//			EntityType.HOGLIN, EntityType.HUSK, EntityType.ILLUSIONER,
	//			EntityType.IRON_GOLEM, EntityType.LIGHTNING, EntityType.LLAMA,
	//			EntityType.LLAMA_SPIT, EntityType.MAGMA_CUBE, EntityType.PANDA,
	//			EntityType.PHANTOM, EntityType.PIGLIN, EntityType.PILLAGER,
	//			EntityType.PLAYER, EntityType.POLAR_BEAR, EntityType.PRIMED_TNT,
	//			EntityType.PUFFERFISH, EntityType.RAVAGER, EntityType.SHULKER,
	//			EntityType.SHULKER_BULLET, EntityType.SILVERFISH, EntityType.SKELETON,
	//			EntityType.SLIME, EntityType.SMALL_FIREBALL, EntityType.SPECTRAL_ARROW,
	//			EntityType.SPIDER, EntityType.SPLASH_POTION, EntityType.STRAY,
	//			EntityType.TRADER_LLAMA, EntityType.TRIDENT, EntityType.UNKNOWN,
	//			EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER,
	//			EntityType.WITHER_SKELETON, EntityType.WITHER_SKULL, EntityType.WOLF,
	//			EntityType.ZOGLIN, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER,
	//			EntityType.ZOMBIFIED_PIGLIN };
	//
	//	private HashMap<EntityType, List<String>> messages = new HashMap<EntityType, List<String>>();
	//
	//	public MessageManager(CustomDeathMessages plugin) 
	//	{ 
	//		this.plugin = plugin;
	//
	//		for (EntityType entity : entities) 
	//		{ 
	//			messages.put(entity, plugin.getConfig().getStringList(Paths.valueOf(entity.toString()).getPath())); 
	//		} 
	//	}
	//
	//	public String getMessage(EntityType messageType) 
	//	{ 
	//		Random rand = new Random(); 
	//		String msg = messages.get(messageType).get(rand.nextInt(messages.size()));
	//
	//		return HexChat.translateHexCodes(msg, plugin); 
	//	}
}
