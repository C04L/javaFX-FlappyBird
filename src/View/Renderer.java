package View;

import GameObjects.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.LinkedHashMap;
import java.util.Map;

public class Renderer {
    private double width;
    private double height;
    private GraphicsContext ctx;
    private Map<String, GameObject> gameObjects;

    private Bird bird;
    private Restart restart;
    private GameState gameState;
    private Font appFont;
    private Color appColor;
    private Difficulty difficulty;

    public Renderer(double width, double height, GraphicsContext ctx, Font font, Color appColor) {
        this.width = width;
        this.height = height;
        this.ctx = ctx;
        this.gameState = GameState.getInstance();
        this.gameObjects = new LinkedHashMap<>();
        this.appFont = font;
        this.appColor = appColor;

        initializeGameObjects();
    }

    public void setDimensions(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void refreshGameObjects() {
        initializeGameObjects();
    }

    public void initializeGameObjects() {
        ctx.clearRect(0, 0, width, height);
        gameObjects.clear();

        gameObjects.put("background", new Background(width, height, ctx));
        Pipes pipes = new Pipes(width, height, ctx);
        gameObjects.put("pipes", pipes);
        gameObjects.put("floor", new Floor(width, height, ctx));

        bird = new Bird(width, height, ctx);
        restart = new Restart(width, height, ctx);
        difficulty = new Difficulty(width, height, ctx, appFont, appColor);

        switch (gameState.getDifficulty()) {
            case 0:
                bird.setJumpVel(-4.5f);
                bird.setTerminalVel(6.5f);
                break;
            case 1:
                bird.setJumpVel(-6.5f);
                bird.setTerminalVel(5f);
                break;
            case 2:
                bird.setJumpVel(-3.5f);
                bird.setTerminalVel(7.5f);
                break;
        }


        gameObjects.put("bird", bird);
        gameObjects.put("restart", restart);
        gameObjects.put("score", new Score(width, height, ctx, appFont, appColor, bird));
        gameObjects.put("title", new Title(width, height, ctx));
        gameObjects.put("gameover", new GameOver(width, height, ctx));
        gameObjects.put("difficulty", difficulty);
    }

    public void render() {
        ctx.clearRect(0, 0, width, height);

        for (GameObject gameObject : gameObjects.values()) {
            gameObject.render();
        }
    }

    public void updateGameObjects(long now) {
        for (GameObject gameObject : gameObjects.values()) {
            gameObject.update(now);
        }
    }

    public Bird getBird() {
        return bird;
    }

    public Restart getRestart() {
        return restart;
    }

    public Map<String, GameObject> getGameObjects() {
        return gameObjects;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
