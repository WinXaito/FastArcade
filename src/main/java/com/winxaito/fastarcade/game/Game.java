package com.winxaito.fastarcade.game;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.winxaito.fastarcade.Main;
import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.game.menu.Hud;
import com.winxaito.fastarcade.game.menu.LevelMenu;
import com.winxaito.fastarcade.game.menu.LoadingMenu;
import com.winxaito.fastarcade.game.menu.MainMenu;

public class Game{
	private int width;
	private int height;
	private static int fpsView;
	private static int tpsView;
	private boolean running;
	
	private GameState gameState = GameState.GameStarting;
	private Level level;
	private Hud hud;
	private MainMenu menu;
	private LevelMenu levelMenu;
	private LoadingMenu loaderMenu;
	private float xScroll;
	private float yScroll;

	private long tickTimer = 0;
	private int gameTime = 0;
	private long time = System.currentTimeMillis();
	private long lastTickTime = System.nanoTime();
	private long lastRenderTime = System.nanoTime();
	private int frames = 0;
	private int ticks = 0;
	
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
	private void mainLoop(){
		while(running){
			if(Display.isCloseRequested())
				exit();
			
			Display.update();
			timer();
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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && Keyboard.next())
			setGameState(GameState.MainMenu);
		
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
	private void translateView(float xa, float ya){
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
	
	private void initializeLoadingMenu(){
		loaderMenu = new LoadingMenu(this, "Lancement de FastArcade");
		loaderMenu.render();
	}
	
	/**
	 * Initialisation des paramètres OpenGL (2D)
	 */
	private void initializeView(){
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
	private void initializeDisplay(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		
		try{
			DisplayMode displayMode = null;
			
			if(Main.isFullScreen()){
				DisplayMode[] modes = Display.getAvailableDisplayModes();

				for(DisplayMode mode : modes){
					if(mode.isFullscreenCapable() && mode.getWidth() == screenWidth && mode.getHeight() == screenHeight)
						displayMode = mode;
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
	 * Timer of application
     */
	private void timer(){
		if(!Main.isVSync()){
			double tickTime = 1_000_000_000.0d / Main.getTpsLimit();
			double renderTime = 1_000_000_000.0d / Main.getFpsLimit();

			if(System.nanoTime() - lastTickTime > tickTime){
				ticks++;
				tickTimer++;
				gameTime++;

				if(gameTime > Main.getTpsLimit() * 60 * 12)
					gameTime = 0;

				update();
				lastTickTime += tickTime;
			}else if(System.nanoTime() - lastRenderTime > renderTime){
				frames++;
				render();

				lastRenderTime += renderTime;
			}else{
				try{
					Thread.sleep((int)(1000.0 / Main.getFpsLimit()));
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}else{
			update();
			render();
			frames++;
			ticks++;
		}

		if(System.currentTimeMillis() - time > 1000){
			time += 1000;
			System.out.println("INFO: Tps: " + ticks + " Fps: " + frames);

			fpsView = frames;
			tpsView = ticks;

			ticks = 0;
			frames = 0;
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
