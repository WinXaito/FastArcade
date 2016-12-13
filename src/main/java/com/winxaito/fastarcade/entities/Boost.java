package com.winxaito.fastarcade.entities;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;
import com.winxaito.fastarcade.utils.Animation;

public class Boost extends Entity{
	private int quantity;
	private int texSize = 4;
	
	private Animation anim = new Animation(2, 30, true);
	private Audio audioBoost;
	
	public Boost(int x, int y, int size, Level level){
		super(x, y, size, level);
		this.quantity = 100;

		try{
			audioBoost = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("sounds/boost/boost.ogg"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Boost(int x, int y, int size, Level level, int quantity){
		super(x, y, size, level);
		this.quantity = quantity;
	}
	
	@Override
	public void update(){
		if(level.getPlayer().getX() > x - 42 && level.getPlayer().getX() < x + 42 &&
		   level.getPlayer().getY() > y - 42 && level.getPlayer().getY() < y + 42){
			level.setBoost(level.getBoost() + quantity);
			removed = true;
			audioBoost.playAsSoundEffect(1.0f, 0.2f, false);
		}
		
		anim.start();
		anim.update();
	}

	@Override
	public void render(){
		Texture.textBoost_100.bind();
		Renderer.renderQuad(x, y, size, size, color, anim.getCurrentFrame(), 0, texSize);
	}
}
