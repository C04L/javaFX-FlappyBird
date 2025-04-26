package GameObjects;

import View.Asset;
import View.Sprite;
import Controller.SoundController;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class SoundButton implements GameObject {
    private final int WIDTH = 32;
    private final int HEIGHT = 32;
    private Asset soundOnAsset = new Asset("/Assets/images/soundOn.png", WIDTH, HEIGHT);
    private Asset soundOffAsset = new Asset("/Assets/images/soundOff.png", WIDTH, HEIGHT);
    private Sprite sprite;
    private SoundController soundController = SoundController.getInstance();
    private double posX, posY;

    public SoundButton(double screenWidth, double screenHeight, GraphicsContext ctx) {

        // Position in top right with some margin
        posX = screenWidth - WIDTH - 10;
        posY = 10;

        // Initialize sprite with the correct image based on current sound state
        sprite = new Sprite(soundController.isSoundEnabled() ? soundOnAsset : soundOffAsset);
        sprite.setPos(posX, posY);
        sprite.setVel(0, 0);
        sprite.setCtx(ctx);
    }

    public boolean checkClick(double posX, double posY) {
        return sprite.intersects(new Rectangle2D(posX, posY, 1, 1));
    }

    public void toggleSound() {
        soundController.toggleSound();
        // Update the button image based on new sound state
        sprite.changeImage(soundController.isSoundEnabled() ? soundOnAsset : soundOffAsset);
    }

    public void repositionButton(double screenWidth) {
        posX = screenWidth - WIDTH - 10;
        sprite.setPosX(posX);
    }

    @Override
    public void update(long now) {
        // No animation needed for this button
    }

    @Override
    public void render() {
        sprite.render();
    }
}