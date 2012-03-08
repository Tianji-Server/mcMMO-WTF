package com.gmail.nossr50.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.gmail.nossr50.Users;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.datatypes.HUDType;
import com.gmail.nossr50.datatypes.HUDmmo;
import com.gmail.nossr50.datatypes.PlayerProfile;
import com.gmail.nossr50.datatypes.buttons.ButtonEscape;
import com.gmail.nossr50.datatypes.buttons.ButtonHUDStyle;
import com.gmail.nossr50.datatypes.buttons.ButtonPartyToggle;
import com.gmail.nossr50.datatypes.popups.PopupMMO;
import com.gmail.nossr50.spout.SpoutStuff;

public class mcSpoutScreenListener implements Listener
{
	mcMMO plugin = null;
	public mcSpoutScreenListener(mcMMO pluginx)
	{
		plugin = pluginx;
	}
	
	@EventHandler
	public void onButtonClick(ButtonClickEvent event) 
	{
		SpoutPlayer sPlayer = event.getPlayer();
		PlayerProfile PP = Users.getProfile(sPlayer);
		
		if(event.getButton() instanceof ButtonHUDStyle)
		{
			if(SpoutStuff.playerHUDs.containsKey(sPlayer))
			{
				SpoutStuff.playerHUDs.get(sPlayer).resetHUD();
				SpoutStuff.playerHUDs.remove(sPlayer);
				
				switch(PP.getHUDType())
				{
				case RETRO:
					PP.setHUDType(HUDType.STANDARD);
					break;
				case STANDARD:
					PP.setHUDType(HUDType.SMALL);
					break;
				case SMALL:
					PP.setHUDType(HUDType.DISABLED);
					break;
				case DISABLED:
					PP.setHUDType(HUDType.RETRO);
				}
				
				SpoutStuff.playerHUDs.put(sPlayer, new HUDmmo(sPlayer));
				
				SpoutStuff.playerScreens.get(sPlayer).updateButtons(PP);
			}
		} else if (event.getButton() instanceof ButtonEscape)
		{
			sPlayer.getMainScreen().closePopup();
		} else if (event.getButton() instanceof ButtonPartyToggle)
		{
			PP.togglePartyHUD();
			ButtonPartyToggle bpt = (ButtonPartyToggle)event.getButton();
			bpt.updateText(PP);
			SpoutStuff.playerHUDs.get(sPlayer).resetHUD();
			SpoutStuff.playerHUDs.get(sPlayer).initializeHUD(sPlayer);
		}
	}
	
	@EventHandler
	public void onScreenClose(ScreenCloseEvent event) 
	{
		if(event.getScreen() instanceof PopupMMO)
		{
			SpoutStuff.playerScreens.remove(event.getPlayer());
		}
	}
}