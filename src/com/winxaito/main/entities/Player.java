package com.winxaito.main.entities;

import org.lwjgl.input.Keyboard;

import com.winxaito.main.game.level.Level;
import com.winxaito.main.render.Renderer;
import com.winxaito.main.render.Texture;
import com.winxaito.main.utils.Animation;

public class Player extends Entity{
	private int xo = 0, yo = 0;
	private int direction = 1;
	private Level level;
	int z = 0;
		
	/**
	 * Constructeur
	 * @param x
	 * @param y
	 * @param size
	 */
	public Player(int x, int y, int size, Level level){
		super(x, y, size, level);
		this.level = level;
		
		speed = 0.6f;
		mass = 0.2f;
		drag = 0.3f;
		texSize = 4.0f;
		
		anim = new Animation(3, 6, true);
	}
	
	@Override
	public void update(){
		anim.update();
		anim.pause();
		z = 0;
		ya += level.getGravity() * mass;
		
		if(isGrounded())
			drag = 0.85f;
		else
			drag = 0.9f;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && isGrounded()){
			//Jump (saut)
			ya -= 16f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			//Déplacement vers le haut
			if(level.getBoost() > 0){
				ya -= speed * 3;
				z = 1;
				level.setBoost(level.getBoost() - 1);
			}
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			//Déplacement vers le bas
			ya += speed;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			//Déplacement vers la droite
			anim.start();
			direction = 1;
			xa += speed;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			//Déplacement vers la gauche
			anim.start();
			direction = 2;
			xa -= speed;
		}
		
		
		int xStep = (int)Math.abs(xa * 10);
		
		for(int i = 0;i < xStep;i++){
			if(!isSolidTile(xa / xStep, 0))
				x += xa / xStep;
			else xa = 0;
		}
		
		int yStep = (int)Math.abs(ya * 10);
		
		for(int i = 0;i < yStep;i++){
			if(!isSolidTile(0, ya / yStep))
				y += ya / yStep;
			else
				ya = 0;
		}
		
		xa *= drag;
		ya *= drag;
	}

	@Override
	public void render(){
		Texture.texPlayer.bind();
		if(z == 0)
			Renderer.renderQuad(x, y, size, size, color, direction, anim.getCurrentFrame(), texSize);
		else
			Renderer.renderQuad(x, y, size, size, color, direction, 3, texSize);
	}
}
