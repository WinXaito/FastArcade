package com.winxaito.fastarcade.game.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class GameState extends State{
    private static final Logger logger = LogManager.getLogger(GameState.class.getName());
    private static GameStateList gameState = GameStateList.STARTING;

    public enum GameStateList{
        STARTING,
        MENU,
        GAME,

        NONE,
    }

    public static GameStateList getState(){
        return gameState;
    }

    public static void setState(GameStateList gameState){
        logger.debug("[GAMESTATE] Changing from " + GameState.gameState + " to " + gameState);
        GameState.gameState = gameState;
    }

    public static boolean isState(GameStateList gameState){
        return GameState.gameState == gameState;
    }
}
