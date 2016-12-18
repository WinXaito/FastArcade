package com.winxaito.fastarcade;

import java.io.File;

import com.winxaito.fastarcade.game.Game;

public class Main{
	private static String appTitle = "FastArcade";
	private static String appVersion = "Alpha-0.0.1";
	private static int appId = 0;
	
	private static int startWidth = 1280;
	private static int startHeight = 720;
	private static int tpsLimit = 60;
	private static int fpsLimit = 60;
	private static boolean fullScreen = false;
	private static boolean borderless = true;
	private static boolean vSync = true;
	private static boolean sleep = false;
	
	private static final String osName = System.getProperty("os.name").toLowerCase();
	
	/**
	 * Lancement de l'application
	 */
	public static void main(String args[]){
		String osNameProperty = osName;
		
		if(args.length == 1)
			osNameProperty = args[0];
		else if(args.length > 1)
			System.err.println("Args incorrect");
		
		if(isOSNameMatch(osNameProperty, "win")){
			System.setProperty("org.lwjgl.librarypath", new File("lib/native_win/").getAbsolutePath());
		}else if(isOSNameMatch(osNameProperty, "mac")){
			System.setProperty("org.lwjgl.librarypath", new File("lib/native_macosx/").getAbsolutePath());
		}else if(isOSNameMatch(osNameProperty, "lin")){
			System.setProperty("org.lwjgl.librarypath", new File("lib/native_linux/").getAbsolutePath());
		}else if(isOSNameMatch(osNameProperty, "solaris")){
			System.setProperty("org.lwjgl.librarypath", new File("lib/native_solaris/").getAbsolutePath());
		}else{
			System.err.println("Your OS (" + osNameProperty + ") is not compatible, is you have : "
					+ "\n\t- Windows\n\t- Linux\n\t- Mac OS X\n\t- Solaris\nTry add parameter: -win | -lin | -mac | -sol");
			System.exit(1);
		}
				
		Game game = new Game(startWidth, startHeight);
		game.start();
	}
	
	/**
	 * Retourne le nom du jeu
	 * @return appTitle
	 */
	public static String getAppTitle(){
		return appTitle;
	}
	
	/**
	 * Retourne la version (Format String) du jeu
	 * @return appVersion
	 */
	public static String getAppVersion(){
		return appVersion;
	}
	
	/**
	 * Retourne l'ID de la version (nombre)
	 * @return appId
	 */
	public static int getAppId(){
		return appId;
	}
	
	/**
	 * Retourne la résolution (Width) de départ
	 * @return startWidth
	 */
	public static int getStartWidth(){
		return startWidth;
	}
	
	/**
	 * Retourne la résolution (Height) de départ
	 * @return startHeight
	 */
	public static int getStartHeight(){
		return startHeight;
	}
	
	/**
	 * Retourne la limite de TPS
	 * @return tpsLimit
	 */
	public static int getTpsLimit(){
		return tpsLimit;
	}
	
	/**
	 * Retourne la limite de FPS
	 * @return fpsLimit
	 */
	public static int getFpsLimit(){
		return fpsLimit;
	}
	
	/**
	 * Retourne si le jeu doit être lancé en plein écran
	 * @return fullScreen
	 */
	public static boolean isFullScreen(){
		return fullScreen;
	}
	
	/**
	 * Retourne si le jeu doit être lancé en Borderless (Sans bord)
	 * @return borderless
	 */
	public static boolean isBorderless(){
		return borderless;
	}

	/**
	 * Retourne si le jeu doit être en mode VSync
	 * @return vSync
	 */
	public static boolean isVSync(){
		return vSync;
	}
	
	/**
	 * Attribue la variable vSync
	 * @param vSync
	 */
	public static void setVSync(boolean vSync){
		Main.vSync = vSync;
	}
	
	/**
	 * Retourne si le jeu doit executé une pause lorsque le rendu n'est pas nécéssaire (Fps limit)
	 * @return sleep
	 */
	public static boolean isSleep(){
		return sleep;
	}
	
	/**
	 * Check l'OS 
	 */
	private static boolean isOSNameMatch(String osName, String osNamePrefix){
		return osName != null && osName.startsWith(osNamePrefix);
	}
}
