package com.winxaito.fastarcade.entities.action;

import com.winxaito.fastarcade.Main;
import com.winxaito.fastarcade.entities.Player;
import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;
import org.lwjgl.opengl.Display;

import java.text.DecimalFormat;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public abstract class Action{
    protected final DecimalFormat dfSecondOnly = new DecimalFormat("00.0");
    protected final DecimalFormat dfSecond = new DecimalFormat("00");
    protected final DecimalFormat dfMinut = new DecimalFormat("00");
    protected final DecimalFormat dfHour = new DecimalFormat("00");

    protected float backgroundX, backgroundY;
    protected Texture texture;
    protected Texture text;
    protected ActionType actionType = ActionType.NOT_DEFINED;

    protected Level level;
    protected int startTick = 0;
    protected int timeTicks = 0;
    protected boolean active = false;

    public enum ActionType{
        BLINDNESS(1, "Blindness"),
        BLINK(2, "Blink"),

        NOT_DEFINED(0, "Not defined");

        private int id;
        private String name;

        ActionType(int id, String name){
            this.id = id;
            this.name = name;
        }

        public int getId(){
            return id;
        }

        public String getName(){
            return name;
        }
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

    public int getTimeLeftTicks(){
        if(!active)
            return 0;

        return timeTicks - (Game.getCurrentTicks() - startTick);
    }

    public int getTimeLeftSecond(){
        return getTimeLeftTicks() / Main.getTpsLimit();
    }

    public String getTimeLeftSecondFormat(){
        return dfSecondOnly.format((float)getTimeLeftTicks() / (float)Main.getTpsLimit());
    }

    public String getTimeLeftFormat(){
        float ts = (float)getTimeLeftTicks() / (float)Main.getTpsLimit();

        int h = (int)ts / 3600;
        int m = (int)(ts % 3600) / 60;
        float s = ts % 60f;

        if(ts < 60)
            return dfSecondOnly.format(s);
        else if(ts < 3600)
            return dfMinut.format(m) + ":" + dfSecond.format(s);

        return dfHour.format(h) + ":" + dfMinut.format(m) + ":" + dfSecond.format(s);
    }

    public boolean isActive(){
        return active;
    }
}
