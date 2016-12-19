package com.winxaito.fastarcade.game.menu.hud;

import java.awt.Font;
import java.util.ArrayList;

import com.winxaito.fastarcade.entities.action.Action;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.level.Level;

public class MainHud{
	private Level level;
	private int yLoc;
	private int yLocTimer;

	private TrueTypeFont font;
	private TrueTypeFont fontTimer;
	private TrueTypeFont fontTitle;
	private int fps;
	private int tps;
	private int gameTick;
	private ArrayList<Action> activeActions;
	private String hudMessage;
	
	private int boost;
	private int point;
	
	public MainHud(Level level){
		this.level = level;

		Font awtFontTimer = new Font("Times New Roman", Font.BOLD, 30);
		Font awtFontTitle = new Font("Times New Roman", Font.BOLD, 24);
		Font awtFont = new Font("Times New Roman", 0, 20);
        fontTitle = new TrueTypeFont(awtFontTitle, true);
		font = new TrueTypeFont(awtFont, true);
		fontTimer = new TrueTypeFont(awtFontTimer, true);
	}
	
	public void update(){
		fps = Game.getFps();
		tps = Game.getTps();
		gameTick = Game.getCurrentTicks();
		boost = level.getBoost();
		point = level.getPoint();
		activeActions = level.getActiveActions();
		hudMessage = level.getHudMessage();
	}
	
	public void render(){
		TextureImpl.bindNone();
		yLoc = 10;

		addHudMessage(hudMessage);

		addString("General:", 10, true);
		addString("Fps: " + fps, 20, false);
		addString("Tps: " + tps, 20, false);
		addString("Game tick: " + gameTick, 20, false);
		addString("Boost: " + boost, 20, false);
		addString("Point: " + point, 20, false);

		addString("Actions:", 10, true);

		if(activeActions == null || activeActions.isEmpty())
			addString("None", 20, false);
		else
			for(Action action : activeActions)
				addString(action.getActionType().getName() + " - Time left: " + action.getTimeLeftFormat(), 20, false);

		yLoc = 0;
	}

	private void addHudMessage(String text){
		if(text == null)
			return;

		int x = Display.getWidth() / 2 - fontTimer.getWidth(text) / 2;
		fontTimer.drawString(x, 50, text, new Color(30,30,200));
	}

	private void addString(String text, int xLoc, boolean title){
		if(title){
			yLoc += 15;
			fontTitle.drawString(xLoc, yLoc, text, Color.white);
			yLoc += 5;
		}else{
			font.drawString(xLoc, yLoc, text, Color.white);
		}

		yLoc += 20;
	}
}
