package com.winxaito.fastarcade.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.utils.Animation;

public abstract class Entity{
	protected int xo, yo, size;
	protected float x, y, xa, ya;
	protected float mass = 0.1f;
	protected float drag = 1f;
	protected float speed = 0.5f;
	protected float texSize = 16;
	protected boolean removed;
	
	protected Animation anim;
	protected Texture texture;
	protected Level level;
	protected Color color = Color.white;
	
	/**
	 * Constructeur
	 * @param x int
	 * @param y int
	 * @param size size
	 */
	public Entity(int x, int y, int size, Level level){
		this.x = x;
		this.y = y;
		this.size = size;
		this.level = level;
	}
	
	/**
	 * Update l'entité, appelé par update() de Level
	 */
	public abstract void update();
	
	/**
	 * Rend l'entité, appelé par render() de Level
	 */
	public abstract void render();

	/**
	 * Indique si la tile indiqué est solid ou non
	 * @param xa float
	 * @param ya float
     * @return boolean
     */
	public boolean isSolidTile(float xa, float ya){
		int x0 = (int)(x + xa) / size;
		int x1 = (int)(x + xa + size) / size;
		int y0 = (int)(y + ya) / size;
		int y1 = (int)(y + ya + size) / size;

		return (level.getSolidTile(x0, y0) != null)
				|| (level.getSolidTile(x1, y0) != null)
				|| (level.getSolidTile(x1, y1) != null)
				|| (level.getSolidTile(x0, y1) != null);
	}
	
	public boolean isGrounded() {
		return level.getSolidTile((int)(x + size / 2) / size, (int)(y + size + 0.1) / size) != null;
	}
	
	/**
	 * Getter position X
	 * @return x
	 */
	public float getX(){
		return x;
	}

	/**
	 * Setter position X
	 * @param x float
	 */
	public void setX(float x){
		this.x = x;
	}

	/**
	 * Getter position Y
	 * @return y
	 */
	public float getY(){
		return y;
	}

	/**
	 * Setter position Y
	 * @param y float
	 */
	public void setY(float y){
		this.y = y;
	}

	/**
	 * Getter size
	 * @return size
	 */
	public int getSize(){
		return size;
	}

	/**
	 * Setter size
	 * @param size int
	 */
	public void setSize(int size){
		this.size = size;
	}

	/**
	 * Getter xo (Offset en X de la texture -> SpriteSheet)
	 * @return xo
	 */
	public int getXo(){
		return xo;
	}

	/**
	 * Setter xo (Offset en X de la texture -> SpriteSheet)
	 * @param xo int
	 */
	public void setXo(int xo){
		this.xo = xo;
	}

	/**
	 * Getter yo (Offset en Y de la texture -> SpriteSheet)
	 * @return yo
	 */
	public int getYo(){
		return yo;
	}

	/**
	 * Getter yo (Offset en Y de la texture -> SpriteSheet)
	 * @param yo int
	 */
	public void setYo(int yo){
		this.yo = yo;
	}

	/**
	 * Getter xa ("Quantité" de déplacement du personnage)
	 * @return xa
	 */
	public float getXa(){
		return xa;
	}

	/**
	 * Setter xa ("Quantité" de déplacement du personnage)
	 * @param xa float
	 */
	public void setXa(float xa){
		this.xa = xa;
	}

	/**
	 * Getter ya ("Quantité" de déplacement du personnage)
	 * @return ya
	 */
	public float getYa(){
		return ya;
	}

	/**
	 * Setter ya ("Quantité" de déplacement du personnage)
	 * @param ya float
	 */
	public void setYa(float ya){
		this.ya = ya;
	}

	/**
	 * Getter vitesse de déplacement du personnage
	 * @return speed
	 */
	public float getSpeed(){
		return speed;
	}

	/**
	 * Setter vitesse de déplacement du personnage
	 * @param speed float
	 */
	public void setSpeed(float speed){
		this.speed = speed;
	}

	/**
	 * GETTER
	 * @return removed
	 */
	public boolean isRemoved(){
		return removed;
	}

	/**
	 * @param removed boolean
	 */
	public void setRemoved(boolean removed){
		this.removed = removed;
	}
}
