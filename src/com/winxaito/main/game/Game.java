package com.winxaito.main.game;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.winxaito.main.Main;
import com.winxaito.main.game.level.Level;
import com.winxaito.main.game.menu.Hud;

public class Game{
	private int width;
	private int height;
	private int tps;
	private int fps;
	private static int fpsView;
	private static int tpsView;
	private int sleep;
	private boolean running;
	
	private GameState gameState = GameState.LEVEL;
	private Level level;
	private Hud hud;
	private float xScroll;
	private float yScroll;
	
	/**
	 * Etats de jeu (Menu, level, etc.)
	 */
	public enum GameState{
		MAIN_MENU,
		LEVEL,
		OPTIONS_MENU
	}
	
	/**
	 * Constructeur de la classe Game
	 * @param width
	 * @param height
	 */
	public Game(int width, int height){
		this.width = width;
		this.height = height;
		
		//initialisation de la fenêtre
		initializeDisplay();
		
		//Initialisation de la vue
		initializeView();
		
		//Création du level
		level = new Level();
		hud = new Hud(level);
	}
	
	/**
	 * Lancement du jeu
	 */
	public void start(){
		running = true;
		mainLoop();
	}
	
	/**
	 * Fonction d'arrêt de jeu
	 */
	public void stop(){
		running = false;
	}
	
	/**
	 * Boucle de jeu
	 */
	public void mainLoop(){
		long time = System.currentTimeMillis();
		long tpsTime = System.nanoTime();
		long fpsTime = tpsTime;
		
		long lastTickTime = System.nanoTime();
		long lastRenderTime = System.nanoTime();
		
		double tickTime = 1_000_000_000.0d / Main.getTpsLimit();
		double renderTime = 1_000_000_000.0d / Main.getFpsLimit();
		
		int ticks = 0;
		int frames = 0;
		
		long timer = System.currentTimeMillis();
		
		while(running){
			if(Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
				exit();
			
			Display.update();	
			//update();
			//render();
			
			if(!Main.isVSync()){
				//Si on est en dessous des 60 tps
				if(System.nanoTime() - lastTickTime > tickTime){
					update();
					lastTickTime += tickTime;
					tps++;
				}
				//Sinon si on est en dessous des 500 fps
				else if(System.nanoTime() - lastRenderTime > renderTime){
					render();
					lastRenderTime += renderTime;
					fps++;
				}
				//Sinon on endore le Thread
				else{
					try {
						Thread.sleep((int)(1000.0 / Main.getFpsLimit()));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}else{
				update();
				render();
				fps++;
				tps++;
			}
			
			//On affiche chaque seconde les ticks et les fps actuel
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				Display.setTitle(Main.getAppTitle() + " - " + Main.getAppVersion() + "  Fps: " + fps + "/" + Main.getFpsLimit() + 
						" Tps: " + tps + "/" + Main.getTpsLimit() + " VSync: " + Main.isVSync());
				
				if(tps < 50)
					System.err.println("TPS très faible");
				if(fps < 40)
					System.err.println("FPS très faible");
				
				fpsView = fps;
				tpsView = tps;
				tps = 0;
				fps = 0;
			}
		}
		
		exit();
	}
	
	/**
	 * Quitte le jeu
	 */
	public void exit(){
		Display.destroy();
		System.exit(0);
	}
	
	/**
	 * Update du jeu (Correspond au TPS)
	 */
	public void update(){
		switch(gameState){
			case MAIN_MENU:
				//Update menu
				break;
			case LEVEL:
				level.update();
				hud.update();
				float xa = -level.getPlayer().getX() + width / 2 - level.getPlayer().getSize() / 2;
				float ya = -level.getPlayer().getY() + height / 2 - level.getPlayer().getSize() / 2;
				translateView(xa, ya);
				break;
			case OPTIONS_MENU:
				//Update Options-menu
				break;
		}
	}
	
	/**
	 * Rendu du jeu (Correspond au FPS)
	 */
	public void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);		
		
		this.width = Display.getWidth();
		this.height = Display.getHeight();
		initializeView();
		
		switch(gameState){
			case MAIN_MENU:
				//Render menu
				break;
			case LEVEL:
				
				GL11.glTranslatef(xScroll, yScroll, 0);
				level.render();
				GL11.glTranslatef(-xScroll, -yScroll, 0);
				hud.render();
				break;
			case OPTIONS_MENU:
				//Render options-menu
				break;
		}
	}
	
	/**
	 * Déplacement du level
	 * @param xa
	 * @param ya
	 */
	public void translateView(float xa, float ya){
		xScroll = xa;
		yScroll = ya;
		
		if(xScroll < level.getLimits(2))
			xScroll = level.getLimits(2);
		if(yScroll < level.getLimits(3))
			yScroll = level.getLimits(3);
		
		if(xScroll > level.getLimits(0))
			xScroll = level.getLimits(0);
		if(yScroll > level.getLimits(1))
			yScroll = level.getLimits(1);
	}
	
	/**
	 * Initialisation des paramètres OpenGL (2D)
	 */
	public void initializeView(){
		GL11.glViewport(0, 0, width, height);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluOrtho2D(0, width, height, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH); 
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/**
	 * Initialisation de l'écran (Display)
	 */
	public void initializeDisplay(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		
		try{
			DisplayMode displayMode = null;
			
			if(Main.isFullScreen()){
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				
				for (int i = 0; i < modes.length; i++){
					if(modes[i].isFullscreenCapable() && modes[i].getWidth() == screenWidth && modes[i].getHeight() == screenHeight)
						displayMode = modes[i];
				}
				
				if(displayMode != null){
					Display.setDisplayModeAndFullscreen(displayMode);
				}
			}else{
				displayMode = new DisplayMode(Main.getStartWidth(), Main.getStartHeight());
				Display.setDisplayMode(displayMode);
				Display.setResizable(true);
			}
			
			Display.setVSyncEnabled(Main.isVSync());
			Display.setTitle(Main.getAppTitle() + " - " + Main.getAppVersion());
			Display.create();
		}catch(LWJGLException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Getter gameState
	 * @return GameState
	 */
	public GameState getGameState(){
		return gameState;
	}
	
	/**
	 * Setter gameState
	 * @param gameState
	 */
	public void setGameState(GameState gameState){
		this.gameState = gameState;
	}
	
	public static int getFps(){
		return Game.fpsView;
	}
	public static int getTps(){
		return Game.tpsView;
	}
}
