package me.element.customdeathmessages.other;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class MsgToJson {

	@SuppressWarnings("deprecation")
	public static TextComponent translate(String newDeathMessage, String originalDeathMessage)
	{
		TextComponent message = new TextComponent(TextComponent.fromLegacyText(newDeathMessage));
		TextComponent hoverBase = new TextComponent(TextComponent.fromLegacyText(originalDeathMessage));
		BaseComponent[] hoverComponent = new ComponentBuilder(hoverBase).create();
		HoverEvent hoverEvent = new HoverEvent(Action.SHOW_TEXT, hoverComponent);
		message.setHoverEvent(hoverEvent);
		
		return message;
	}
}
