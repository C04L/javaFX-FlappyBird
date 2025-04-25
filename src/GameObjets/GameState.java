package GameObjets;

import View.Sprite;

// Place this in the same package as GameObjects
public class GameState {
    private static GameState instance;

    // Game state
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private int score = 0;
    private int highscore = 0;
    private Sprite[] activePipes;

    // Private constructor for singleton
    private GameState() {}

    // Get the singleton instance
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    // Getters and setters
    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore() {
        this.score++;
        if (this.score > this.highscore) {
            this.highscore = this.score;
        }
    }

    public int getHighscore() {
        return highscore;
    }

    public void setActivePipes(Sprite[] pipes) {
        this.activePipes = pipes;
    }

    public Sprite[] getActivePipes() {
        return activePipes;
    }

    // Reset game state
    public void resetGame() {
        gameStarted = false;
        gameEnded = false;
        score = 0;
    }
}
