package com.winxaito.fastarcade.game.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.winxaito.fastarcade.entities.action.Action;
import com.winxaito.fastarcade.entities.action.Blindness;
import com.winxaito.fastarcade.entities.action.Blink;
import com.winxaito.fastarcade.entities.items.Point;
import com.winxaito.fastarcade.game.menu.hud.OptionsHud;
import com.winxaito.fastarcade.utils.keyboard.FaKeyboard;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.winxaito.fastarcade.entities.items.Boost;
import com.winxaito.fastarcade.entities.Entity;
import com.winxaito.fastarcade.entities.Player;
import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.level.tiles.Tile;
import com.winxaito.fastarcade.game.level.tiles.Tile.Tiles;
import com.winxaito.fastarcade.game.menu.LoadingMenu;
import com.winxaito.fastarcade.render.Texture;

public class Level{
	private int width = 20;
	private int height = 20;
	private int xSpawn = 2;
	private int ySpawn = 2;
	private int boost = 0;
	private int point = 0;
	private int[] limits = new int[4];
	private float gravity = 4.4f;
	private boolean loaded = false;
	
	private LoadingMenu loaderMenu;
	private OptionsHud optionsHud;

	private Background background;
	private ArrayList<Tile> tiles = new ArrayList<>();
	private Tile[][] solidTiles;
	private Tile[][] transparentTiles;
	
	private ArrayList<Entity> entities = new ArrayList<>();
	private HashMap<Action.ActionType, Action> actions = new HashMap<>();
	private Player player;

	public void setActionBackgroundPositions(float x, float y){
		for(Map.Entry<Action.ActionType, Action> action : actions.entrySet())
			action.getValue().setBackgroundPositions(x, y);
	}

	/**
	 * Constructeur de la classe Level
	 */
	public Level(Game game, String levelName){
		//Création du LoaderMenu
		loaderMenu = new LoadingMenu(game, "Chargement du level: " + levelName);

		//Initialisation du menu options (HUD)
		optionsHud = new OptionsHud(game, this);

		//Initialisation du background
		//TODO: background = new Background(Background.BackgroundType.PLAYER_MOVE);
		background = new Background(5, 1, 0.3f, Background.BackgroundType.PLAYER_MOVE);

		//Load level
		loadLevel(levelName);

		//Init player
		player = new Player(xSpawn * Tile.getSize(), ySpawn * Tile.getSize(), Tile.getSize(), this);

		//Init actions
		initActions();
	}
	
	/**
	 * Chargement d'un level
	 */
	public void loadLevel(String name){
		loaderMenu.render();

		int[] pixels;
		BufferedImage image = null;
		try{
			//Lecture de l'image
			image = ImageIO.read(Level.class.getResource("/levels/level_" + name + "/level_" + name + ".png"));
		}catch(IOException e){
			e.printStackTrace();
			return;
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
				}else if(pixels[x + y * width] == 0xFFFF0000){
					//TransparentTile
					transparentTiles[x][y] = new Tile(x, y, Tiles.DAMIER);
					System.out.println("___#####DAMIER____ + " + - x + " - " + y);
				}

				System.out.println("_OTHER_");
			}
		}
		
		//Ajout des tiles dans la liste principales
		for(Tile[] tiles : solidTiles){
			Collections.addAll(this.tiles, tiles);
		}
		for(Tile[] tiles : transparentTiles){
			Collections.addAll(this.tiles, tiles);
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
					entities.add(new Boost(x * Tile.getSize() + Tile.getSize() / 6,
							y * Tile.getSize() + Tile.getSize() / 6, (int)(Tile.getSize() / 1.5), this));
				}else if(pixels[x + y * width] == 0xFFFF0000){
					entities.add(new Point(x * Tile.getSize() + Tile.getSize() / 6,
							y * Tile.getSize() + Tile.getSize() / 6, (int)(Tile.getSize() / 1.5), this));
				}
			}
		}

		loaded = true;
	}
	
	public void unloadLevel(){
		loaded = false;
	}
	
	/**
	 * Update du level (Appeler par la fonction update de la classe Game)
	 */
	public void update(){		
		//Limite droite et bas du level
		limits[2] = -width * Tile.getSize() + Display.getWidth();
		limits[3] = -height * Tile.getSize() + Display.getHeight();

		//Render entities
		if(!entities.isEmpty()){
			ArrayList<Entity> removedEntity = new ArrayList<>();
			for(Entity entity : entities){
				if(entity.isRemoved())
					removedEntity.add(entity);
				else
					entity.update();
			}
			for(Entity entity : removedEntity){
				entities.remove(entity);

				//TODO:TEST
				if(entities.isEmpty())
					actions.get(Action.ActionType.BLINDNESS).activeAction(60*5);
			}
		}
		
		player.update();
		background.update(new Vector2f(player.getX(), player.getY()));

		//Update des actions
		for(Map.Entry<Action.ActionType, Action> action : actions.entrySet())
			action.getValue().update();

		/*
			EFFECT
		 */
		if(FaKeyboard.isKeyDown(FaKeyboard.Key.KEY_R))
			actions.get(Action.ActionType.BLINK).activeAction(60 * 5);

		//Update du HUD options
		if(FaKeyboard.isKeyDown(FaKeyboard.Key.KEY_ESPACE))
			optionsHud.toggleVisible();

		optionsHud.update();
	}

	/**
	 * Rendu du level (Appeler par la fonction render de la classe Game)
	 */
	public void render(){
		//Rendu du background
		background.render();

		//Rendu des tiles
		Texture.texTiles.bind();
		tiles.stream().filter(tile -> tile != null).forEach(Tile::render);
		Texture.texTiles.unbind();

		entities.forEach(Entity::render);
		
		//Rendu du joueur
		player.render();

		//Rendu des actions
		for(Map.Entry<Action.ActionType, Action> action : actions.entrySet())
			action.getValue().render();
	}

	public void renderHud(){
		optionsHud.render();
	}

	public void initActions(){
		actions.put(Action.ActionType.BLINDNESS, new Blindness(this));
		actions.put(Action.ActionType.BLINK, new Blink(this));
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

	public int getTopLimit(){
		return limits[1];
	}

	public int getLeftLimit(){
		return limits[0];
	}

	public int getBottomLimit(){
		return limits[3];
	}

	public int getRightLimit(){
		return limits[2];
	}

	public boolean isLoaded(){
		return loaded;
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

	public int getPoint(){
		return point;
	}

	public void setPoint(int point){
		this.point = point;
	}

	public HashMap<Action.ActionType, Action> getActions(){
		return actions;
	}

	public ArrayList<Action> getActionsList(){
		return actions.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<Action> getActiveActions(){
		return actions.entrySet().stream()
				.filter(entry -> entry.getValue().isActive())
				.map(Map.Entry::getValue)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public OptionsHud getOptionsHud(){
		return optionsHud;
	}
}
