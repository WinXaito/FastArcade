package com.winxaito.fastarcade.game.menu.hud;

import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.GameState;
import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.game.menu.Menu;
import com.winxaito.fastarcade.game.menu.components.Button;
import com.winxaito.fastarcade.game.menu.components.IButton;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class OptionsHud extends Menu{
    private Level level;
    private boolean visible;

    private ArrayList<Button> buttons = new ArrayList<>();
    private Button buttonReturn;
    private Button buttonOptions;
    private Button buttonReturnMainMenu;
    private Button buttonExitGame;

    public OptionsHud(Game game, Level level){
        super(game);
        this.level = level;

        load();
    }

    @Override
    public void load(){
        buttonReturn = new Button(50, "Retour"){
            @Override
            public void onClick(Button button){
                visible = false;
            }
        };

        buttonOptions = new Button(150, "Retourner au menu principal"){
            @Override
            public void onClick(Button button){
                level.unloadLevel();
                GameState.setState(GameState.GameStateList.MENU);
            }
        };

        buttonReturnMainMenu = new Button(250, "Options"){
            @Override
            public void onClick(Button button){
                System.out.println("CLICK OPTIONS");
            }
        };

        buttonExitGame = new Button(Display.getWidth() - 350, Display.getHeight() - 100, "Quitter"){
            @Override
            public void onClick(Button button){
                game.exit();
            }
        };

        buttons.add(buttonReturn);
        buttons.add(buttonOptions);
        buttons.add(buttonReturnMainMenu);
        buttons.add(buttonExitGame);
    }

    @Override
    public void update(){
        if(visible){
            buttons.forEach(Button::update);

            if(Display.wasResized()){
                buttonExitGame.setX(Display.getWidth() - 400);
                buttonExitGame.setY(Display.getHeight() - 200);
            }
        }
    }

    @Override
    public void render(){
        if(visible)
            buttons.forEach(Button::render);
    }

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public void toggleVisible(){
        visible = !visible;
    }
}
