package com.winxaito.fastarcade.game.level;

import com.winxaito.fastarcade.render.Renderer;
import com.winxaito.fastarcade.render.Texture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import static com.winxaito.fastarcade.render.Texture.loadTexture;


/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class Background{
    private static int size = 64*64;
    private Texture texture = loadTexture("background", "png");
    private Vector2f position = new Vector2f(0, 0);
    private float speed;
    private int xTileable;
    private int yTileable;

    public Background(){
        this(1, 1, 0.3f);
    }

    public Background(int xTileable, int yTileable, float speed){
        this.xTileable = xTileable * size;
        this.yTileable = yTileable * size;
        this.speed = speed;
    }

    public void update(Vector2f playerPosition){
        //Update position of background
        position.setX(playerPosition.getX() * speed);
        position.setY(position.getY() + (playerPosition.getY() - position.getY()) + speed);
    }

    public void render(){
        texture.bind();

        //Le "-250" est utilisé pour pousser le background contre la gauche à cause de
        // son mouvement (Pour pas voir le fond noir
        Renderer.renderQuad(position.getX() - 1000 * speed, 6, size, size, Color.white, 0, 0, 1);
    }
}
