package me.element.customdeathmessages.listeners;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.element.customdeathmessages.CustomDeathMessages;
import me.element.customdeathmessages.enums.VersionEnums;
import me.element.customdeathmessages.other.HexChat;
import me.element.customdeathmessages.other.JsonChat;
import me.element.customdeathmessages.other.MsgToJson;
import me.element.customdeathmessages.other.SkullCreator;

public class PlayerDeathListener implements Listener {

	public CustomDeathMessages plugin;

	public PlayerDeathListener (CustomDeathMessages plugin) 
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent event) 
	{
		Player victim = event.getEntity();
		Player killer = event.getEntity().getKiller();
		Location playerLocation = victim.getLocation();

		if (victim instanceof Player && killer instanceof Player && plugin.getConfig().getBoolean("enable-pvp-messages"))
		{
			victim.sendMessage(HexChat.translateHexCodes(plugin.getConfig().getString("victim-message")
					.replace("%killer%", victim.getName())
					.replace("%killer-nick%", victim.getDisplayName())
					.replace("%victim-x%", String.valueOf(victim.getLocation().getBlockX()))
					.replace("%victim-y%", String.valueOf(victim.getLocation().getBlockY()))
					.replace("%victim-z%", String.valueOf(victim.getLocation().getBlockZ()))
					.replace("%killer-x%", String.valueOf(killer.getLocation().getBlockX()))
					.replace("%killer-y%", String.valueOf(killer.getLocation().getBlockY()))
					.replace("%killer-z%", String.valueOf(killer.getLocation().getBlockZ())), plugin));


			killer.sendMessage(HexChat.translateHexCodes(plugin.getConfig().getString("killer-message")
					.replace("%victim%", victim.getName())
					.replace("%victim-nick%", victim.getDisplayName())
					.replace("%victim-x%", String.valueOf(victim.getLocation().getBlockX()))
					.replace("%victim-y%", String.valueOf(victim.getLocation().getBlockY()))
					.replace("%victim-z%", String.valueOf(victim.getLocation().getBlockZ()))
					.replace("%killer-x%", String.valueOf(killer.getLocation().getBlockX()))
					.replace("%killer-y%", String.valueOf(killer.getLocation().getBlockY()))
					.replace("%killer-z%", String.valueOf(killer.getLocation().getBlockZ())), plugin));
		}

		double percent = plugin.getConfig().getDouble("drop-head-percentage"); // percent to drop head
		{
			Random rand = new Random();
			double randomDouble = rand.nextDouble(); // used to see if percent is bigger

			if (randomDouble <= percent) { // calculate to drop or not

				String headName = HexChat.translateHexCodes(plugin.getConfig().getString("head-name")
						.replace("%victim%", victim.getName()), plugin);

				if (plugin.getConfig().getBoolean("developer-mode")) // for debugging
					Bukkit.broadcastMessage("HEAD DROPPED");

				ItemStack skull = SkullCreator.itemFromUuid(victim.getUniqueId(), plugin);
				SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
				skullMeta.setDisplayName(headName);
				skull.setItemMeta(skullMeta);

				playerLocation.getWorld().dropItemNaturally(victim.getLocation(), skull);
			}
		}

		if (plugin.getConfig().getBoolean("do-lightning")) 
		{
			playerLocation.getWorld().strikeLightningEffect(playerLocation);
		}

		if (plugin.getConfig().getBoolean("enable-global-messages")) 
		{
			if (killer instanceof Player)
			{
				ItemStack killWeapon = getKillWeapon(killer);

				if (killWeapon.getType() != Material.AIR)
				{
					String weaponName = killWeapon.getItemMeta().hasDisplayName() ? killWeapon.getItemMeta().getDisplayName() : WordUtils.capitalize(killWeapon.getType().name().replaceAll("_", " ").toLowerCase());
					Random rand = new Random();
					List<String> msgs = plugin.getConfig().getStringList("global-pvp-death-messages");

					String msg = msgs.get(rand.nextInt(msgs.size()))
							.replace("%victim%", victim.getName())
							.replace("%victim-nick%", victim.getDisplayName())
							.replace("%killer%", killer.getName())
							.replace("%killer-nick%", killer.getDisplayName())
							.replace("%victim-x%", String.valueOf(victim.getLocation().getBlockX()))
							.replace("%victim-y%", String.valueOf(victim.getLocation().getBlockY()))
							.replace("%victim-z%", String.valueOf(victim.getLocation().getBlockZ()))
							.replace("%killer-x%", String.valueOf(killer.getLocation().getBlockX()))
							.replace("%killer-y%", String.valueOf(killer.getLocation().getBlockY()))
							.replace("%killer-z%", String.valueOf(killer.getLocation().getBlockZ()));

					msg = HexChat.translateHexCodes(msg, plugin);

					if (plugin.getConfig().getBoolean("developer-mode"))
						Bukkit.broadcastMessage("msg test: " + msg);

					if (plugin.getConfig().getBoolean("enable-item-hover"))
					{
						event.setDeathMessage("");
						Bukkit.spigot().broadcast(new JsonChat().getTextComponent(msg, killWeapon, "kill-weapon"));
						return;
					}
					msg = msg.replace("%kill-weapon%", weaponName);
					event.setDeathMessage(msg);
				}
				else
				{
					Random rand = new Random();
					List<String> msgs = plugin.getConfig().getStringList("melee-death-messages");

					String msg = msgs.get(rand.nextInt(msgs.size()))
							.replace("%victim%", victim.getName())
							.replace("%victim-nick%", victim.getDisplayName())
							.replace("%killer%", killer.getName())
							.replace("%killer-nick%", killer.getDisplayName())
							.replace("%victim-x%", String.valueOf(victim.getLocation().getBlockX()))
							.replace("%victim-y%", String.valueOf(victim.getLocation().getBlockY()))
							.replace("%victim-z%", String.valueOf(victim.getLocation().getBlockZ()))
							.replace("%killer-x%", String.valueOf(killer.getLocation().getBlockX()))
							.replace("%killer-y%", String.valueOf(killer.getLocation().getBlockY()))
							.replace("%killer-z%", String.valueOf(killer.getLocation().getBlockZ()));

					msg = HexChat.translateHexCodes(msg, plugin);
					event.setDeathMessage(msg);
				}
			}
			else
			{
				int versionInt = plugin.getServerVersion().getVersionInt();

				DamageCause cause = DamageCause.CUSTOM;
				if (victim.getLastDamageCause() != null)
					cause = victim.getLastDamageCause().getCause();
				
				String path = null;

				if (cause == DamageCause.CUSTOM) {
					path = "unknown-messages";
				} else if (cause == DamageCause.FALL) {
					path = "fall-damage-messages";
				} else if (cause == DamageCause.DROWNING) { // added before supported v
					path = "drowning-messages";
				} else if (cause == DamageCause.LAVA) { // added before supported v
					path = "lava-messages";   		
				} else if (cause == DamageCause.SUFFOCATION) { // added before supported v
					path = "suffocation-messages";
				} else if (cause == DamageCause.WITHER) { // added before supported v
					path = "wither-messages";
				} else if (cause == DamageCause.FIRE_TICK) { // added before supported v
					path = "fire-tick-messages";
				} else if (cause == DamageCause.FIRE) { // added before supported v
					path = "fire-messages";
				} else if (cause == DamageCause.STARVATION) { // added before supported v
					path = "starvation-messages";
				} else if (cause == DamageCause.CONTACT) { // added before supported v
					path = "cactus-messages";
				} else if (cause == DamageCause.MAGIC) { // added before supported v
					path = "potion-messages";
				} else if (cause == DamageCause.VOID) { // added before supported v
					path = "void-messages";
				} else if (cause == DamageCause.LIGHTNING) { // added before supported v
					path = "lightning-messages";
				} else if (versionInt >= VersionEnums.VERSION_19.getVersionInt() && cause == DamageCause.FLY_INTO_WALL) { // 1.9
					path = "elytra-messages";
				} else if (versionInt >= VersionEnums.VERSION_110.getVersionInt() && cause == DamageCause.HOT_FLOOR) { // 1.10
					path = "magma-block-messages";
				} else if (cause == DamageCause.SUICIDE) { // added before supported v
					path = "suicide-messages";
				} 

				String msg = "";

				if (path == null)
				{
					if (plugin.deathMessage.get(victim.getName()) != null)
					{
						msg = HexChat.translateHexCodes(plugin.deathMessage.get(victim.getName()), plugin);
						plugin.deathMessage.clear();
					}
					else
					{
						Random rand = new Random();
						List<String> msgs = plugin.getConfig().getStringList("unknown-messages");
						msg = msgs.get(rand.nextInt(msgs.size()))
								.replace("%victim%", victim.getName())
								.replace("%victim-nick%", victim.getDisplayName())
								.replace("%victim-x%", String.valueOf(victim.getLocation().getBlockX()))
								.replace("%victim-y%", String.valueOf(victim.getLocation().getBlockY()))
								.replace("%victim-z%", String.valueOf(victim.getLocation().getBlockZ()));
						msg = HexChat.translateHexCodes(msg, plugin);
					}
				}
				else
				{
					Random rand = new Random();
					List<String> msgs = plugin.getConfig().getStringList(path);
					msg = msgs.get(rand.nextInt(msgs.size()))
							.replace("%victim%", victim.getName())
							.replace("%victim-nick%", victim.getDisplayName())
							.replace("%victim-x%", String.valueOf(victim.getLocation().getBlockX()))
							.replace("%victim-y%", String.valueOf(victim.getLocation().getBlockY()))
							.replace("%victim-z%", String.valueOf(victim.getLocation().getBlockZ()));
					msg = HexChat.translateHexCodes(msg, plugin);
				}
				if (plugin.getConfig().getBoolean("original-hover-message"))
				{
					String previous = event.getDeathMessage();
					Bukkit.spigot().broadcast((MsgToJson.translate(msg, previous)));
					event.setDeathMessage("");
				}
				else event.setDeathMessage(msg);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public ItemStack getKillWeapon(Player killer)
	{
		if (plugin.getServerVersion().getVersionInt() >= VersionEnums.VERSION_19.getVersionInt())
			return killer.getInventory().getItemInMainHand();
		else
			return killer.getInventory().getItemInHand();
	}
}
