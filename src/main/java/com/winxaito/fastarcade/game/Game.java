package com.winxaito.fastarcade.game;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URISyntaxException;

import com.winxaito.fastarcade.game.menu.*;
import com.winxaito.fastarcade.game.menu.hud.MainHud;
import com.winxaito.fastarcade.game.state.GameState;
import com.winxaito.fastarcade.utils.MusicPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.winxaito.fastarcade.Main;
import com.winxaito.fastarcade.game.level.Level;

public class Game{
	private Logger logger = LogManager.getLogger(Game.class);

	private int scaleWidth = 1920;
	private int scaleHeight = 1080;
	private int width;
	private int height;
	private static int fpsView;
	private static int tpsView;
	private boolean running = false;

	private MusicPlayer musicPlayer;
	private Level level;
	private MainHud mainHud;
	private LoadingMenu loadingMenu;
	private Menus menus;
	private float xScroll;
	private float yScroll;

	private long tickTimer = 0;
	private int gameTime = 0;
	private long time = System.currentTimeMillis();
	private long lastTickTime = System.nanoTime();
	private long lastRenderTime = System.nanoTime();
	private int frames = 0;
	private int ticks = 0;
	private static int currentTicks = 0;
	
	/**
	 * Constructeur de la classe Game
	 * @param width int
	 * @param height int
	 */
	public Game(int width, int height){
		GameState.setState(GameState.GameStateList.STARTING);
		this.width = width;
		this.height = height;
		
		//Initializing the window
		initializeDisplay();
		
		//Initialize the view
		initializeView();

		//Initialisation du Menu Loading (Chargement du démarrage)
		loadingMenu = new LoadingMenu(this, "Lancement de FastArcade");
		initializeLoadingMenu();

		//Création du menu
		//menu = new MainMenu(this);
		menus = new Menus(this, loadingMenu);

		//Initialisation du MusicPlayer
		musicPlayer = new MusicPlayer();

		//Chargement (Menu, music, etc.)
		load();
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

			timer();
			Display.update();
		}
		
		exit();
	}


	/**
	 * Quitte le jeu
	 */
	public void exit(){
		if(level != null && level.isLoaded())
			level.unloadLevel();

		musicPlayer.exit();
		Display.destroy();
		System.exit(0);
	}

	/**
	 * Menu loading (With starting menu)
     */
	private void load(){
		menus.load();

		try{
			musicPlayer.load();
		}catch(IOException | URISyntaxException e){
			logger.error("Error for load music", e);
		}

		GameState.setState(GameState.GameStateList.MENU);
	}
	
	/**
	 * Update du jeu (Correspond au TPS)
	 */
	public void update(){
		//input();
		musicPlayer.update();

		switch(GameState.getState()){
			case MENU:
				menus.update();
				break;
			case GAME:
				level.update();
				mainHud.update();
				float xa = -level.getPlayer().getX() + width / 2 - level.getPlayer().getSize() / 2;
				float ya = -level.getPlayer().getY() + height / 2 - level.getPlayer().getSize() / 2;
				translateView(xa, ya);
				break;
		}
	}

	/**
	 * Manage game inputs
     */
	private void input(){
		//TODO: Input
	}
	
	/**
	 * Rendu du jeu (Correspond au FPS)
	 */
	public void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);		
		
		this.width = Display.getWidth();
		this.height = Display.getHeight();
		initializeView();

		switch(GameState.getState()){
			case STARTING:
				loadingMenu.render();
				break;
			case MENU:
				menus.render();
				break;
			case GAME:
				GL11.glTranslatef(xScroll, yScroll, 0);
				level.render();
				GL11.glTranslatef(-xScroll, -yScroll, 0);
				level.setActionBackgroundPositions(-xScroll, -yScroll);
				mainHud.render();
				level.renderHud();
				break;
		}
	}
	
	public void loadLevel(String levelName){
		level = new Level(this, levelName);
		mainHud = new MainHud(level);
		
		GameState.setState(GameState.GameStateList.GAME);
	}

	/**
	 * Déplacement de la vue
	 * @param xa float
	 * @param ya float
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
		render();
		Display.update();
	}
	
	/**
	 * Initialisation des paramètres OpenGL (2D)
	 */
	private void initializeView(){
		GL11.glViewport(0, 0, width, height);
		//GL11.glViewport(0, 0, scaleWidth, scaleHeight);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		//GLU.gluOrtho2D(0, width, height, 0);
		//GLU.gluOrtho2D(0, scaleWidth, scaleHeight, 0);
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
				currentTicks++;

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
			currentTicks++;
		}

		if(System.currentTimeMillis() - time > 1000){
			time += 1000;
			fpsView = frames;
			tpsView = ticks;

			ticks = 0;
			frames = 0;
		}
	}

	/**
	 * Return static int (Used for view in HUD), FPS setted in timer method every second
	 * @return int
     */
	public static int getFps(){
		return Game.fpsView;
	}

	/**
	 * Return static int (Used for view in HUD), TPS setted in timer method every second
	 * @return int
	 */
	public static int getTps(){
		return Game.tpsView;
	}

	/**
	 * Return the number of ticks since the game was lauched
	 * @return int
     */
	public static int getCurrentTicks(){
		return currentTicks;
	}
}
