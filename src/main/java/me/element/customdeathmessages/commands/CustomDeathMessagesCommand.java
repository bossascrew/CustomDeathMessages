package me.element.customdeathmessages.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import me.element.customdeathmessages.CustomDeathMessages;

public class CustomDeathMessagesCommand implements CommandExecutor, TabCompleter 
{

	private CustomDeathMessages plugin;

	public CustomDeathMessagesCommand (CustomDeathMessages plugin) 
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender.hasPermission("cdm.reload"))
		{
			if (args.length == 1) 
			{
				if (args[0].equalsIgnoreCase("reload"))
				{
					plugin.reloadConfig();
					plugin.saveConfig();
					sender.sendMessage(ChatColor.GREEN + "Configuration has been reloaded.");
				} 
				else 
				{
					sender.sendMessage(ChatColor.RED + "Incorrect Syntax. Correct Usage:");
					sender.sendMessage(ChatColor.RED + "To reload: /cdm reload");
					sender.sendMessage(ChatColor.RED + "To add messages to config: /cdm add [path] [message]");
					sender.sendMessage(ChatColor.RED + "To list messages: /cdm list [path]");
					sender.sendMessage(ChatColor.RED + "To remove messages: /cdm remove [path] [number]");
				}
			}
			else if (args.length >= 3)
			{
				if (sender.hasPermission("cdm.add.message"))
				{
					if (args[0].equalsIgnoreCase("add"))
					{
						String path = args[1];

						String[] deathMessage = String.join(" ", args).split(" ", 3);

						String message = deathMessage[2];

						if (plugin.getConfig().getString(path) != null)
						{
							List<String> messages = plugin.getConfig().getStringList(path);
							messages.add(message);
							plugin.getConfig().set(path, messages);
							plugin.saveConfig();
							plugin.reloadConfig();
						}
						else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis is an invalid path!"));


						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour message: \"" + message + "\" &chas been added to config under &4" + path + "."));

						return true;
					}
					else if (args[0].equalsIgnoreCase("remove"))
					{
						if (sender.hasPermission("cdm.remove.message"))
						{
							String path = args[1];

							try 
							{
								int listNumber = Integer.valueOf(args[2]) - 1;

								if (plugin.getConfig().getString(path) != null)
								{
									List<Integer> list = new ArrayList<Integer>();

									for (int i = 0; i < plugin.getConfig().getStringList(path).size(); i++)
									{
										list.add(i + 1);
									}

									try
									{
										List<String> messages = plugin.getConfig().getStringList(path);

										String removed = messages.get(listNumber);

										if (messages.size() > 1)
										{
											messages.remove(listNumber);
											plugin.getConfig().set(path, messages);
											plugin.saveConfig();
											plugin.reloadConfig();
											sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe message \"" + removed + "\"" + " &c has been removed from &4" + path));
										}
										else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must add a message before you can remove the last one!"));
									}
									catch (IndexOutOfBoundsException e)
									{
										sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis is an invalid number! Options are " + list.toString()));
									}
								}
								else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis is an invalid path!"));
							}
							catch (NumberFormatException e)
							{
								if (plugin.getConfig().getString(path) != null)
								{
									List<Integer> list = new ArrayList<Integer>();

									for (int i = 0; i < plugin.getConfig().getStringList(path).size(); i++)
									{
										list.add(i + 1);
									}
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis is an invalid number! Options are " + list.toString()));
								}
								else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis is an invalid path!"));
							}
						}
						else sender.sendMessage(ChatColor.RED + "You do not have permission.");

						return true;
					}
					else 
					{
						sender.sendMessage(ChatColor.RED + "Incorrect Syntax. Correct Usage:");
						sender.sendMessage(ChatColor.RED + "To reload: /cdm reload");
						sender.sendMessage(ChatColor.RED + "To add messages to config: /cdm add [path] [message]");
						sender.sendMessage(ChatColor.RED + "To list messages: /cdm list [path]");
						sender.sendMessage(ChatColor.RED + "To remove messages: /cdm remove [path] [number]");
					}
				}
				else sender.sendMessage(ChatColor.RED + "You do not have permission.");
			}
			else if (args.length == 2)
			{
				if (sender.hasPermission("cdm.list"))
				{
					if (args[0].equalsIgnoreCase("list"))
					{
						String path = args[1];

						if (plugin.getConfig().getStringList(path).size() != 0)
						{
							int listSize = plugin.getConfig().getStringList(path).size();

							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6-- Messages --"));
							for (int i = 0; i < listSize; i++)
							{
								int number = i + 1;
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + number +". " + plugin.getConfig().getStringList(path).get(i)));
							}
						}
						else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis is an invalid path!"));

						return true;
					}
				}
				else sender.sendMessage(ChatColor.RED + "You do not have permission.");

				if (sender.hasPermission("cdm.toggle"))
				{
					if (args[0].equalsIgnoreCase("toggle"))
					{
						String path = args[1];
						boolean value = !plugin.getConfig().getBoolean(path);

						if (plugin.getConfig().isSet(path))
						{
							plugin.getConfig().set(path, value);
							plugin.saveConfig();
							plugin.reloadConfig();

							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Set " + path + " to &c" + value + "!"));
						}
						else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis is an invalid path!"));

						return true;
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "Incorrect Syntax. Correct Usage:");
						sender.sendMessage(ChatColor.RED + "To reload: /cdm reload");
						sender.sendMessage(ChatColor.RED + "To add messages to config: /cdm add [path] [message]");
						sender.sendMessage(ChatColor.RED + "To list messages: /cdm list [path]");
						sender.sendMessage(ChatColor.RED + "To remove messages: /cdm remove [path] [number]");
					}
				} 
				else sender.sendMessage(ChatColor.RED + "You do not have permission.");
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Incorrect Syntax. Correct Usage:");
				sender.sendMessage(ChatColor.RED + "To reload: /cdm reload");
				sender.sendMessage(ChatColor.RED + "To add messages to config: /cdm add [path] [message]");
				sender.sendMessage(ChatColor.RED + "To list messages: /cdm list [path]");
				sender.sendMessage(ChatColor.RED + "To remove messages: /cdm remove [path] [number]");
			}
			return false;
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "You do not have permission");
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] args) 
	{

		List<String> completionsOne = new ArrayList<String>();
		List<String> completionsTwo = new ArrayList<String>();
		List<String> completionsTwoTwo = new ArrayList<String>();
		List<String> completionsThreeOne = new ArrayList<String>();
		List<String> completionsThreeTwo = new ArrayList<String>();

		List<String> paths = new ArrayList<String>();
		List<String> booleans = new ArrayList<String>();

		String[] one = {"reload", "add", "list", "remove", "toggle"};
		String[] threeOne = {"<message>"};
		String[] threeTwo = {"<number>"};

		for (String path : plugin.getConfig().getKeys(false))
		{
			if (path.contains("message") && !path.equalsIgnoreCase("enable-global-messages") && !path.equalsIgnoreCase("killer-message") && !path.equalsIgnoreCase("victim-message"))
			{
				paths.add(path);
			}

			booleans.add("enable-update-messages");
			booleans.add("enable-pvp-messages");
			booleans.add("do-lightning");
			booleans.add("enable-global-messages");
			booleans.add("original-hover-message");
			booleans.add("enable-item-hover");
			booleans.add("enable-custom-name-entity-messages");

		}

		String[] two = paths.toArray(new String[paths.size()]);
		String[] twoTwo = booleans.toArray(new String[paths.size()]);

		if (args.length == 1)
		{
			StringUtil.copyPartialMatches(args[0], Arrays.asList(one), completionsOne);
			Collections.sort(completionsOne);
			return completionsOne;
		}
		else if (args.length == 2)
		{
			if (!args[0].equalsIgnoreCase("reload"))
			{
				if (args[0].equalsIgnoreCase("toggle"))
				{
					StringUtil.copyPartialMatches(args[1], Arrays.asList(twoTwo), completionsTwoTwo);
					Collections.sort(completionsTwoTwo);
					return completionsTwoTwo;
				}
				else
				{
					StringUtil.copyPartialMatches(args[1], Arrays.asList(two), completionsTwo);
					Collections.sort(completionsTwo);
					return completionsTwo;
				}
			}
		}
		else if (args.length == 3)
		{
			if (!args[0].equalsIgnoreCase("reload") && !args[0].equalsIgnoreCase("list"))
			{
				if (args[0].equalsIgnoreCase("add"))
				{
					StringUtil.copyPartialMatches(args[2], Arrays.asList(threeOne), completionsThreeOne);
					Collections.sort(completionsThreeOne);
					return completionsThreeOne;
				}
				else if (args[0].equalsIgnoreCase("remove"))
				{
					StringUtil.copyPartialMatches(args[2], Arrays.asList(threeTwo), completionsThreeTwo);
					Collections.sort(completionsThreeTwo);
					return completionsThreeTwo;
				}
			}
		}

		return null;
	}
}