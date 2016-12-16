package com.winxaito.fastarcade.utils.keyboard;

import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.GameState;
import org.lwjgl.input.Keyboard;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class FaKeyboard{
    public static boolean isKeyDownLoop(int key){
        if(Keyboard.next() && Keyboard.getEventKeyState())
            if(Keyboard.isKeyDown(key))
                return true;

        return false;
    }
}
