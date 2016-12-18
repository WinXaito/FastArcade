package com.winxaito.fastarcade.entities.items;

import com.winxaito.fastarcade.entities.Entity;
import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;
import com.winxaito.fastarcade.utils.Animation;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class Point extends Entity{
    private int quantity;
    private int texSize = 4;
    private Audio audio;

    public Point(int x, int y, int size, Level level){
        super(x, y, size, level);

        try{
            audio = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("sounds/boost/boost.ogg"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(){
        if(level.getPlayer().getX() > x - 42 && level.getPlayer().getX() < x + 42 &&
                level.getPlayer().getY() > y - 42 && level.getPlayer().getY() < y + 42){
            level.setPoint(level.getPoint() + 1);
            removed = true;
            audio.playAsSoundEffect(1.0f, 0.2f, false);
        }
    }

    @Override
    public void render(){
        Texture.textPoint.bind();
        Renderer.renderQuad(x, y, size, size, color, 0, 0, texSize);
    }
}
