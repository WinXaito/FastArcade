package com.winxaito.fastarcade.utils.keyboard;

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
