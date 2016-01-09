package com.winxaito.main.entities;

import com.winxaito.main.game.level.Level;
import com.winxaito.main.render.Renderer;

public class Boost extends Entity{
	public Boost(int x, int y, int size, Level level){
		super(x, y, size, level);
	}

	@Override
	public void update(){
	}

	@Override
	public void render(){
		Renderer.renderQuad(x, y, size, size, color, 7, 1, texSize);
	}
}
