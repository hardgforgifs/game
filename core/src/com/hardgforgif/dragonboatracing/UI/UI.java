package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public abstract class UI {

    public abstract void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta);

    public abstract void drawPlayerUI(Batch batch, Player playerBoat);

    public abstract void getInput(float screenWidth, Vector2 mousePos);

    public void playMusic(){
        if (!GameData.music.isPlaying()) {
            GameData.music.play();
        }
    }


}
