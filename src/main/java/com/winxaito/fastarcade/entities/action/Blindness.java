package com.winxaito.fastarcade.entities.action;

import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;
import org.lwjgl.opengl.Display;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class Blindness extends Action{
    public Blindness(Level level){
        super(level);
    }

    @Override
    public void activeAction(int timeInTick){
        startTick = Game.getCurrentTicks();
        timeTicks = timeInTick;

        active = true;
    }

    @Override
    public void update(){
        if(active)
            if(Game.getCurrentTicks() - startTick > timeTicks)
                active = false;
    }

    @Override
    public void render(){
        if(active)
            Renderer.renderTexture(200, 250,
                Display.getWidth(), Display.getHeight(), background);
    }

    @Override
    protected void defineTexture(){
        background = Texture.loadTexture("actions/blindness", "png");
    }

    @Override
    protected void defineActionType(){
        actionType = ActionType.BLINDNESS;
    }
}
