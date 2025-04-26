package GameObjects;

import java.util.ArrayList;

import View.Asset;
import View.Sprite;
import javafx.scene.canvas.GraphicsContext;


public class Background implements GameObject{
    private final int WIDTH = 288;
    private final int HEIGHT = 512;
    private Asset asset = new Asset("/images/background.png", WIDTH, HEIGHT);
    private final ArrayList<Sprite> sprites = new ArrayList<>();

    public Background(double screenWidth, double screenHeight, GraphicsContext ctx) {
        int backgroundWidth = 0;

//        Đảm bảo cái background sẽ luôn đầy màn hình bằng cách lặp lại cái ảnh

        do {
            Sprite background = new Sprite(asset);

            if ((screenHeight - 112) < HEIGHT)
                background.resizeImage(WIDTH, HEIGHT);
            else
                background.resizeImage(WIDTH, screenHeight - 112 );
            
            if (screenHeight > HEIGHT)
                background.setPos(backgroundWidth, 0);
            else
                background.setPos(backgroundWidth, screenHeight - HEIGHT);

            background.setVel(0, 0);
            background.setCtx(ctx);

            sprites.add(background);
            backgroundWidth += WIDTH;
        } while (backgroundWidth < (screenWidth + WIDTH));
    }

    public void update(long now) {
    }

    public void render() {
        for (Sprite background : sprites)
            background.render();
    }
}