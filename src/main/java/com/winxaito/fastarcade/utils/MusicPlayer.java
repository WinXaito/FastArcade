package com.winxaito.fastarcade.utils;

import com.winxaito.fastarcade.game.state.GameState;
import com.winxaito.fastarcade.utils.keyboard.FaKeyboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class MusicPlayer{
    private Logger logger = LogManager.getLogger(MusicPlayer.class);

    private ArrayList<Audio> menuAudios = new ArrayList<>();
    private ArrayList<Audio> levelAudios = new ArrayList<>();

    private AudioType audioType = AudioType.NONE;
    private Audio currentAudio;
    private float currentPosition;
    private boolean pause;
    private int menuIndex = 0;
    private int levelIndex = 0;

    private File[] menuAudiosFile;
    private File[] levelAudiosFile;

    public enum AudioType{
        MENU(GameState.GameStateList.MENU),
        GAME(GameState.GameStateList.GAME),
        NONE(GameState.GameStateList.NONE);

        private GameState.GameStateList gameState;

        AudioType(GameState.GameStateList gameStateList){
            this.gameState = gameStateList;
        }

        public GameState.GameStateList getGameState(){
            return gameState;
        }
    }

    public void load() throws IOException, URISyntaxException{
        logger.info("Load music:");
        File menuAudiosFolder = new File(ResourceLoader.getResource("music/menu/").toURI());
        File levelAudiosFolder = new File(ResourceLoader.getResource("music/level/").toURI());

        menuAudiosFile = menuAudiosFolder.listFiles();
        levelAudiosFile = levelAudiosFolder.listFiles();

        if(menuAudiosFile != null){
            for(File file : menuAudiosFile){
                logger.info("  Load menuAudio: " + file.getName());
                menuAudios.add(AudioLoader.getAudio("OGG", new FileInputStream(file)));
            }
        }

        if(levelAudiosFile != null){
            for(File file : levelAudiosFile){
                logger.info("  Load levelAudio: " + file.getName());
                levelAudios.add(AudioLoader.getAudio("OGG", new FileInputStream(file)));
            }
        }
    }

    public void update(){
        input();

        if(pause)
            return;

        if((audioType == AudioType.MENU && menuAudios.size() == 0) || (audioType == AudioType.GAME && levelAudios.size() == 0))
            return;

        if(currentAudio != null && !GameState.isState(audioType.getGameState()))
            currentAudio.stop();

        if(currentAudio == null || !currentAudio.isPlaying()){
            if(GameState.isState(GameState.GameStateList.MENU)){
                if(menuIndex >= menuAudios.size()){
                    currentAudio = menuAudios.get(0);
                    menuIndex = 1;
                }else{
                    currentAudio = menuAudios.get(menuIndex);
                    menuIndex++;
                }

                audioType = AudioType.MENU;
            }else if(GameState.isState(GameState.GameStateList.GAME)){
                if(levelIndex >= levelAudios.size()){
                    currentAudio = levelAudios.get(0);
                    levelIndex = 1;
                }else{
                    currentAudio = levelAudios.get(levelIndex);
                    levelIndex++;
                }

                audioType = AudioType.GAME;
            }
        }

        if(currentAudio != null){
            if(!currentAudio.isPlaying())
                currentAudio.playAsMusic(1.0f, 1.0f, false);
        }
    }

    public void input(){
        if(FaKeyboard.isKeyDown(FaKeyboard.Key.KEY_M))
            togglePause();

        if(FaKeyboard.isKeyDown(FaKeyboard.Key.KEY_N))
            nextMusic();
    }

    public void togglePause(){
        if(pause)
            resume();
        else
            pause();
    }

    public void pause(){
        if(currentAudio != null && currentAudio.isPlaying()){
            currentPosition = currentAudio.getPosition();
            currentAudio.stop();

            pause = true;
        }
    }

    public void resume(){
        if(currentAudio != null && !currentAudio.isPlaying()){
            currentAudio.playAsMusic(1f, 1f, false);
            currentAudio.setPosition(currentPosition);

            pause = false;
        }
    }

    public void nextMusic(){
        if(currentAudio != null && currentAudio.isPlaying())
            currentAudio.stop();
    }

    public void exit(){
        if(currentAudio != null && currentAudio.isPlaying())
            currentAudio.stop();
    }
}
