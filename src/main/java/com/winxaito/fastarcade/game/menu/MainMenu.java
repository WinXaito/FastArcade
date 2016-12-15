package com.winxaito.fastarcade.game.menu;

import com.winxaito.fastarcade.game.GameState;
import com.winxaito.fastarcade.game.menu.components.Button;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;

import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;

public class MainMenu extends Menu{
	private int mainButtonWidth = 250;
	private int mainButtonHeight = 50;
	
	private Button buttonPlay;
	private Button buttonOptions;
	private Button buttonExit;
	
	private TrueTypeFont font;
	private Audio music;	
	
    public MainMenu(Game game){
    	super(game);
    }

    public void load(){
    	//Load buttons
		buttonPlay = new Button(Display.getWidth() / 2, 50, "Jouer");
		buttonOptions = new Button(Display.getWidth() / 2, 150, "Options");
		buttonExit = new Button(Display.getWidth() / 2, 250, "Quitter");

		//Load home music
		/* TODO: Active music
		try{
			music = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("music/home.ogg"));
		}catch(IOException e){
			e.printStackTrace();
		}*/
	}

    public void update(){
    	//TODO: Active mudic
    	/*if(!music.isPlaying())
    		music.playAsMusic(1f, 0.8f, true);*/

    	buttonPlay.setX(Display.getWidth() / 2);
    	buttonPlay.update();
    	buttonOptions.setX(Display.getWidth() / 2);
    	buttonOptions.update();
    	buttonExit.setX(Display.getWidth() / 2);
    	buttonExit.update();
    	
    	while(Mouse.next()){
    		if (Mouse.getEventButtonState()) {
		    	if(buttonPlay.isClick()){
					//TODO: Remove -> GameState.setState(GameState.GameStateList.MENU_LEVEL);
					MenuState.setState(MenuState.MenuStateList.LEVEL);
		    	}else if(buttonOptions.isClick()){
					//GameState.setState(GameState.GameStateList.MENU_OPTIONS);
					MenuState.setState(MenuState.MenuStateList.OPTIONS);
		    	}else if(buttonExit.isClick()){
		    		game.exit();
		    	}
    		}
    	}
    }
    
    public void render(){
    	//Texture.texWhite.bind();
    	int xTiles = Display.getWidth() / 128;
    	int yTiles = Display.getHeight() / 128;
    	for(int x = 0;x < xTiles + 1;x++){
    		for(int y = 0;y < yTiles + 1;y++){
    			Texture.texMenuBackground.bind();
        		Renderer.renderQuad(x * 128, y * 128, 128, 128, Color.white, 0, 0, 2);
    		}
    	}   
    	
    	buttonPlay.render();
    	buttonOptions.render();
    	buttonExit.render(); 	
    }
}
