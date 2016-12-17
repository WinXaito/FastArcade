package com.winxaito.fastarcade.entities.action;

import com.winxaito.fastarcade.entities.Player;
import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;
import org.lwjgl.opengl.Display;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public abstract class Action{
    protected float backgroundX, backgroundY;
    protected Texture texture;
    protected Texture text;
    protected ActionType actionType = ActionType.NOT_DEFINED;

    protected Level level;
    protected int startTick = 0;
    protected int timeTicks = 0;
    protected boolean active = false;

    public enum ActionType{
        BLINDNESS,
        BLINK,

        NOT_DEFINED
    }

    public Action(Level level, ActionType actionType){
        this.level = level;
        this.actionType = actionType;
    }

    public abstract void updateAction();

    public void activeAction(int timeInTick){
        startTick = Game.getCurrentTicks();
        timeTicks = timeInTick;

        active = true;
    }

    public void update(){
        if(active){
            if(Game.getCurrentTicks() - startTick > timeTicks)
                active = false;

            Player p = level.getPlayer();
            int width = Display.getWidth();
            int height = Display.getHeight();
            float x = p.getX() + p.getSize() / 2 - width / 2;
            float y = p.getY() + p.getSize() / 2 - height / 2;

            if(-x > level.getLeftLimit())
                x = level.getLeftLimit();
            if(-y > level.getTopLimit())
                y = level.getTopLimit();
            if(-x < level.getRightLimit())
                x = -level.getRightLimit();
            if(-y < level.getBottomLimit())
                y = -level.getBottomLimit();

            backgroundX = x;
            backgroundY = y;
        }

        updateAction();
    }

    public void render(){
        if(active)
            Renderer.renderTexture(backgroundX, backgroundY,
                    Display.getWidth(), Display.getHeight(), texture);
    }


    public ActionType getActionType(){
        return actionType;
    }

    public void setBackgroundX(float backgroundX){
        this.backgroundX = backgroundX;
    }

    public void setBackgroundY(float backgroundY){
        this.backgroundY = backgroundY;
    }

    public void setBackgroundPositions(float backgroundX, float backgroundY){
        this.backgroundX = backgroundX;
        this.backgroundY = backgroundY;
    }

    public int getStartTick(){
        if(active)
            return startTick;

        return 0;
    }

    public int getTimeTicks(){
        if(active)
            return timeTicks;

        return 0;
    }

    public boolean isActive(){
        return active;
    }
}
