package lists;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;

public class StatusPanel {
    private static final String BACKGROUND_IMAGE = "res/images/statuspanel.png";
    private final Image background;
    private static final String DEJA_VU_SANS_BOLD = "res/fonts/DejaVuSans-Bold.ttf";
    private final Font globalFont;

    public StatusPanel(){
        this.background = new Image(BACKGROUND_IMAGE);
        globalFont = new Font(DEJA_VU_SANS_BOLD,18);
    }

    public void draw(int waveCount, double timeScale, String gameStatus, int livesLeft){
        drawBackground();
        drawWaveCount(waveCount);
        drawTimeScale(timeScale);
        drawGameStatus(gameStatus);
        drawLivesCount(livesLeft);
    }

    public boolean isInsideStatusPanel(Point p){
        return background.getBoundingBox().intersects(p);
    }

    private void drawBackground(){
        background.drawFromTopLeft(0.0,758.0,new DrawOptions().setScale(2.0,3.0));
    }

    private void drawWaveCount(int waveCount){
        globalFont.drawString("Wave: "+waveCount,18.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

    private void drawTimeScale(double timeScale){
        globalFont.drawString("Time Scale: "+timeScale,138.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

    private void drawGameStatus(String gameState){
        globalFont.drawString("Status: "+gameState,358.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

    private void drawLivesCount(int livesLeft){
        globalFont.drawString("Lives: "+livesLeft,899.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }
}
