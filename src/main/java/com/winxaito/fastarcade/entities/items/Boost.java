package com.winxaito.fastarcade.entities.items;

import java.io.IOException;

import com.winxaito.fastarcade.entities.Entity;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;
import com.winxaito.fastarcade.utils.Animation;

public class Boost extends Entity{
	private BoostType boostType;
	private int texSize = 4;
	
	private Animation anim = new Animation(2, 30, true);
	private Audio audioBoost;

	public enum BoostType{
		B50(50),
		B100(100);

		private int quantity;

		BoostType(int quantity){
			this.quantity = quantity;
		}

		public int getQuantity(){
			return quantity;
		}
	}
	
	public Boost(int x, int y, int size, Level level){
		super(x, y, size, level);
		this.boostType = BoostType.B50;

		try{
			audioBoost = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("sounds/boost/boost.ogg"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Boost(int x, int y, int size, Level level, BoostType boostType){
		super(x, y, size, level);
		this.boostType = boostType;
	}
	
	@Override
	public void update(){
		if(level.getPlayer().getX() > x - 42 && level.getPlayer().getX() < x + 42 &&
		   level.getPlayer().getY() > y - 42 && level.getPlayer().getY() < y + 42){
			level.setBoost(level.getBoost() + boostType.getQuantity());
			removed = true;
			audioBoost.playAsSoundEffect(1.0f, 0.2f, false);
		}
		
		anim.start();
		anim.update();
	}

	@Override
	public void render(){
		Texture.textBoost.bind();
		Renderer.renderQuad(x, y, size, size, color, anim.getCurrentFrame(), 0, texSize);
	}
}
