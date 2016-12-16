package com.winxaito.fastarcade.render;


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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

public class Texture{
	private static Logger logger = LogManager.getLogger(Texture.class);

	public static final Texture texWhite = loadTexture("white", "png");
	public static final Texture texMenuBackground = loadTexture("menuBackground", "png");
	public static final Texture texMenuButton = loadTexture("ButtonMenu", "png");
	public static final Texture textBoost_100 = loadTexture("boost", "png");
	public static final Texture texTiles = loadTexture("terrain", "png");
	public static final Texture texPlayer = loadTexture("player", "png");
	
	
	private int width;
	private int height;
	public int id;
	
	/**
	 * Constructeur (Utilisé par "loadTexture")
	 * @param width width
	 * @param height height
	 * @param id int
	 */
	public Texture(int width, int height, int id){
		this.width = width;
		this.height = height;
		this.id = id;
	}
	
	/**
	 * Loader de background
	 * @param name String
	 * @param extension String
	 * @return Texture
	 */
	public static Texture loadTexture(String name, String extension){
		BufferedImage image = null;
		try{
			//Chargement de la background
			image = ImageIO.read(Texture.class.getResource("/textures/" + name + "." + extension));
			logger.debug("Load texture: /textures/" + name + "." + extension);
		}catch(IOException e){
			logger.error("Error for loading texture: /textures/" + name + "." + extension, e);
			System.exit(1);
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
		
		//Génération de la background OpenGL
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		//Retourne une nouvelle background
		return new Texture(width, height, id);
	}
	
	/**
	 * Permet d'appliquer la background
	 */
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	/**
	 * Enlève la background
	 */
	public void unbind(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Getter largeur background
	 * @return int
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * Getter hauteur background
	 * @return int
	 */
	public int getHeight(){
		return height;
	}
}
