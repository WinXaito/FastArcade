package com.winxaito.fastarcade.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class GameState{
    private static final Logger logger = LogManager.getLogger(GameState.class.getName());
    private static GameStateList gameState = GameStateList.STARTING;

    public enum GameStateList{
        STARTING,
        GAMELOADER,

        MENU_MAIN,
        MENU_LEVEL,
        MENU_OPTIONS,

        GAME,
        GAME_OPTIONS,

        LEVEL_LOAD,
        LEVEL_GAME,
        LEVEL_OPTIONS,

        NONE,
    }

    public static GameStateList getGameState(){
        return gameState;
    }

    public static void setGameState(GameStateList gameState){
        logger.debug("[GAMESTATE] Changing from " + GameState.gameState + " to " + gameState);
        GameState.gameState = gameState;
    }
}
