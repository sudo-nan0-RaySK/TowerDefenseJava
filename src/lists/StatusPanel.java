package lists;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;

public class StatusPanel {
    private static final String BACKGROUND_IMAGE = "res/images/statuspanel.png";
    private final Image background;
    private static final String DEJA_VU_SANS_BOLD = "res/fonts/DejaVuSans-Bold.ttf";
    private Font globalFont;

    public StatusPanel(){
        this.background = new Image(BACKGROUND_IMAGE);
        globalFont = new Font(DEJA_VU_SANS_BOLD,18);
    }

    public void draw(){
        drawBackground();
        drawWaveCount();
        drawTimeScale();
        drawGameStatus();
        drawLivesCount();
    }

    private void drawBackground(){
        background.drawFromTopLeft(0.0,758.0,new DrawOptions().setScale(2.0,3.0));
    }

    private void drawWaveCount(){
        globalFont.drawString("Wave: 10",18.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

    private void drawTimeScale(){
        globalFont.drawString("Time Scale: 1.0",138.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

    private void drawGameStatus(){
        globalFont.drawString("Status: Wave in progress",358.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

    private void drawLivesCount(){
        globalFont.drawString("Lives: 25",899.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }
}
