package lists;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;

public class BuyPanel {
    private static final String BACKGROUND_IMAGE = "res/images/buypanel.png";
    private static final String TANK_IMAGE = "res/images/tank.png";
    private static final String SUPER_TANK = "res/images/supertank.png";
    private static final String AIR_SUPPORT = "res/images/airsupport.png";
    private static final String DEJA_VU_SANS_BOLD = "res/fonts/DejaVuSans-Bold.ttf";
    private Font globalFont;
    private final Image background;
    private final Image tank;
    private final Image superTank;
    private final Image airSupport;

    public BuyPanel() {
        this.globalFont = new Font(DEJA_VU_SANS_BOLD,22);
        this.background = new Image(BACKGROUND_IMAGE);
        this.tank = new Image(TANK_IMAGE);
        this.superTank = new Image(SUPER_TANK);
        this.airSupport = new Image(AIR_SUPPORT);
    }

    public void draw(){
        drawBackground();
        drawTank();
        drawSuperTank();
        drawAirSupport();
        drawKeyBindings();
        drawCurrentMoney();
    }

    private void drawBackground(){
        background.draw(0.0,0.0,new DrawOptions().setScale(2.0,2.0));
    }

    private void drawTank(){
        tank.draw(64.0,45.0);
        globalFont.drawString("$250",35.0,90.0,
                new DrawOptions().setBlendColour(Colour.GREEN));
    }

    private void drawSuperTank(){
        superTank.draw(184.0,45.0);
        globalFont.drawString("$600",155.0,90.0,
                new DrawOptions().setBlendColour(Colour.GREEN));
    }

    private void drawAirSupport(){
        airSupport.draw(304.0,45.0);
        globalFont.drawString("$500",275.0,90.0,
                new DrawOptions().setBlendColour(Colour.GREEN));
    }

    private void drawKeyBindings(){
        String data = "Key Bindings: \n\n"
                + "S - Start Wave \n"
                + "L - Increase Timescale \n"
                + "K - Decrease Timescale";
        new Font(DEJA_VU_SANS_BOLD,15)
                .drawString(data,445.0,25.0,
                        new DrawOptions().setBlendColour(Colour.WHITE));
    }

    private void drawCurrentMoney(){
        String data = "$2,500";
        new Font(DEJA_VU_SANS_BOLD,50)
                .drawString(data,750,70,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

}
