package com.winxaito.fastarcade.utils;

import com.winxaito.fastarcade.game.state.GameState;
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
        if((audioType == AudioType.MENU && menuAudios.size() == 0) || (audioType == AudioType.GAME && levelAudios.size() == 0))
            return;

        if(currentAudio != null && !GameState.isState(audioType.getGameState()))
            currentAudio.stop();

        if(currentAudio == null || !currentAudio.isPlaying()){
            logger.debug("OUI");
            if(GameState.isState(GameState.GameStateList.MENU)){
                logger.debug("MUSIC MENU - " + menuIndex + " - " + menuAudios.size());

                if(menuIndex >= menuAudios.size()){
                    currentAudio = menuAudios.get(0);
                    menuIndex = 1;
                }else{
                    currentAudio = menuAudios.get(menuIndex);
                    menuIndex++;
                }

                audioType = AudioType.MENU;
            }else if(GameState.isState(GameState.GameStateList.GAME)){
                logger.debug("MUSIC GAME");

                if(menuIndex >= levelAudios.size()){
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

    public void nextMusic(){
        if(currentAudio != null && currentAudio.isPlaying())
            currentAudio.stop();
    }

    public void exit(){
        if(currentAudio != null && currentAudio.isPlaying())
            currentAudio.stop();
    }
}
