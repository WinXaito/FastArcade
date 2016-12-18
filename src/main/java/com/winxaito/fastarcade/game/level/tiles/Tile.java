package com.winxaito.fastarcade.game.level.tiles;

import org.newdawn.slick.Color;

import com.winxaito.fastarcade.render.Renderer;

public class Tile{
	private static int size = 64;
	private int x;
	private int y;

	private Tiles tile;
	private float textureSlot = 16.0f;
	
	/**
	 * Type de tiles (Solide ou transparent)
	 */
	public enum TileType{
		SOLID,
		TRANSPARENT
	}
	
	/**
	 * Type de tiles (Avec offset de la texture et type)
	 */
	public enum Tiles{
		BACKGROUND(0, TileType.TRANSPARENT, 16, 1),
		DIRT(1, TileType.SOLID, 0, 0),
		GRASS(2, TileType.SOLID, 1, 0),
		STONE(3, TileType.SOLID, 3, 0),
		CLOUD(4, TileType.TRANSPARENT, 3, 0),
		DAMIER(5, TileType.TRANSPARENT, 14, 0);
		
		protected int id;
		protected TileType tileType;
		protected int xOffset;
		protected int yOffset;
		
		Tiles(int id, TileType tileType, int xOffset, int yOffset){
			this.id = id;
			this.tileType = tileType;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}
	}
	
	/**
	 * Constructeur
	 * @param x int
	 * @param y int
	 * @param tile Tiles
	 */
	public Tile(int x, int y, Tiles tile){
		this.x = x * size;
		this.y = y * size;
		this.tile = tile;
	}
	
	/**
	 * Fonction de rendu (Appelé par la fonction render() de Level)
	 */
	public void render(){
		if(tile.id != 0)
			Renderer.renderQuad(x, y, size, size, Color.white, tile.xOffset, tile.yOffset, textureSlot);
	}
	
	/**
	 * Getter de l'id de la Tile (Correspondant à un certain type et une certaine texture)
	 * @return int
	 */
	public int getId(){
		return tile.id;
	}
	
	/**
	 * Getter du type de Type (Solid ou transparent)
	 * @return TileType
	 */
	public TileType getTileType(){
		return tile.tileType;
	}

	/**
	 * Getter taille Tile
	 * @return size
	 */
	public static int getSize(){
		return size;
	}

	/**
	 * Setter taille Tile
	 * @param size int
	 */
	public static void setSize(int size){
		Tile.size = size;
	}
}
