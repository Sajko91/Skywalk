package com.sajko.skywalk.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.sajko.skywalk.Screens.PlayScreen;
import com.sajko.skywalk.Skywalk;

/**
 * Created by Sajko on 24.5.2016.
 */
public class BlackClouds extends com.sajko.skywalk.Sprites.Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    float angle;

    public BlackClouds(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("blackclouds"), i * 16, 0, 16, 16));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / Skywalk.PPM, 16 / Skywalk.PPM);
        setToDestroy = false;
        destroyed = false;
        angle = 0;
    }


    @Override
    protected void defineEnemy() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Skywalk.PPM);
        fdef.filter.categoryBits = Skywalk.ENEMY_BIT;
        fdef.filter.maskBits = Skywalk.GROUND_BIT |
                Skywalk.COIN_BIT |
                Skywalk.CLOUD_BIT |
                Skywalk.ENEMY_BIT |
                Skywalk.OBJECT_BIT |
                Skywalk.JACK_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);


        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / Skywalk.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / Skywalk.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / Skywalk.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / Skywalk.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Skywalk.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void update(float dt) {

        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("blackclouds"), 32, 0, 16, 16));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    public void hitOnHead(Jack jack) {
        setToDestroy = true;
    }

    public void hitByEnemy(Enemy enemy) {
        reverseVelocity(true, false);
    }


}
