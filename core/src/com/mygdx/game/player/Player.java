package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.main.Map;
import com.mygdx.game.utilities.Animator;

public class Player{
    //CONSTANTS
    private float PLAYER_WIDTH = 84, PLAYER_HEIGHT = 84, COLLISION_WIDTH = 5, COLLISION_HEIGHT = 5,
                  LOS_WIDTH = 180, LOS_HEIGHT = 180;
    private float centerX = Gdx.graphics.getWidth() / 2f;
    private float centerY = Gdx.graphics.getHeight() / 2f;

    // Calculate the position to draw the player sprite
    private float playerDrawX = centerX - PLAYER_WIDTH / 2f;
    private float playerDrawY = centerY - PLAYER_HEIGHT / 2f;

    //Player Attributes
//    private int health;
//    private int coins;

    //Player Prerequisites
    public SpriteBatch spriteBatch;
    public Sprite character;
    private Animator animator;
    private float stateTime;
    private int speed;
    private boolean isMoving;
    private boolean isMovingLeft;

    //Player Collision Attributes
    private MapObjects collision_objects;
    private Rectangle player_bounds;
    private float previous_x;
    private float previous_y;

    //Player Line of Sight
    private Rectangle line_of_sight;

    //Player preloaded textures
    private Texture idle;
    private Texture run;
    private Texture idle_inverse;
    private Texture run_inverse;

    //Debugging
    private ShapeRenderer shapeRendererCollision;
    private ShapeRenderer shapeRendererLOS;

    public Player(){
        character = new Sprite(new Texture("assets/Full body animated characters/Char 4/no hands/idle_0.png"));
        character.setScale(-2f);
        spriteBatch = new SpriteBatch();
        animator = new Animator();
        stateTime = 0f;
        speed = 55 * 2;
        isMoving = false;
        isMovingLeft = false;

        collision_objects = new Map().getCollissionObjects();
        player_bounds = new Rectangle(playerDrawX, playerDrawY, COLLISION_WIDTH, COLLISION_HEIGHT);
        line_of_sight = new Rectangle(playerDrawX, playerDrawY, LOS_WIDTH, LOS_HEIGHT);
        previous_x = 0;
        previous_y = 0;
        shapeRendererCollision = new ShapeRenderer();
        shapeRendererLOS = new ShapeRenderer();

        idle = new Texture(Gdx.files.internal("animations/idle_test.png"));
        run = new Texture(Gdx.files.internal("animations/run_test.png"));
        idle_inverse = new Texture(Gdx.files.internal("animations/idle_inverse_test.png"));
        run_inverse = new Texture(Gdx.files.internal("animations/run_inverse_test.png"));
    }

    public void handleMovement(OrthographicCamera camera){
        stateTime += Gdx.graphics.getDeltaTime() * 0.30f;
        TextureRegion currentFrame = null;


          //debugging
//        shapeRendererLOS.setProjectionMatrix(camera.combined);
//        shapeRendererLOS.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRendererLOS.setColor(Color.BLUE);
//        shapeRendererLOS.rect(line_of_sight.x, line_of_sight.y, line_of_sight.width, line_of_sight.height);
//        shapeRendererLOS.end();
//
//        shapeRendererCollision.setProjectionMatrix(camera.combined);
//        shapeRendererCollision.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRendererCollision.setColor(Color.RED); // Adjust color as needed
//        shapeRendererCollision.rect(player_bounds.x, player_bounds.y, player_bounds.width, player_bounds.height);
//        shapeRendererCollision.end();

        for (MapObject object : collision_objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                // Check for collision
                if (Intersector.overlaps(player_bounds, rectangle)) {
                    // Determine the direction of collision (left, right, top, bottom)
                    float overlapX = Math.max(0, Math.min(player_bounds.x + player_bounds.width, rectangle.x + rectangle.width) - Math.max(player_bounds.x, rectangle.x));
                    float overlapY = Math.max(0, Math.min(player_bounds.y + player_bounds.height, rectangle.y + rectangle.height) - Math.max(player_bounds.y, rectangle.y));

                    // Adjust player's position based on the collision direction
                    if (overlapX < overlapY) {
                        // Horizontal collision
                        if (player_bounds.x < rectangle.x) {
                            // Collided from the left
                            character.setX(rectangle.x - player_bounds.width);
                        } else {
                            // Collided from the right
                            character.setX(rectangle.x + rectangle.width);
                        }
                    } else {
                        // Vertical collision
                        if (player_bounds.y < rectangle.y) {
                            // Collided from the bottom
                            character.setY(rectangle.y - player_bounds.height);
                        } else {
                            // Collided from the top
                            character.setY(rectangle.y + rectangle.height);
                        }
                    }
                }
            }
        }

        isMovingLeft = checkDirectionFacing(camera);

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            previous_x = character.getX();
            character.setX(previous_x -= Gdx.graphics.getDeltaTime() * speed);
            isMoving = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            previous_x = character.getX();
            character.setX(previous_x += Gdx.graphics.getDeltaTime() * speed);
            isMoving = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            previous_y = character.getY();
            character.setY(previous_y += Gdx.graphics.getDeltaTime() * speed);
            isMoving = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            previous_y = character.getY();
            character.setY(previous_y -= Gdx.graphics.getDeltaTime() * speed);
            isMoving = true;
        }

        if(isMoving){
            if(isMovingLeft){
                currentFrame = animator.animateRun(run_inverse).getKeyFrame(stateTime, true);
            }
            else{
                currentFrame = animator.animateRun(run).getKeyFrame(stateTime, true);
            }
        }

        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(camera.combined);

        if(isMoving){
            spriteBatch.draw(currentFrame, character.getX() - 40, character.getY() - 10, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        else{
            TextureRegion idles = null;
            if(isMovingLeft){
                idles = animator.animateIdle(idle_inverse).getKeyFrame(stateTime, true);
            }
            if(!isMovingLeft){
                idles = animator.animateIdle(idle).getKeyFrame(stateTime, true);
            }
            spriteBatch.draw(idles, character.getX() - 40, character.getY() - 10, PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        //collision box
        player_bounds = new Rectangle(character.getX(), character.getY(), COLLISION_WIDTH, COLLISION_HEIGHT);

        //line of sight
        line_of_sight = new Rectangle(character.getX() - (LOS_WIDTH / 2), character.getY() - (LOS_HEIGHT / 2), LOS_WIDTH, LOS_HEIGHT);

        isMoving = false;
        spriteBatch.end();
    }

    private boolean checkDirectionFacing(OrthographicCamera camera){
        Vector3 position = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return !(position.x - character.getX() > 0);
    }
}
