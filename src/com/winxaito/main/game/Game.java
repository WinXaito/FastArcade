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
import com.winxaito.main.game.menu.LevelMenu;
import com.winxaito.main.game.menu.LoadingMenu;
import com.winxaito.main.game.menu.MainMenu;

public class Game{
	private int width;
	private int height;
	private int tps;
	private int fps;
	private static int fpsView;
	private static int tpsView;
	private int sleep;
	private boolean running;
	
	private GameState gameState = GameState.GameStarting;
	private Level level;
	private Hud hud;
	private MainMenu menu;
	private LevelMenu levelMenu;
	private LoadingMenu loaderMenu;
	private float xScroll;
	private float yScroll;
	
	/**
	 * Etats de jeu (Menu, level, etc.)
	 */
	public enum GameState{
		MainMenu,
		LevelMenu(MainMenu),
		OptionsMenu(MainMenu),
		Level(LevelMenu),
		GameStarting;
		
		protected GameState escapteGoTo;
		
		GameState(){
			this.escapteGoTo = null;
		}
		
		GameState(GameState escapteGoTo){
			this.escapteGoTo = escapteGoTo;
		}
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
		
		//Initialisation du Menu Loading (Chargement du démarrage)
		initializeLoadingMenu();
		
		//Création du menu
		menu = new MainMenu(this);
		
		//Création LevelMenu
		levelMenu = new LevelMenu(this);
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
			if(Display.isCloseRequested())
				exit();
			
			Display.update();
			
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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_0))
			System.out.println("Key 0");
		
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
		        	if(gameState.escapteGoTo != null){
		        		if(gameState == GameState.Level)
		        			level.unloadLevel();
		        		
		        		setGameState(gameState.escapteGoTo);
		        	}else{
		        		exit();
		        	}
		        }
		    }
		}
		
		switch(gameState){
			case MainMenu:					
				menu.update();
				break;
			case LevelMenu:
				levelMenu.update();
				break;
			case Level:
				level.update();
				hud.update();
				float xa = -level.getPlayer().getX() + width / 2 - level.getPlayer().getSize() / 2;
				float ya = -level.getPlayer().getY() + height / 2 - level.getPlayer().getSize() / 2;
				translateView(xa, ya);
				break;
			case GameStarting:
				
				break;
			case OptionsMenu:
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
			case MainMenu:
				menu.render();
				break;
			case LevelMenu:
				levelMenu.render();
				break;
			case Level:					
				GL11.glTranslatef(xScroll, yScroll, 0);
				level.render();
				GL11.glTranslatef(-xScroll, -yScroll, 0);
				hud.render();
				break;
			case GameStarting:
				loaderMenu.render();
				break;
			case OptionsMenu:
				//Render options-menu
				break;
		}
	}
	
	public void loadLevel(String levelName){
		level = new Level(this, levelName);
		hud = new Hud(level);
		
		setGameState(GameState.Level);
		
		try{
			Thread.sleep(3);
		}catch(InterruptedException e){
			e.printStackTrace();
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
	
	public void initializeLoadingMenu(){
		loaderMenu = new LoadingMenu(this, "Lancement de FastArcade");
		loaderMenu.render();
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
			}else if(Main.isBorderless()){
				System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
				displayMode = new DisplayMode((int)screenWidth, (int)screenHeight);
				Display.setDisplayMode(displayMode);
				Display.setResizable(false);
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
