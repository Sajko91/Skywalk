package com.sajko.skywalk.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sajko.skywalk.Screens.PlayScreen;
import com.sajko.skywalk.Skywalk;

/**
 * Created by Sajko on 24.5.2016.
 */
public class Jack extends Sprite{

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD };
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion jackStand;
    private Animation jackRun;
    private TextureRegion jackJump;
    private TextureRegion jackDead;
    private float stateTimer;
    private boolean runningRight;
    private boolean jackIsDead;
    private PlayScreen screen;

    public Jack(PlayScreen screen){

        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("jack"), i * 16, 0, 16, 16));
        jackRun = new Animation(0.1f, frames);

        frames.clear();

        jackJump = new TextureRegion(screen.getAtlas().findRegion("jack"), 80, 0, 16, 16);

        jackStand = new TextureRegion(screen.getAtlas().findRegion("jack"), 0, 0, 16, 16);

        jackDead = new TextureRegion(screen.getAtlas().findRegion("jack"), 96, 0, 16, 16);

        defineJack();

        setBounds(0, 0, 16 / Skywalk.PPM, 16 / Skywalk.PPM);
        setRegion(jackStand);


    }

    public void update(float dt){

        if (isDead()) {
            die();
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

    }

    public TextureRegion getFrame(float dt){

        currentState = getState();

        TextureRegion region;


        switch(currentState){
            case DEAD:
                region = jackDead;
                break;
            case JUMPING:
                region = jackJump;
                break;
            case RUNNING:
                region = jackRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region =  jackStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        previousState = currentState;

        return region;

    }

    public State getState(){

        if(jackIsDead)
            return State.DEAD;

        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;

        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;

        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;

        else
            return State.STANDING;
    }

    public void die() {

        if (!isDead()) {

            jackIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = Skywalk.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public boolean isDead(){
        return jackIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void hit(Enemy enemy) {
        die();
    }

    public void defineJack(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Skywalk.PPM, 32 / Skywalk.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Skywalk.PPM);
        fdef.filter.categoryBits = Skywalk.JACK_BIT;
        fdef.filter.maskBits = Skywalk.GROUND_BIT |
                Skywalk.CLOUD_BIT |
                Skywalk.COIN_BIT |
                Skywalk.OBJECT_BIT |
                Skywalk.ENEMY_BIT |
                Skywalk.ENEMY_HEAD_BIT |
                Skywalk.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Skywalk.PPM, 6 / Skywalk.PPM), new Vector2(2 / Skywalk.PPM, 6 / Skywalk.PPM));
        fdef.filter.categoryBits = Skywalk.JACK_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        super.draw(batch);

    }
}
