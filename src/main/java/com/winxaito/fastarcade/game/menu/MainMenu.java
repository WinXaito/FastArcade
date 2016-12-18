package com.winxaito.fastarcade.game.menu;

import com.winxaito.fastarcade.game.menu.components.Button;
import com.winxaito.fastarcade.game.state.MenuState;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;

import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;

import java.util.ArrayList;

public class MainMenu extends Menu{
	private int mainButtonWidth = 250;
	private int mainButtonHeight = 50;

	private ArrayList<Button> buttons = new ArrayList<>();
	private Button buttonPlay;
	private Button buttonOptions;
	private Button buttonExit;
	
	private TrueTypeFont font;
	private Audio music;	
	
    public MainMenu(Game game){
    	super(game);
    }

    @Override
    public void load(){
		//Load buttons
		buttonPlay = new Button(50, "Jouer"){
			@Override
			public void onClick(Button button){
				MenuState.setState(MenuState.MenuStateList.LEVEL);
			}
		};

		buttonOptions = new Button(150, "Options"){
			@Override
			public void onClick(Button button){
				MenuState.setState(MenuState.MenuStateList.OPTIONS);
			}
		};

		buttonExit = new Button(250, "Quitter"){
			@Override
			public void onClick(Button button){
				game.exit();
			}
		};

		buttons.add(buttonPlay);
		buttons.add(buttonOptions);
		buttons.add(buttonExit);
	}

	@Override
    public void update(){
		buttons.forEach(Button::update);
    }

    @Override
    public void render(){
    	int xTiles = Display.getWidth() / 128;
    	int yTiles = Display.getHeight() / 128;
    	for(int x = 0;x < xTiles + 1;x++){
    		for(int y = 0;y < yTiles + 1;y++){
    			Texture.texMenuBackground.bind();
        		Renderer.renderQuad(x * 128, y * 128, 128, 128, Color.white, 0, 0, 2);
    		}
    	}

		buttons.forEach(Button::render);
    }
}
