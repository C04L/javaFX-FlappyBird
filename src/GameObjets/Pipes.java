package GameObjets;

import java.util.ArrayList;

import View.Asset;
import View.Sprite;
import javafx.scene.canvas.GraphicsContext;
import java.util.concurrent.ThreadLocalRandom;

public class Pipes implements GameObject {
    private final int WIDTH = 62;
    private final int HEIGHT = 2000;
    private final Asset assetUp = new Asset("/images/up_pipe.png", WIDTH, HEIGHT);
    private final Asset assetDown = new Asset("/images/down_pipe.png", WIDTH, HEIGHT);
    private final ArrayList<Sprite> spritesUp = new ArrayList<>();
    private final ArrayList<Sprite> spritesDown = new ArrayList<>();
    private final GameState gameState = GameState.getInstance();

    private final double screenHeight;
    private final double screenWidth;
    private final GraphicsContext ctx;

    public Pipes(double screenWidth, double screenHeight, GraphicsContext ctx) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.ctx = ctx;

        Sprite[] pipes = createPipes(screenWidth + 200);
        gameState.setActivePipes(pipes);
        spritesUp.add(pipes[0]);
        spritesDown.add(pipes[1]);
    }

    public void update(long now) {
        if (gameState.isGameStarted()) {
            for (int i = 0; i < spritesUp.size(); i++) {
                spritesUp.get(i).update();
                spritesDown.get(i).update();

                Sprite[] activePipes = gameState.getActivePipes();
                if (activePipes != null && activePipes[0].getPosX() + WIDTH < screenWidth / 2 - 56) {
                    gameState.setActivePipes(new Sprite[] { spritesUp.get(i), spritesDown.get(i) });
                }
            }
        }
    }

    public void render() {
        for (Sprite pipe : spritesUp)
            pipe.render();

        for (Sprite pipe : spritesDown)
            pipe.render();

        if (spritesUp.getLast().getPosX() < screenWidth) {
            Sprite[] pipes = createPipes(spritesUp.getLast().getPosX() + 260);
      
            spritesUp.add(pipes[0]);
            spritesDown.add(pipes[1]);
        }
    
        if (spritesUp.getFirst().getPosX() < -WIDTH) {
            spritesUp.removeFirst();
            spritesDown.removeFirst();
        }
    }

    private Sprite[] createPipes(double posX) {
        double usableHeight = screenHeight - 364;
        int randomNum = ThreadLocalRandom.current().nextInt(0, (int) usableHeight + 1);

        Sprite pipeUp = new Sprite(assetUp);
        pipeUp.setPos(posX, 206 + randomNum);
        pipeUp.setVel(-1.5, 0);
        pipeUp.setCtx(ctx);

        Sprite pipeDown = new Sprite(assetDown);
        pipeDown.setPos(posX, -1954 + randomNum);
        pipeDown.setVel(-1.5, 0);
        pipeDown.setCtx(ctx);

        return new Sprite[] { pipeUp, pipeDown };
    }
}