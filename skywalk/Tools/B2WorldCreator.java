package com.sajko.skywalk.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sajko.skywalk.Screens.PlayScreen;
import com.sajko.skywalk.Skywalk;
import com.sajko.skywalk.Sprites.BlackClouds;
import com.sajko.skywalk.Sprites.Cloud;
import com.sajko.skywalk.Sprites.Coin;
import com.sajko.skywalk.Sprites.Enemy;

/**
 * Created by Sajko on 24.5.2016.
 */
public class B2WorldCreator {

    private Array<BlackClouds> blackClouds;

    public B2WorldCreator(PlayScreen screen) {

        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Skywalk.PPM, (rect.getY() + rect.getHeight() / 2) / Skywalk.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Skywalk.PPM, rect.getHeight() / 2 / Skywalk.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            new Cloud(screen, object);
        }

        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){

            new Coin(screen, object);
        }

        blackClouds = new Array<BlackClouds>();
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            blackClouds.add(new BlackClouds(screen, rect.getX() / Skywalk.PPM, rect.getY() / Skywalk.PPM));
        }

    }

    public Array<BlackClouds> getBlackClouds() {
        return blackClouds;
    }

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(blackClouds);
        return enemies;
    }

}
