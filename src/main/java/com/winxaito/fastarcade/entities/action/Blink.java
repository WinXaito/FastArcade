package com.winxaito.fastarcade.entities.action;

import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class Blink extends Action{
    private final int colorTicksTime = 30;
    private Color color;

    private float alpha = 0;
    private int colorTick = 0;
    private boolean up = true;

    public Blink(Level level){
        super(level, ActionType.BLINK);
        texture = Texture.loadTexture("actions/blink", "png");
    }

    @Override
    public void updateAction(){
        if(active){
            if(up){
                colorTick++;

                if(colorTick >= colorTicksTime)
                    up = false;
            }else{
                colorTick--;

                if(colorTick <= 0)
                    up = true;
            }

            alpha = (float)colorTick / (float)colorTicksTime;
        }
    }

    @Override
    public void render(){
        if(active)
            Renderer.renderTexture(backgroundX, backgroundY,
                    Display.getWidth(), Display.getHeight(), texture, alpha);
    }
}
