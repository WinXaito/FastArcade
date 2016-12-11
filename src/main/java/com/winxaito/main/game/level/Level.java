package com.winxaito.main.game.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.winxaito.main.entities.Boost;
import com.winxaito.main.entities.Entity;
import com.winxaito.main.entities.Player;
import com.winxaito.main.game.Game;
import com.winxaito.main.game.level.tiles.Tile;
import com.winxaito.main.game.level.tiles.Tile.Tiles;
import com.winxaito.main.game.menu.LoadingMenu;
import com.winxaito.main.render.Texture;

public class Level{
	private int width = 20;
	private int height = 20;
	private int xSpawn = 2;
	private int ySpawn = 2;
	private int boost = 100;
	private int[] limits = new int[4];
	private float gravity = 4.4f;
	
	private LoadingMenu loaderMenu;
	
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	private Tile[][] solidTiles;
	private Tile[][] transparentTiles;
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Player player;
	
	private Audio music;	
	
	/**
	 * Constructeur de la classe Level
	 */
	public Level(Game game, String levelName){
		//Création du LoaderMenu
		loaderMenu = new LoadingMenu(game, "Chargement du level: " + levelName);
		
		loadLevel(levelName);
		try{
			music = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("/res/music/music.ogg"));
			music.playAsMusic(0.8f, 0.8f, true);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		player = new Player(xSpawn * Tile.getSize(), ySpawn * Tile.getSize(), 1 * Tile.getSize(), this);
	}
	
	/**
	 * Chargement d'un level
	 */
	public void loadLevel(String name){		
		int[] pixels;
		BufferedImage image = null;
		try{
			//Lecture de l'image
			image = ImageIO.read(Level.class.getResource("/levels/level_" + name + "/level_" + name + ".png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		width = image.getWidth();
		height = image.getHeight();
		
		//Limit du level gauche et haut
		limits[0] = 0;
		limits[1] = 0;
		
		//Création d'un tableau contenu les pixels du level chargé
		pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		//Création de tableaux contenants les tiles
		solidTiles = new Tile[width][height];
		transparentTiles = new Tile[width][height];
		
		//Boucle d'attribution des types de tiles selon la couleurs des pixels
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(pixels[x + y * width] == 0xFFffffff){
					//SolidTile
					solidTiles[x][y] = new Tile(x, y, Tiles.STONE);
				}else if(pixels[x + y * width] == 0xFF000000){
					//TransparentTile
					transparentTiles[x][y] = new Tile(x, y, Tiles.BACKGROUND);
				}else if(pixels[x + y * width] == 0xFFffff00){
					//SpawnTile
					transparentTiles[x][y] = new Tile(x, y, Tiles.BACKGROUND);
					xSpawn = x;
					ySpawn = y;
				}
			}
		}
		
		//Ajout des tiles dans la liste principales
		for(Tile[] tiles : solidTiles){
			for(Tile tile : tiles){
				this.tiles.add(tile);
			}
		}
		for(Tile[] tiles : transparentTiles){
			for(Tile tile : tiles){
				this.tiles.add(tile);
			}
		}
		
		try{
			//Lecture de l'image
			image = ImageIO.read(Level.class.getResource("/levels/level_" + name + "/level_" + name + "_entities.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		width = image.getWidth();
		height = image.getHeight();
		
		//Création d'un tableau contenu les pixels du level chargé
		pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
				
		//loading des entités
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(pixels[x + y * width] == 0xFFFFAA00){
					System.out.println("YEEEEEEEEP");
					entities.add(new Boost(x * Tile.getSize() + (int)(Tile.getSize() / 6), 
							y * Tile.getSize() + (int)(Tile.getSize() / 6), (int)(Tile.getSize() / 1.5), this));
				}
			}
		}
	}
	
	public void unloadLevel(){
		music.stop();
	}
	
	/**
	 * Update du level (Appeler par la fonction update de la classe Game)
	 */
	public void update(){		
		//Limite droite et bas du level
		limits[2] = -width * Tile.getSize() + Display.getWidth();
		limits[3] = -height * Tile.getSize() + Display.getHeight();
		
		ArrayList<Entity> removedEntity = new ArrayList<Entity>();
		for(Entity entity : entities){
			if(entity.isRemoved())
				removedEntity.add(entity);
			else
				entity.update();
		}
		for(Entity entity : removedEntity){
			entities.remove(entity);
		}
		
		player.update();
	}
	
	/**
	 * Rendu du level (Appeler par la fonction render de la classe Game)
	 */
	public void render(){
		//Rendu des tiles
		Texture.texTiles.bind();
		GL11.glBegin(GL11.GL_QUADS);
			for(Tile tile : tiles){
				if(tile != null)
					tile.render();
			}
		GL11.glEnd();
		
		for(Entity entity : entities){
			entity.render();
		}
		
		//Rendu du joueur
		player.render();		
	}
	
	/**
	 * Getter player
	 * @return player
	 */
	public Player getPlayer(){
		return player;
	}
	
	/**
	 * Indique si la taille à la position passé en paramètre est solide (Si pas le cas, retourne null)
	 * @param x
	 * @param y
	 * @return Tile
	 */
	public Tile getSolidTile(int x, int y){
		if (x < 0 || y < 0 || x >= width || y >= height) 
			return null;
		
		return solidTiles[x][y];
	}

	/**
	 * @return gravity
	 */
	public float getGravity(){
		return gravity;
	}

	/**
	 * @param gravity
	 */
	public void setGravity(float gravity){
		this.gravity = gravity;
	}
	
	/**
	 * Retourne les limites du level (Bordure) pour le translate
	 * @return limits
	 */
	public int getLimits(int index){
		return limits[index];
	}

	/**
	 * Retourne le boost que possède le joueur
	 * @return boost
	 */
	public int getBoost(){
		return boost;
	}

	/**
	 * @param boost
	 */
	public void setBoost(int boost){
		this.boost = boost;
		
		if(this.boost < 0)
			this.boost = 0;
	}
}
