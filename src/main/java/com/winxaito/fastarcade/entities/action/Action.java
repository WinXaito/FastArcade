package com.winxaito.fastarcade.entities.action;

import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.render.Texture;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public abstract class Action{
    protected float backgroundX, backgroundY;
    protected Texture background;
    protected Texture text;
    protected ActionType actionType = ActionType.NOT_DEFINED;

    protected Level level;
    protected int startTick = 0;
    protected int timeTicks = 0;
    protected boolean active = false;

    public enum ActionType{
        BLINDNESS,

        NOT_DEFINED
    }

    public Action(Level level){
        this.level = level;

        defineTexture();
        defineActionType();
    }

    public abstract void activeAction(int timeInTick);
    public abstract void update();
    public abstract void render();
    protected abstract void defineTexture();
    protected abstract void defineActionType();

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
