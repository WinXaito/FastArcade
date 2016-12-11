package com.winxaito.fastarcade.game.menu;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.level.Level;

public class Hud{
	private Level level;
	
	private TrueTypeFont font;
	private int fps;
	private int tps;
	
	private int boost;
	
	public Hud(Level level){
		Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
        font = new TrueTypeFont(awtFont, true);
        this.level = level;
	}
	
	public void update(){
		fps = Game.getFps();
		tps = Game.getTps();
		boost = level.getBoost();
	}
	
	public void render(){
		TextureImpl.bindNone();
		font.drawString(10, 10, "FPS: " + fps, Color.white);
		font.drawString(10, 30, "TPS: " + tps, Color.white);
		font.drawString(10, 50, "BOOST: " + boost, Color.white);
	}
}
