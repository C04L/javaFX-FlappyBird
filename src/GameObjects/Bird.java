package GameObjects;

import View.Asset;
import View.Sprite;
import javafx.scene.canvas.GraphicsContext;

public class Bird implements GameObject {
    private int WIDTH = 56;
    private int HEIGHT = 40;
    private double rotation = 0;
    private final double MAX_ROTATION_UP = -30;
    private final double MAX_ROTATION_DOWN = 55;
    private final double ROTATION_SPEED = 1;
    private Asset assets[] = {
        new Asset("/Assets/images/bird1.png", WIDTH, HEIGHT),
        new Asset("/Assets/images/bird2.png", WIDTH, HEIGHT),
        new Asset("/Assets/images/bird3.png", WIDTH, HEIGHT)
    };
    private Sprite sprite;
    private int currentAssetIndex = 0;
    private long prevTime = 0;
    private float terminalVel = 5f; //Tốc độ rơi tối đa của con bird
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

        // Update rotation based on velocity
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

        // Reset rotation during hovering
        rotation = 0;
    }

    public void updateBirdPlaying() {
        double vel = sprite.getVelY();

        if (vel < terminalVel)
            sprite.setVelY(vel + 0.2);
        else
            sprite.setVelY(terminalVel);
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
        GraphicsContext ctx = sprite.getContext();

        // Save the current state of the graphics context
        ctx.save();

        // Calculate the center point of the bird for rotation
        double centerX = sprite.getPosX() + WIDTH / 2;
        double centerY = sprite.getPosY() + HEIGHT / 2;

        // Move to the center of the bird
        ctx.translate(centerX, centerY);

        // Apply rotation
        ctx.rotate(rotation);

        // Ném con chim ra giữa màn hình
        ctx.drawImage(
            sprite.getImage(),
            -WIDTH / 2,
            -HEIGHT / 2,
            WIDTH,
            HEIGHT
        );

        ctx.restore();
    }

    public double getPosX() {
        return sprite.getPosX();
    }

    public double getPosY() {
        return sprite.getPosY();
    }

    private void updateRotation() {
        double velY = sprite.getVelY();

        if (!gameState.isGameStarted() && !gameState.isGameEnded()) {
            rotation = 0;
            return;
        }

        if (velY < 0) {
            rotation = MAX_ROTATION_UP;
        } else {
            // When
            double targetRotation = MAX_ROTATION_DOWN;

            // Gradually approach the target rotation
            if (rotation < targetRotation) {
                rotation += ROTATION_SPEED;
                if (rotation > targetRotation) rotation = targetRotation;
            }
        }
    }
}