package GameObjects;

import Controller.SoundController;
import View.Renderer;
import View.Sprite;

public class GameState {
    private static GameState instance;
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private int score = 0;
    private int highscore = 0;
    private Sprite[] activePipes;
    private int difficulty = 0;
    private SoundController sound = SoundController.getInstance();

    private GameState() {}

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        if (gameStarted) {
            sound.playBackgroundMusic();
        }
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
        if (gameEnded) {
            sound.stopBackgroundMusic();
        }
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

     public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        Renderer renderer = Renderer.getInstance();
        System.out.println("Difficulty set to: " + difficulty);
        this.difficulty = difficulty;
        renderer.updateGameObjects(0);
    }


    public void resetGame() {
        gameStarted = false;
        gameEnded = false;
        score = 0;
    }
}
