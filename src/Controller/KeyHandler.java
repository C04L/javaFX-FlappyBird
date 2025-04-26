package Controller;

import GameObjects.Bird;
import GameObjects.Difficulty;
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
    private Difficulty difficulty;

    public KeyHandler(Renderer renderer, double width, double height) {
        this.renderer = renderer;
        this.gameState = GameState.getInstance();
        this.bird = renderer.getBird();
        this.restart = renderer.getRestart();
        this.difficulty = renderer.getDifficulty();
    }

    public void handleKeyInput(KeyEvent e) {
        if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.UP) {
            handleInput(-1, -1);
        }

        if (!gameState.isGameStarted()) {
            if (e.getCode() == KeyCode.LEFT) {
                difficulty.decreaseDifficulty();
            } else if (e.getCode() == KeyCode.RIGHT) {
                difficulty.increaseDifficulty();
            }
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
