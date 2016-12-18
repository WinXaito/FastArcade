package com.winxaito.fastarcade.game.menu.components;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public interface IButton{
    void onClick(Button button);
    default void onStartHover(Button button){}
    default void onStopHover(Button button){}
    default void onWhileHover(Button button){}
}
