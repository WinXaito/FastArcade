package com.winxaito.fastarcade.render;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.Color;

public class Renderer{
	/**
	 * Rend un rectangle, contient glBegin et glEnd
	 * @param x float
	 * @param y float
	 * @param width int
	 * @param height int
	 * @param color Color
	 * @param xo int
	 * @param yo int
	 * @param size int
	 */
	public static void renderQuad(float x, float y, int width, int height, Color color, int xo, int yo, float size){
		glBegin(GL_QUADS);
			Renderer.quadData(x, y, width, height, color, xo, yo, size);
		glEnd();
	}
	
	/**
	 * Rend un rectangle (Entité ou Tile) - Sans glBegin et glEnd
	 * @param x float
	 * @param y float
	 * @param width int
	 * @param height int
	 * @param color Color
	 * @param xo int
	 * @param yo int
	 * @param size float
	 */
	public static void quadData(float x, float y, int width, int height, Color color, int xo, int yo, float size){
		glColor4f(color.r, color.g, color.b, color.a);
		
		glTexCoord2f((xo) / size, (yo) / size);
		glVertex2f(x, y);
		
		glTexCoord2f((1 + xo) / size, (yo) / size);
		glVertex2f(x + width, y);
		
		glTexCoord2f((1 + xo) / size, (1 + yo) / size);
		glVertex2f(x + width, y + height);
		
		glTexCoord2f((xo) / size, (1 + yo) / size);
		glVertex2f(x, y + height);
	}

	public static void renderTexture(int x, int y, int width, int height, Texture texture){
		texture.bind();
			renderQuad(x, y, width, height, Color.white, 0, 0, 0);
		texture.unbind();
	}
}
