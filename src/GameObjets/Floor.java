package GameObjets;

import View.Asset;
import View.Sprite;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Floor implements GameObject {
    private int WIDTH = 336;
    private int HEIGHT = 112;
    private Asset asset = new Asset("/images/floor.png", WIDTH, HEIGHT);
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private GameState gameState = GameState.getInstance();

    public Floor(double screenWidth, double screenHeight, GraphicsContext ctx) {
        int floorWidth = 0;
        do {
            Sprite floor = new Sprite(asset);
            floor.setPos(floorWidth, screenHeight - HEIGHT);
            floor.setVel(-2.5, 0);
            floor.setCtx(ctx);

            sprites.add(floor);
            floorWidth += WIDTH;
        } while (floorWidth < (screenWidth + WIDTH));
    }

    public void update(long now) {
        if (gameState.isGameStarted()) {
            for (Sprite floor : sprites)
                floor.update();

            if (sprites.get(0).getPosX() < -WIDTH) {
                Sprite firstFloor = sprites.get(0);

                sprites.remove(0);
                firstFloor.setPosX(sprites.get(sprites.size() - 1).getPosX() + WIDTH);
                sprites.add(firstFloor);
            }
        }
    }

    public void render() {
        for (Sprite floor : sprites)
            floor.render();
    }
}