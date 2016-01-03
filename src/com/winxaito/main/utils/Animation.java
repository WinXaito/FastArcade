package com.winxaito.main.utils;

public class Animation{
	private int frame = 0;
	private int length;
	private int speed;
	private boolean loop = false;
	
	private boolean playing = false;
	private int time = 0;
	
	/**
	 * Constructeur
	 * @param length
	 * @param speed
	 * @param loop
	 */
	public Animation(int length, int speed, boolean loop){
		this.length = length;
		this.speed = speed;
		this.loop = loop;
	}
	
	/**
	 * Update l'animation
	 */
	public void update(){
		if(playing){
			time++;
			
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
	 * Retourne true si l'animation est lancé
	 * @return boolean
	 */
	public boolean isPlaying(){
		return playing;
	}
}
