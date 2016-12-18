package com.winxaito.fastarcade.game.menu;

import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.state.MenuState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class Menus{
    private HashMap<MenuList, Menu> menus = new HashMap<>();

    public enum MenuList{
        MENU_LOADING,
        MENU_MAIN,
        MENU_LEVEL,
        MENU_OPTIONS,

        NONE
    }

    public Menus(Game game, LoadingMenu loadingMenu){
        menus.put(MenuList.MENU_LOADING, loadingMenu);
        menus.put(MenuList.MENU_MAIN, new MainMenu(game));
        menus.put(MenuList.MENU_LEVEL, new LevelMenu(game));
        menus.put(MenuList.MENU_OPTIONS, new OptionsMenu(game));
    }

    public void load(){
        for(Map.Entry<MenuList, Menu> menu : menus.entrySet()){
            menu.getValue().load();
        }
    }

    public void update(){
        switch(MenuState.getState()){
            case MAIN:
                menus.get(MenuList.MENU_MAIN).update();
                break;
            case LEVEL:
                menus.get(MenuList.MENU_LEVEL).update();
                break;
            case OPTIONS:
                menus.get(MenuList.MENU_OPTIONS).update();
                break;
        }
    }

    public void render(){
        switch(MenuState.getState()){
            case MAIN:
                menus.get(MenuList.MENU_MAIN).render();
                break;
            case LEVEL:
                menus.get(MenuList.MENU_LEVEL).render();
                break;
            case OPTIONS:
                menus.get(MenuList.MENU_OPTIONS).render();
                break;
        }
    }
}
