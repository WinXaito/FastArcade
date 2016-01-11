package com.winxaito.main.game.menu;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.winxaito.main.game.Game;
import com.winxaito.main.render.Renderer;
import com.winxaito.main.render.Texture;

public class LoadingMenu extends Menu{
	private String text;
	
	public LoadingMenu(Game game, String text){
		super(game);
		this.text = text;
	}

	@Override
	public void update(){
	}

	@Override
	public void render(){
		Texture.texMenuBackground.bind();
		Renderer.renderQuad(0, 0, Display.getWidth(), Display.getHeight(), Color.white, 0, 0, 16);
		drawStringCenter(Display.getWidth() / 2, Display.getHeight() / 2, text);
	}
}
