package com.winxaito.main.render;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture{
	public static final Texture texTiles = loadTexture("terrain", "png");
	public static final Texture texPlayer = loadTexture("player", "png");
	
	private int width;
	private int height;
	public int id;
	
	/**
	 * Constructeur (Utilisé par "loadTexture")
	 * @param width
	 * @param height
	 * @param id
	 */
	public Texture(int width, int height, int id){
		this.width = width;
		this.height = height;
		this.id = id;
	}
	
	/**
	 * Loader de texture
	 * @param name
	 * @param extension
	 * @return Texture
	 */
	public static Texture loadTexture(String name, String extension){
		BufferedImage image = null;
		try{
			//Chargement de la texture 
			image = ImageIO.read(Texture.class.getResource("/textures/" + name + "." + extension));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		//Création d'un tableau contenants les pixels de l'image
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int i = pixels[x + y * width];
				buffer.put((byte)((i >> 16) & 0xFF));
				buffer.put((byte)((i >> 8) & 0xFF));
				buffer.put((byte)((i) & 0xFF));
				buffer.put((byte)((i >> 24) & 0xFF));
			}
		}
		
		buffer.flip();
		
		//Génération de la texture OpenGL
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		System.out.println("Id de: " + name + " : " + id);
		
		//Retourne une nouvelle texture
		return new Texture(width, height, id);
	}
	
	/**
	 * Permet d'appliquer la texture
	 */
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	/**
	 * Enlève la texture
	 */
	public void unbind(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Getter largeur texture
	 * @return int
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * Getter hauteur texture
	 * @return int
	 */
	public int getHeight(){
		return height;
	}
}
