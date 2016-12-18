package com.winxaito.fastarcade.utils.keyboard;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class FaKeyboard{
    public enum Key{
        KEY_ESPACE(false, Keyboard.KEY_ESCAPE),
        KEY_BACK(false, Keyboard.KEY_BACK),
        KEY_TAB(false, Keyboard.KEY_TAB),
        KEY_RETURN(false, Keyboard.KEY_RETURN),
        KEY_LCONTROL(false, Keyboard.KEY_LCONTROL),
        KEY_RCONTROL(false, Keyboard.KEY_RCONTROL),
        KEY_LSHIFT(false, Keyboard.KEY_LSHIFT),
        KEY_RSHIFT(false, Keyboard.KEY_RSHIFT),
        KEY_COMMA(false, Keyboard.KEY_COMMA),
        KEY_SPACE(false, Keyboard.KEY_SPACE),

        KEY_F1(false, Keyboard.KEY_F1),
        KEY_F2(false, Keyboard.KEY_F2),
        KEY_F3(false, Keyboard.KEY_F3),
        KEY_F4(false, Keyboard.KEY_F4),
        KEY_F5(false, Keyboard.KEY_F5),
        KEY_F6(false, Keyboard.KEY_F6),
        KEY_F7(false, Keyboard.KEY_F7),
        KEY_F8(false, Keyboard.KEY_F8),
        KEY_F9(false, Keyboard.KEY_F9),
        KEY_F10(false, Keyboard.KEY_F10),
        KEY_F11(false, Keyboard.KEY_F11),
        KEY_F12(false, Keyboard.KEY_F12),

        KEY_NUMPAD0(false, Keyboard.KEY_NUMPAD0),
        KEY_NUMPAD1(false, Keyboard.KEY_NUMPAD1),
        KEY_NUMPAD2(false, Keyboard.KEY_NUMPAD2),
        KEY_NUMPAD3(false, Keyboard.KEY_NUMPAD3),
        KEY_NUMPAD4(false, Keyboard.KEY_NUMPAD4),
        KEY_NUMPAD5(false, Keyboard.KEY_NUMPAD5),
        KEY_NUMPAD6(false, Keyboard.KEY_NUMPAD6),
        KEY_NUMPAD7(false, Keyboard.KEY_NUMPAD7),
        KEY_NUMPAD8(false, Keyboard.KEY_NUMPAD8),
        KEY_NUMPAD9(false, Keyboard.KEY_NUMPAD9),

        KEY_UP(false, Keyboard.KEY_UP),
        KEY_DOWN(false, Keyboard.KEY_DOWN),
        KEY_RIGHT(false, Keyboard.KEY_RIGHT),
        KEY_LEFT(false, Keyboard.KEY_LEFT),

        KEY_A(false, Keyboard.KEY_A),
        KEY_B(false, Keyboard.KEY_B),
        KEY_C(false, Keyboard.KEY_C),
        KEY_D(false, Keyboard.KEY_D),
        KEY_E(false, Keyboard.KEY_E),
        KEY_F(false, Keyboard.KEY_F),
        KEY_G(false, Keyboard.KEY_G),
        KEY_H(false, Keyboard.KEY_H),
        KEY_I(false, Keyboard.KEY_I),
        KEY_J(false, Keyboard.KEY_J),
        KEY_K(false, Keyboard.KEY_K),
        KEY_L(false, Keyboard.KEY_L),
        KEY_M(false, Keyboard.KEY_M),
        KEY_N(false, Keyboard.KEY_N),
        KEY_O(false, Keyboard.KEY_O),
        KEY_P(false, Keyboard.KEY_P),
        KEY_Q(false, Keyboard.KEY_Q),
        KEY_R(false, Keyboard.KEY_R),
        KEY_S(false, Keyboard.KEY_S),
        KEY_T(false, Keyboard.KEY_T),
        KEY_U(false, Keyboard.KEY_U),
        KEY_V(false, Keyboard.KEY_V),
        KEY_W(false, Keyboard.KEY_W),
        KEY_X(false, Keyboard.KEY_X),
        KEY_Y(false, Keyboard.KEY_Y),
        KEY_Z(false, Keyboard.KEY_Z),

        KEY_0(false, Keyboard.KEY_0),
        KEY_1(false, Keyboard.KEY_1),
        KEY_2(false, Keyboard.KEY_2),
        KEY_3(false, Keyboard.KEY_3),
        KEY_4(false, Keyboard.KEY_4),
        KEY_5(false, Keyboard.KEY_5),
        KEY_6(false, Keyboard.KEY_6),
        KEY_7(false, Keyboard.KEY_7),
        KEY_8(false, Keyboard.KEY_8),
        KEY_9(false, Keyboard.KEY_9);

        private boolean down;
        private int keyCode;

        Key(boolean down, int keyCode){
            this.down = down;
            this.keyCode = keyCode;
        }

        public boolean isDown(){
            return down;
        }

        public void setDown(boolean down){
            this.down = down;
        }

        public int getKeyCode(){
            return keyCode;
        }
    }

    public static void update(){
        while(Keyboard.next()){
            for(Key key : Key.values()){
                if(!key.isDown())
                    if(Keyboard.isKeyDown(key.getKeyCode()) && Keyboard.getEventKey() == key.getKeyCode())
                        key.setDown(true);
            }
        }
    }

    public static boolean isKeyDown(Key key){
        boolean t = key.isDown();

        if(key.isDown())
            key.setDown(false);

        return t;
    }
}
