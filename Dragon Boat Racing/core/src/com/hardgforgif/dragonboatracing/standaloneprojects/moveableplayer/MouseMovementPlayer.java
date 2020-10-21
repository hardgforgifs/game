package com.hardgforgif.dragonboatracing.standaloneprojects.moveableplayer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class MouseMovementPlayer implements ApplicationListener, InputProcessor {
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;
    private Sprite reddot;
    private Sprite greendot;

    private float posX, posY; //player starting coordinates

    private Vector2 playerPos = new Vector2();
    private Vector2 mousePos = new Vector2();
    private Vector2 directionVector = new Vector2();
    private Vector2 velocity = new Vector2();
    private Vector2 movement = new Vector2();
    float speed = 150;
    float turningspeed = 2;

    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        batch = new SpriteBatch();

        texture = new Texture("arrow.png");
        sprite = new Sprite(texture);
        posX = w/2 - sprite.getWidth()/2;
        posY = h/2 - sprite.getHeight()/2;
        sprite.setPosition(posX,posY);
        Gdx.input.setInputProcessor(this);
        System.out.println(sprite.getOriginX());
        System.out.println(sprite.getOriginY());
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // getOrigin return the position of the center of the image, relative to the bottom left corner
        // in order to make the player rotate around it's center we need to find the coordinates of the image center
        float originX = sprite.getOriginX() + sprite.getX();
        float originY = sprite.getOriginY() + sprite.getY();

        // rotate the player
        float angle = MathUtils.atan2(mousePos.y - originY, mousePos.x - originX) * MathUtils.radDeg - 90;
        if(angle < 0)
        {
            angle = 360 + angle;
        }

        float angleDifference = angle - sprite.getRotation();
        if (Math.abs(angleDifference) < turningspeed)
            sprite.setRotation(angle);
        else {
            if (angleDifference < -180)
                angleDifference = (360 + angleDifference);
            else if (angleDifference > 180)
                angleDifference = (360 - angleDifference) * (-1); // The multiplication is required to
            // change the turning direction
            float newAngle = sprite.getRotation() + (turningspeed * (angleDifference / Math.abs(angleDifference)));
            if(newAngle < 0)
            {
                newAngle = 360 + newAngle;
            }
            if(newAngle > 360)
            {
                newAngle = newAngle - 360;
            }
            sprite.setRotation(newAngle);
        }

        System.out.println(sprite.getRotation());

        // we then need to calculate the position of the player's head
        Vector2 playerHeadPos = new Vector2();
        float radius = sprite.getHeight()/2;
        playerHeadPos.set(originX + radius * MathUtils.cosDeg(angle + 90),
                          originY + radius * MathUtils.sinDeg(angle + 90));

//         move the player
//         we move the player based on the direction vector between the player head and the mouse position


        Vector2 directionPosition = new Vector2();
        double auxAngle = sprite.getRotation() % 90;
        if (sprite.getRotation() < 90 || sprite.getRotation() >= 180 && sprite.getRotation() < 270)
            auxAngle = 90 - auxAngle;
        auxAngle = auxAngle * MathUtils.degRad;
        float x = (float) (Math.cos(auxAngle) * speed);
        float y = (float) (Math.sin(auxAngle) * speed);
//        float bSide = (float) Math.tan(auxAngle);
        if (sprite.getRotation() < 90)
            directionPosition.set(playerHeadPos.x - x, playerHeadPos.y + y);
        else if (sprite.getRotation() < 180)
            directionPosition.set(playerHeadPos.x - x, playerHeadPos.y - y);
        else if (sprite.getRotation() < 270)
            directionPosition.set(playerHeadPos.x + x, playerHeadPos.y - y);
        else
            directionPosition.set(playerHeadPos.x + x, playerHeadPos.y + y);

        playerPos.set(sprite.getX(), sprite.getY());
        directionVector.set(directionPosition).sub(playerHeadPos).nor();
        velocity.set(directionVector).scl(speed);
        movement.set(velocity).scl(Gdx.graphics.getDeltaTime());
        if (playerHeadPos.dst2(mousePos) > movement.len2()) {
            playerPos.add(movement);
        } else {
            playerHeadPos.set(mousePos);
        }
        sprite.setX(playerPos.x);
        sprite.setY(playerPos.y);

        batch.begin();
        sprite.draw(batch);
        batch.end();

//        shapeRenderer.setColor(Color.BLACK);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.circle(directionPosition.x, directionPosition.y, 5);
//        shapeRenderer.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePos.set(screenX, Gdx.graphics.getHeight() - screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}