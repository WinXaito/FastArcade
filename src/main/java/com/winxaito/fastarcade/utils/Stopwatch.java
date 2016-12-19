package com.winxaito.fastarcade.utils;

import com.winxaito.fastarcade.Main;
import com.winxaito.fastarcade.game.Game;

import java.text.DecimalFormat;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class Stopwatch{
    protected final DecimalFormat dfSecond = new DecimalFormat("00.00");
    protected final DecimalFormat df = new DecimalFormat("00");
    private boolean reset = false;
    private int startTime;
    private int time;
    private boolean active;

    public void start(){
        if(startTime == 0 && time == 0)
            startTime = Game.getCurrentTicks();

        active = true;
    }

    public void stop(){
        active = false;
    }

    public void reset(){
        if(!active){
            startTime = 0;
            time = 0;
        }
    }

    public void update(){
        if(active)
            time = Game.getCurrentTicks();
    }

    public boolean isActive(){
        return active;
    }

    public int getTicksTime(){
        if(active || time != 0)
            return startTime - time;
        else
            return 0;
    }

    public int getTimeSeconds(){
        return (time - startTime) / Main.getTpsLimit();
    }

    public String getTimeFormat(){
        float ts = (float)(time - startTime) / (float)Main.getTpsLimit();

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
