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

    /**
     * Draw statusPanel
     * @param waveCount Number of current wave
     * @param timeScale Current timescale of the game
     * @param gameStatus Current status of game
     * @param livesLeft lives left in game
     */
    public void draw(int waveCount, double timeScale, String gameStatus, int livesLeft){
        drawBackground();
        drawWaveCount(waveCount);
        drawTimeScale(timeScale);
        drawGameStatus(gameStatus);
        drawLivesCount(livesLeft);
    }

    /**
     * Render background for status panel
     */
    private void drawBackground(){
        background.drawFromTopLeft(0.0,758.0,new DrawOptions().setScale(2.0,3.0));
    }

    /**
     *
     * @param waveCount Current wave number
     */
    private void drawWaveCount(int waveCount){
        globalFont.drawString("Wave: "+waveCount,18.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

    /**
     *
     * @param timeScale Current game timescale
     */
    private void drawTimeScale(double timeScale){
        Colour color = timeScale>1.0?Colour.GREEN:Colour.WHITE;
        globalFont.drawString("Time Scale: "+timeScale,138.0,759.0,
                new DrawOptions().setBlendColour(color));
    }

    /**
     *
     * @param gameState Current status of the game
     */
    private void drawGameStatus(String gameState){
        globalFont.drawString("Status: "+gameState,358.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

    /**
     *
     * @param livesLeft lives left in game
     */
    private void drawLivesCount(int livesLeft){
        globalFont.drawString("Lives: "+livesLeft,899.0,759.0,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }
}
