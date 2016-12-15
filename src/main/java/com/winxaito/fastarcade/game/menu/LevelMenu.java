package com.winxaito.fastarcade.game.menu;

import com.winxaito.fastarcade.game.menu.components.Button;
import org.lwjgl.opengl.Display;

import com.winxaito.fastarcade.game.Game;

public class LevelMenu extends Menu{
	public enum LevelList{
		Level1("1", 0, 1, new Button(Display.getWidth() / 2, 50, "Jouer 1"), new Button(Display.getWidth() / 2, 150, "Supprimer")),
		LevelFirst("first", 1, 2, new Button(Display.getWidth() / 2, 300, "Jouer First"), new Button(Display.getWidth() / 2, 400, "Supprimer"));
		
		protected String name;
		protected int id;
		protected int difficult;
		protected Button buttonPlay;
		protected Button buttonRemove;
		
		LevelList(String name, int id, int difficult, Button play, Button remove){
			this.name = name;
			this.id = id;
			this.difficult = difficult;
			this.buttonPlay = play;
			this.buttonRemove = remove;
		}
	}
	
	public LevelMenu(Game game){
		super(game);
	}

	@Override
	public void load(){

	}

	public void update(){
		for(LevelList level : LevelList.values()){
			level.buttonPlay.update();
			level.buttonRemove.update();
			
			if(level.buttonPlay.isClick()){
				game.loadLevel(level.name);
			}else if(level.buttonRemove.isClick()){
				System.out.println("REMOVE LEVEL");
			}
		}
	}
	
	public void render(){
		for(LevelList level : LevelList.values()){
			level.buttonPlay.render();
			level.buttonRemove.render();
		}
	}
}
