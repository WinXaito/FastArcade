package com.winxaito.main.utils;

public class Animation{
	private int frame = 0;
	private int length;
	private int speed;
	private boolean loop = false;
	
	private boolean playing = false;
	private int time = 0;
	
	private boolean lineAndColumn = false;
	private boolean lineByLine = true;
	private int line = 1;
	private int column;
	private int currentColumn;
	private int currentLine;
	
	/**
	 * Constructeur
	 * @param length
	 * @param speed
	 * @param loop
	 */
	public Animation(int length, int speed, boolean loop){
		this.length = length;
		this.column = length;
		this.speed = speed;
		this.loop = loop;
	}
	
	public Animation(int length, int speed, boolean loop, int line, int column, boolean lineByLine){
		this.length = length;
		this.speed = speed;
		this.loop = loop;
		this.line = line;
		this.column = column;
		this.lineByLine = lineByLine;
		
		this.lineAndColumn = true;
	}
	
	/**
	 * Update l'animation
	 */
	public void update(){
		if(playing){
			time++;
			
			if(!lineAndColumn){
				if(time > speed){
					frame++;
					
					if(frame >= length){
						if(loop)
							frame = 0;
						else
							stop();
					}
					
					time = 0;
				}
			}else{
				if(time > speed){
					frame++;
					currentColumn++;
					
					if(frame >= length){
						frame = 0;
						currentLine = 0;
						currentColumn = 0;
					}else{
						if(currentColumn >= column){
							currentColumn = 0;
							currentLine++;
						}
					}
					
					time = 0;
				}
			}
		}
	}


/**
 * Lance l'animation
 */
public void start(){
	playing = true;
}

/**
 * Met en pause l'animation
 */
public void pause(){
	playing = false;
}

/**
 * Stop l'animation (Recommencera ensuite au début)
 */
public void stop(){
	playing = false;
	frame = 0;
}

/**
 * Retourne la frame actuelle
 * @return currentFrame
 */
public int getCurrentFrame(){
	return frame;
}

/**
 *	Retourne la ligne
 *  @return currentLine
 */
public int getCurrentLine(){
	return currentLine;
}

/**
 * Retourne la colonne
 * @return currentColumn
 */
public int getCurrentColumn(){
	return currentColumn;
}

/**
 * Retourne true si l'animation est lancé
 * @return boolean
 */
public boolean isPlaying(){
	return playing;
}
}
