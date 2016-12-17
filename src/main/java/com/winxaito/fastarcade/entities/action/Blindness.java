package com.winxaito.fastarcade.entities.action;

import com.winxaito.fastarcade.entities.Player;
import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class Blindness extends Action{
    public Blindness(Level level){
        super(level, ActionType.BLINDNESS);
        texture = Texture.loadTexture("actions/blindness", "png");
    }

    @Override
    public void updateAction(){
    }
}
