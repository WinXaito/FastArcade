package com.winxaito.fastarcade.game.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class MenuState extends State{
    private static final Logger logger = LogManager.getLogger(GameState.class.getName());
    private static MenuState.MenuStateList menuState = MenuStateList.MAIN;

    public enum MenuStateList{
        MAIN,
        LEVEL,
        OPTIONS,

        NONE,
    }

    public static MenuState.MenuStateList getState(){
        return menuState;
    }

    public static void setState(MenuState.MenuStateList menuState){
        logger.debug("[GAMESTATE] Changing from " + MenuState.menuState + " to " + MenuState.menuState);
        MenuState.menuState = menuState;
    }

    public static boolean isGameState(MenuState.MenuStateList menuState){
        return MenuState.menuState == MenuState.menuState;
    }
}
