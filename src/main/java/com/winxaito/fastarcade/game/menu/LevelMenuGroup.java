package com.winxaito.fastarcade.game.menu;

import com.winxaito.fastarcade.game.Game;
import com.winxaito.fastarcade.game.level.Level;
import com.winxaito.fastarcade.game.menu.components.Button;

/**
 * Created by: WinXaito (Kevin Vuilleumier)
 */
public class LevelMenuGroup extends Menu{
    private String levelName;

    private Button buttonPlay;
    private Button buttonRemove;

    public LevelMenuGroup(String levelName, int group){
        super();
        this.levelName = levelName;

        buttonPlay = new Button(50 + group * 250, "Jouer (" + levelName + ")"){
            @Override
            public void onClick(Button button){
                game.loadLevel(levelName);
            }
        };

        buttonRemove = new Button(150 +  group * 250, "Supprimer"){
            @Override
            public void onClick(Button button){
                System.out.println("Remove level: " + levelName);
            }
        };
    }

    public void setGame(Game game){
        this.game = game;
    }

    @Override
    public void load(){
    }

    @Override
    public void update(){
        buttonPlay.update();
        buttonRemove.update();
    }

    @Override
    public void render(){
        buttonPlay.render();
        buttonRemove.render();
    }
}
