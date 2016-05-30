package com.sajko.skywalk.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sajko.skywalk.Scenes.Hud;
import com.sajko.skywalk.Skywalk;
import com.sajko.skywalk.Sprites.Enemy;
import com.sajko.skywalk.Sprites.Jack;
import com.sajko.skywalk.Tools.B2WorldCreator;
import com.sajko.skywalk.Tools.Controller;
import com.sajko.skywalk.Tools.WorldContactListener;

/**
 * Created by Sajko on 23.5.2016.
 */
public class PlayScreen implements Screen {

    private Skywalk game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Jack player;
    private Controller controller;


    public PlayScreen(Skywalk game){

        atlas = new TextureAtlas("Jack_and_BlackClouds.pack");

        this.game = game;

        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(Skywalk.V_WIDTH / Skywalk.PPM, Skywalk.V_HEIGHT / Skywalk.PPM, gamecam);

        hud = new Hud(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("levelf.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1  / Skywalk.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);

        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Jack(this);

        controller = new Controller(game.batch);

        world.setContactListener(new WorldContactListener());

    }

    public void handleInput(float dt) {
            if (controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0)
                player.b2body.applyLinearImpulse(new Vector2(0, 4), player.b2body.getWorldCenter(), true); //true - will this impulse wake object.
            if (controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

    }
    public TextureAtlas getAtlas(){
        return atlas;
    }


    public void update(float dt){

        handleInput(dt);
        world.step(1 / 60f, 6, 2);

        player.update(dt);
        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224 / Skywalk.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        hud.update(dt);

        if(player.currentState != Jack.State.DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }

        gamecam.update();

        renderer.setView(gamecam);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        controller.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

    }

    public boolean gameOver(){
        if(player.currentState == Jack.State.DEAD && player.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width,height);

    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }

    public Hud getHud(){
        return hud;
    }

}
