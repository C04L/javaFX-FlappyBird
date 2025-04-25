package GameObjets;

import View.Asset;
import View.Sprite;
import javafx.scene.canvas.GraphicsContext;

public class Bird implements GameObject {
    private int WIDTH = 56;
    private int HEIGHT = 40;
    private double rotation = 0;
    private final double MAX_ROTATION_UP = -30; // pointing up 30 degrees
    private final double MAX_ROTATION_DOWN = 90; // pointing down 90 degrees
    private final double ROTATION_SPEED = 5; // how quickly rotation changes
    private Asset assets[] = {
        new Asset("/images/bird1.png", WIDTH, HEIGHT),
        new Asset("/images/bird2.png", WIDTH, HEIGHT),
        new Asset("/images/bird3.png", WIDTH, HEIGHT)
    };
    private Sprite sprite;
    private int currentAssetIndex = 0;
    private long prevTime = 0;
    private float terminalVel = 5;
    private double screenHeight;
    private GameState gameState = GameState.getInstance();

    public Bird(double screenWidth, double screenHeight, GraphicsContext ctx) {
        this.screenHeight = screenHeight;

        sprite = new Sprite(assets[currentAssetIndex]);
        sprite.setPosX(screenWidth / 2 - WIDTH / 2);
        sprite.setPosY(gameState.isGameEnded() ? screenHeight - 112 - HEIGHT : (screenHeight - 112) / 2);
        sprite.setVel(0, 0);
        sprite.setCtx(ctx);
    }

    public void jumpHandler() {
        sprite.setVelY(-6);
    }

    public void update(long now) {
        if (!gameState.isGameStarted() && !gameState.isGameEnded()) {
            updateBirdHovering();
        } else if (gameState.isGameEnded()) {
            updateBirdFalldown();
        } else if (gameState.isGameStarted()) {
            if (now - prevTime > 90000000) {
                updateSprite();
                prevTime = now;
            }

            Sprite[] activePipes = gameState.getActivePipes();
            if ((sprite.getPosY() + HEIGHT) > (screenHeight - 112) ||
                (activePipes != null && activePipes.length >= 2 &&
                 (sprite.intersects(activePipes[0]) || sprite.intersects(activePipes[1])))) {

                gameState.setGameStarted(false);
                gameState.setGameEnded(true);
            }

            updateBirdPlaying();
        }

        // Add this line to update rotation
        updateRotation();

        sprite.update();
    }

    public void updateBirdHovering() {
        // Simpler hover effect using time-based animation
        double centerPosY = (screenHeight - 112) / 2;
        double amplitude = 10;
        double period = 0.05;

        // Smooth sinusoidal hover
        double offset = amplitude * Math.sin(period * System.currentTimeMillis() / 10000000);
        sprite.setPosY(centerPosY + offset);
        sprite.setVelY(0); // Reset velocity for clean transitions
    }

    public void updateBirdPlaying() {
        double vel = sprite.getVelY();

        if (vel >= terminalVel)
            sprite.setVelY(vel + 0.2);
        else
            sprite.setVelY(vel + 0.2);
    }

    public void updateBirdFalldown() {
        if (sprite.getPosY() + HEIGHT >= screenHeight - 112) {
            sprite.setVel(0, 0);
            sprite.setPosY(screenHeight - 112 - HEIGHT);
        } else {
            updateBirdPlaying();
        }
    }

    public void updateSprite() {
        // Fix array index bounds issue
        currentAssetIndex = (currentAssetIndex + 1) % 3;
        sprite.changeImage(assets[currentAssetIndex]);
    }

    public void render() {
        GraphicsContext ctx = getGraphicsContext();

        // Draw the bird image, centered at the rotation point
        ctx.drawImage(
            sprite.getImage(),
            (double) -WIDTH /2,  // Adjusted X to center the image
            (double) -HEIGHT /2, // Adjusted Y to center the image
            WIDTH,
            HEIGHT
        );

        // Restore the context to its original state
        ctx.restore();
    }

    private GraphicsContext getGraphicsContext() {
        GraphicsContext ctx = sprite.getContext();

        // Don't use sprite.render() - we'll handle drawing ourselves

        // Save current context state
        ctx.save();

        // Calculate the center point of the bird
        double centerX = sprite.getPosX() + (double) WIDTH /2;
        double centerY = sprite.getPosY() + (double) HEIGHT /2;

        // Translate to the center of the bird
        ctx.translate(centerX, centerY);

        // Apply rotation
        ctx.rotate(Math.toRadians(rotation));
        return ctx;
    }

    public double getPosX() {
        return sprite.getPosX();
    }

    public double getPosY() {
        return sprite.getPosY();
    }

    private void updateRotation() {
        double velY = sprite.getVelY();

        if (velY < 0) {
            // When the bird jumps (negative velocity), quickly rotate upward
            rotation = MAX_ROTATION_UP;
        } else {
            // When falling, gradually rotate downward based on fall speed
            double targetRotation = Math.min(MAX_ROTATION_DOWN, velY * 8);

            // Gradually approach the target rotation
            if (rotation < targetRotation) {
                rotation += ROTATION_SPEED;
                if (rotation > targetRotation) rotation = targetRotation;
            }
        }
    }

}