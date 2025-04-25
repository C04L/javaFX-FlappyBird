package GameObjects;

import View.Asset;
import View.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.text.Font;

public class Score implements GameObject {
    private int WIDTH = 108;
    private int HEIGHT = 146;
    private Asset asset = new Asset("/Assets/images/score.png", WIDTH, HEIGHT);
    private Sprite sprite;
    private GraphicsContext ctx;
    private Font appFont;
    private Color appColor;

    private int posX = 10;
    private int posY = 50;
    private int tablePosX, tablePosY;

    private double prevActivePipePosX = 0;
    private double prevActivePipePosY = 0;
    private boolean scoreCounted = false;
    private GameState gameState = GameState.getInstance();
    private Bird bird;
    private double screenWidth;

    public Score(double screenWidth, double screenHeight, GraphicsContext ctx, Font appFont, Color appColor, Bird bird) {
        this.screenWidth = screenWidth;
        this.bird = bird;
        this.appFont = appFont;
        this.appColor = appColor;

        sprite = new Sprite(asset);
        tablePosX = (int) screenWidth / 2 - WIDTH / 2;
        tablePosY = ((int) screenHeight - 112) / 2 - HEIGHT / 2;
        sprite.setPosX(tablePosX);
        sprite.setPosY(tablePosY);
        sprite.setVel(0, 0);
        sprite.setCtx(ctx);

        posX = (int) screenWidth / 2 - 10;
        posY = 80;

        this.ctx = ctx;
        ctx.setFont(appFont);
        ctx.setStroke(appColor);

        // Initialize pipe position tracking
        Sprite[] activePipes = gameState.getActivePipes();
        if (activePipes != null && activePipes.length > 0) {
            prevActivePipePosX = activePipes[0].getPosX();
            prevActivePipePosY = activePipes[0].getPosY();
        }
    }

    public void update(long now) {
        Sprite[] activePipes = gameState.getActivePipes();
        if (gameState.isGameStarted() && !gameState.isGameEnded() && activePipes != null && activePipes.length > 0) {
            // Check if the bird has passed through the middle of the pipes
            double birdCenterX = (bird != null) ? bird.getPosX() + 28 : 0;

            double pipeX = activePipes[0].getPosX();
            double pipeCenterX = pipeX + 31; // Half of pipe width

            // Score when bird passes the center of the pipe
            if (pipeX < prevActivePipePosX && !scoreCounted && birdCenterX > pipeCenterX) {
                gameState.incrementScore();
                scoreCounted = true;
            }

            // Reset scoring flag when a new pipe becomes active
            if (activePipes[0].getPosY() != prevActivePipePosY) {
                scoreCounted = false;
                prevActivePipePosY = activePipes[0].getPosY();
            }

            prevActivePipePosX = pipeX;
        }
    }

    public void render() {
        if (gameState.isGameEnded()) {
            sprite.render();
            ctx.setFill(appColor);
            ctx.setFont(new Font("04b_19", 32));
            ctx.fillText(gameState.getScore() + "", posX + 2, tablePosY + 70);
            ctx.fillText(gameState.getHighscore() + "", posX + 2, tablePosY + 126);
        }

        if (gameState.isGameStarted() && !gameState.isGameEnded()) {
            ctx.setFill(Color.WHITE);
            ctx.fillText(gameState.getScore() + "", posX, posY);
            ctx.strokeText(gameState.getScore() + "", posX, posY);
        }
    }
}