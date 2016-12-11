package com.winxaito.main.game.menu;

import java.awt.Font;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

import com.winxaito.main.render.Renderer;
import com.winxaito.main.render.Texture;

public class Button{
	private int x = 0;
	private int y = 0;
	private int width = 300;
	private int height = 75;
	private boolean hover = false;
	private boolean click = false;
	
	private String text;
	private Color color = Color.white;
	private Color colorHover = Color.lightGray;
	private TrueTypeFont font;
	
	/**
	 * Constructeur
	 * @param x
	 * @param y
	 * @param text
	 */
	public Button(int x, int y, String text){
		this.text = text;
		this.x = x - width / 2;
		this.y = y - height / 2;
		
		init();
	}
	
	/**
	 * Constructeur surchargé
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param text
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
		if(Mouse.getX() > x && Mouse.getX() < x + width &&
				Mouse.getY() < Display.getHeight() - y && Mouse.getY() > Display.getHeight() - y - height){
			hover = true;
			
			if(Mouse.isButtonDown(0))
				click = true;
			else
				click = false;
		}else{
			hover = false;
			click = false;
		}
	}
	
	/**
	 * Rendu du bouton
	 */
	public void render(){
		//Render quad
		Texture.texMenuButton.bind();
		if(!hover)
			Renderer.renderQuad(x, y, width, height, color, 0, 0, 4);
		else
			Renderer.renderQuad(x, y, width, height, colorHover, 0, 0, 4);
		
		//Render text
		TextureImpl.bindNone();
		font.drawString(x + width / 2 - font.getWidth(text) / 2, y + height / 2 - font.getHeight(text) / 2, text, Color.black);
	}
	
	/**
	 * Retourne la position en X du bouton
	 * @return x
	 */
	public int getX(){
		return x;
	}

	/**
	 * @param x
	 */
	public void setX(int x){
		this.x = x - width / 2;
	}

	/**
	 * Retourne la position en Y du bouton
	 * @return y
	 */
	public int getY(){
		return y;
	}

	/**
	 * @param y
	 */
	public void setY(int y){
		this.y = y - height / 2;
	}

	/**
	 * Si la souris est sur le bouton
	 * @return hover
	 */
	public boolean isHover(){
		return hover;
	}

	/**
	 * Si la souris est sur le bouton et pressé (Click)
	 * @return click
	 */
	public boolean isClick(){
		return click;
	}
}
