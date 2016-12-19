package com.winxaito.fastarcade.utils;

import com.winxaito.fastarcade.Main;
import com.winxaito.fastarcade.game.Game;

import java.text.DecimalFormat;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class Timer{
    protected final DecimalFormat dfSecond = new DecimalFormat("00.00");
    protected final DecimalFormat df = new DecimalFormat("00");
    private boolean reset = false;
    private int startTime;
    private int time;
    private boolean active;
    private int currentTime;

    public Timer(float timeInSecond){
        time = (int)(timeInSecond * Main.getTpsLimit());
    }

    public void start(){
        if(startTime == 0)
            startTime = Game.getCurrentTicks();

        active = true;
    }

    public void stop(){
        active = false;
    }

    public void reset(){
        if(!active){
            startTime = 0;
        }
    }

    public void update(){
        if(active)
            currentTime = Game.getCurrentTicks();
    }

    public boolean isActive(){
        return active;
    }

    public int getTicksTime(){
        if(active || time != 0)
            return time - (currentTime - startTime);
        else
            return 0;
    }

    public int getTimeSeconds(){
        return time - (currentTime - startTime) / Main.getTpsLimit();
    }

    public String getTimeFormat(){
        float ts = (float)(time - (currentTime - startTime)) / (float)Main.getTpsLimit();

        int h = (int)ts / 3600;
        int m = (int)(ts % 3600) / 60;
        float s = ts % 60f;

        if(ts < 60)
            return dfSecond.format(s);
        else if(ts < 3600)
            return df.format(m) + ":" + dfSecond.format(s);

        return df.format(h) + ":" + df.format(m) + ":" + dfSecond.format(s);
    }
}
