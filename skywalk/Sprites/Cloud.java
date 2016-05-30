package com.sajko.skywalk.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.sajko.skywalk.Screens.PlayScreen;
import com.sajko.skywalk.Skywalk;

/**
 * Created by Sajko on 24.5.2016.
 */
public class Cloud extends InteractiveTileObjects{

    public Cloud(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Skywalk.CLOUD_BIT);
    }

    public void jackHit(Jack jack){

    }
}
