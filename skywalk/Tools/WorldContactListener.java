package com.sajko.skywalk.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sajko.skywalk.Skywalk;
import com.sajko.skywalk.Sprites.Enemy;
import com.sajko.skywalk.Sprites.InteractiveTileObjects;
import com.sajko.skywalk.Sprites.Jack;

/**
 * Created by Sajko on 24.5.2016.
 */
public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case Skywalk.JACK_BIT | Skywalk.COIN_BIT:
                if(fixA.getFilterData().categoryBits == Skywalk.JACK_BIT)
                    ((InteractiveTileObjects) fixB.getUserData()).jackHit((Jack) fixA.getUserData());
                else
                    ((InteractiveTileObjects) fixA.getUserData()).jackHit((Jack) fixB.getUserData());
                break;
            case Skywalk.ENEMY_HEAD_BIT | Skywalk.JACK_BIT:
                if(fixA.getFilterData().categoryBits == Skywalk.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead((Jack) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((Jack) fixA.getUserData());
                break;
            case Skywalk.ENEMY_BIT | Skywalk.COIN_BIT:
                if(fixA.getFilterData().categoryBits == Skywalk.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Skywalk.JACK_BIT | Skywalk.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == Skywalk.JACK_BIT)
                    ((Jack) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Jack) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;
            case Skywalk.ENEMY_BIT | Skywalk.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
