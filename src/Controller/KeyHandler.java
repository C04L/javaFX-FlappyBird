package Controller;

import GameObjects.Bird;
import GameObjects.GameState;
import GameObjects.Restart;
import View.Renderer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHandler {
    private Renderer renderer;
    private GameState gameState;
    private Bird bird;
    private Restart restart;
    private double width;
    private double height;

    public KeyHandler(Renderer renderer, double width, double height) {
        this.renderer = renderer;
        this.width = width;
        this.height = height;
        this.gameState = GameState.getInstance();
        this.bird = renderer.getBird();
        this.restart = renderer.getRestart();
    }

    public void handleKeyInput(KeyEvent e) {
        if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.UP) {
            handleInput(-1, -1);
        }
    }

    public void handleMouseInput(double posX, double posY) {
        handleInput(posX, posY);
    }

    private void handleInput(double posX, double posY) {
        if (!gameState.isGameEnded()) {
            bird.jumpHandler();
            gameState.setGameStarted(true);
        } else if (posX == -1 && posY == -1 || restart.checkClick(posX, posY)) {
            gameState.resetGame();
            renderer.refreshGameObjects();

            /*
            * Khi end game thì chim sẽ bị xóa -> cần gọi lại instance của chim mới
            * */
            bird = renderer.getBird();
            restart = renderer.getRestart();
        }
    }

    public void updateGame(long now) {
        renderer.updateGameObjects(now);
    }
}
