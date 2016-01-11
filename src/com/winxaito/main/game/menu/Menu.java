package com.winxaito.main.game.menu;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

import com.winxaito.main.game.Game;
import com.winxaito.main.render.Texture;

public abstract class Menu{
	protected int fontSize = 16;
	protected String fontName = "Times New Roman";
	
	protected TrueTypeFont font;
	protected Texture background;
	protected Game game;
	
	public Menu(Game game){
		this.game = game;
		initializeFont();
	}
	
	public Menu(Game game, int fontSize){
		this.game = game;
		this.fontSize = fontSize;
		initializeFont();
	}
	
	public Menu(Game game, String fontName, int fontSize){
		this.game = game;
		this.fontName = fontName;
		this.fontSize = fontSize;
		initializeFont();
	}
	
	private void initializeFont(){
		Font awtFont = new Font(fontName, Font.BOLD, 24);
    	font = new TrueTypeFont(awtFont, true);
	}
	
	public abstract void update();
	public abstract void render();
	
	protected void drawStringLeft(int x, int y, String text){
		TextureImpl.bindNone();
		font.drawString(x, y - font.getHeight(text) / 2, text);
	}
	
	protected void drawStringCenter(int x, int y, String text){
		TextureImpl.bindNone();
		font.drawString(x - font.getWidth(text) / 2, y - font.getHeight(text) / 2, text);
	}
	
	protected void drawStringRight(int x, int y, String text){
		TextureImpl.bindNone();
		font.drawString(x - font.getWidth(text), y - font.getHeight(text) / 2, text);
	}
}
