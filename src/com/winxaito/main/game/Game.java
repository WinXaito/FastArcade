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

public class Game{
	private int width;
	private int height;
	private int tps;
	private int fps;
	private int sleep;
	private boolean running;
	
	private GameState gameState = GameState.LEVEL;
	private Level level;
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
		
		//initialisation de la fen�tre
		initializeDisplay();
		
		//Initialisation de la vue
		initializeView();
		
		//Cr�ation du level
		level = new Level();
	}
	
	/**
	 * Lancement du jeu
	 */
	public void start(){
		running = true;
		mainLoop();
	}
	
	/**
	 * Fonction d'arr�t de jeu
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
		
		while(running){
			if(Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
				exit();
			
			Display.update();	
			
			if(!Main.isVSync()){
				if(System.nanoTime() - tpsTime > 1_000_000_000 / Main.getTpsLimit()){
					update();
					tps++;
					
					tpsTime = System.nanoTime();
				}else if(System.nanoTime() - fpsTime > 1_000_000_000 / Main.getFpsLimit()){
					
					render();
					fps++;
					
					fpsTime = System.nanoTime();
				}else{
					sleep++;
					if(Main.isSleep()){
						try{
							Thread.sleep(1);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
				}
			}else{
				update();
				render();
				fps++;
				tps++;
			}
			
			if(System.currentTimeMillis() - time > 1000){
				Display.setTitle(Main.getAppTitle() + " - " + Main.getAppVersion() + "  Fps: " + fps + " Tps: " + tps + " Sleep: " + sleep);
				
				if(tps < 50){
					System.err.println("TPS tr�s faible");
					
					if(Main.isVSync()){
						System.err.println("-- D�sactivation VSync");
						Main.setVSync(false);
					}
				}
				
				if(fps < 40){
					System.err.println("FPS tr�s faible");
				}
				
				time = System.currentTimeMillis();
				fps = 0;
				tps = 0;
				sleep = 0;
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
				break;
			case OPTIONS_MENU:
				//Render options-menu
				break;
		}
	}
	
	/**
	 * D�placement du level
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
	 * Initialisation des param�tres OpenGL (2D)
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
	 * Initialisation de l'�cran (Display)
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
}
