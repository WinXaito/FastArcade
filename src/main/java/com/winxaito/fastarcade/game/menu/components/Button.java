package com.winxaito.fastarcade.game.menu.components;

import java.awt.Font;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;

public abstract class Button implements IButton{
	private int x = 0;
	private int y = 0;
	private int relativeX = 0;
	private int relativeY = 0;
	private int width = 300;
	private int height = 75;
	private boolean center = false;
	private boolean hover = false;
	private boolean click = true;
	
	private String text;
	private Color color = Color.white;
	private Color colorHover = Color.lightGray;
	private TrueTypeFont font;
	
	/**
	 * Constructor
	 * @param x int
	 * @param y int
	 * @param text String
	 */
	public Button(int x, int y, String text){
		this.text = text;
		this.x = x - width / 2;
		this.y = y - height / 2;
		
		init();
	}

	/**
	 * Constructor
	 * @param y int String
	 * @param text String
     */
	public Button(int y, String text){
		this.text = text;
		this.y = y - height / 2;

		center = true;
		x = Display.getWidth() / 2 - width / 2;

		init();
	}
	
	/**
	 * Constructor
	 * @param x int
	 * @param y int
	 * @param width int
	 * @param height int
	 * @param text String
	 */
	public Button(int x, int y, int width, int height, String text){
		this.x = x - width / 2;
		this.y = y - height / 2;
		this.width = width;
		this.height = height;
		this.text = text;
		
		init();
	}
	
	/**
	 * Initialisation du menu
	 */
	private void init(){
		Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
    	font = new TrueTypeFont(awtFont, true);
	}
	
	/**
	 * Update du bouton
	 */
	public void update(){
		if(center)
			x = Display.getWidth() / 2 - width / 2;

		if(Mouse.getX() > x && Mouse.getX() < x + width &&
				Mouse.getY() < Display.getHeight() - y && Mouse.getY() > Display.getHeight() - y - height){
			hover = true;
			onStartHover(this);

			if(!click){
				click = Mouse.isButtonDown(0);

				if(click)
					onClick(this);
			}
		}else{
			hover = false;
			click = false;
			onStopHover(this);
		}
	}
	
	/**
	 * Rendu du bouton
	 */
	public void render(){
		//Render quad
		Texture.texMenuButton.bind();
		if(!hover)
			Renderer.renderQuad(x + relativeX, y + relativeY, width, height, color, 0, 0, 4);
		else
			Renderer.renderQuad(x + relativeX, y + relativeY, width, height, colorHover, 0, 0, 4);
		
		//Render text
		TextureImpl.bindNone();
		font.drawString(x + width / 2 - font.getWidth(text) / 2 + relativeX, y + height / 2 - font.getHeight(text) / 2 + relativeY, text, Color.black);
	}
	
	/**
	 * Retourne la position en X du bouton
	 * @return int
	 */
	public int getX(){
		return x;
	}

	/**
	 * @param x int
	 */
	public void setX(int x){
		this.x = x - width / 2;
	}

	/**
	 * Retourne la position en Y du bouton
	 * @return int
	 */
	public int getY(){
		return y;
	}

	/**
	 * @param y int
	 */
	public void setY(int y){
		this.y = y - height / 2;
	}

	public int getRelativeX(){
		return relativeX;
	}

	public void setRelativeX(int relativeX){
		this.relativeX = relativeX;
	}

	public int getRelativeY(){
		return relativeY;
	}

	public void setRelativeY(int relativeY){
		this.relativeY = relativeY;
	}

	/**
	 * Si la souris est sur le bouton
	 * @return boolean
	 */
	public boolean isHover(){
		return hover;
	}

	/**
	 * Si la souris est sur le bouton et pressé (Click)
	 * @return click boolean
	 */
	public boolean isClick(){
		return click;
	}

	/**
	 * Si le bouton doit rester centré
	 * @return boolean
     */
	public boolean isCenter(){
		return center;
	}
}
