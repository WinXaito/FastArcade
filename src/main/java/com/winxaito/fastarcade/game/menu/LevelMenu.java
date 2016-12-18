package com.winxaito.fastarcade.game.menu;

import com.winxaito.fastarcade.game.menu.components.Button;
import com.winxaito.fastarcade.game.state.MenuState;
import com.winxaito.fastarcade.utils.keyboard.FaKeyboard;
import org.lwjgl.opengl.Display;

import com.winxaito.fastarcade.game.Game;

public class LevelMenu extends Menu{
	public enum LevelList{
		Level1("1", 0, 1, new LevelMenuGroup("1", 0)),
		LevelFirst("first", 1, 2, new LevelMenuGroup("first", 1));

		protected LevelMenu levelMenu;
		protected String name;
		protected int id;
		protected int difficult;
		protected LevelMenuGroup levelMenuGroup;
		
		LevelList(String name, int id, int difficult, LevelMenuGroup levelMenuGroup){
			this.name = name;
			this.id = id;
			this.difficult = difficult;
			this.levelMenuGroup = levelMenuGroup;
		}
	}
	
	public LevelMenu(Game game){
		super(game);

		for(LevelList level : LevelList.values())
			level.levelMenuGroup.setGame(game);
	}

	@Override
	public void load(){
	}

	public void update(){
		if(FaKeyboard.isKeyDown(FaKeyboard.Key.KEY_ESPACE))
			MenuState.setState(MenuState.MenuStateList.MAIN);

		for(LevelList level : LevelList.values())
			level.levelMenuGroup.update();
	}
	
	public void render(){
		for(LevelList level : LevelList.values())
			level.levelMenuGroup.render();
	}
}
