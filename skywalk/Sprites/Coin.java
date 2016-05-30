package com.sajko.skywalk.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.sajko.skywalk.Scenes.Hud;
import com.sajko.skywalk.Screens.PlayScreen;
import com.sajko.skywalk.Skywalk;

/**
 * Created by Sajko on 24.5.2016.
 */
public class Coin extends InteractiveTileObjects{

    private static TiledMapTileSet tileSet;

    public Coin(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Skywalk.COIN_BIT);
    }

    public void jackHit(Jack jack) {
        setCategoryFilter(Skywalk.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(100);
    }

}

