package lists;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class BuyPanel {
    private static final String BACKGROUND_IMAGE = "res/images/buypanel.png";
    private static final String TANK_IMAGE = "res/images/tank.png";
    private static final String SUPER_TANK = "res/images/supertank.png";
    private static final String AIR_SUPPORT = "res/images/airsupport.png";
    private static final String DEJA_VU_SANS_BOLD = "res/fonts/DejaVuSans-Bold.ttf";
    private final Font globalFont;
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

    /**
     * Draws buyPanel
     * @param currentMoney Player's current in game money
     */
    public void draw(int currentMoney){
        drawBackground();
        drawTank(currentMoney);
        drawSuperTank(currentMoney);
        drawAirSupport(currentMoney);
        drawKeyBindings();
        drawCurrentMoney(currentMoney);
    }

    /**
     *
     * @param p Point to be checked
     * @return if point is inside buyPanel or not
     */
    public boolean isInsideBuyPanel(Point p){
        return background.getBoundingBox().intersects(p);
    }

    /**
     *
     * @param p Point to be checked
     * @return if tank was clicked
     */
    public boolean clickedOnTank(Point p){
        return tank.getBoundingBoxAt(new Point(35.0,90.0)).intersects(p);
    }

    /**
     *
     * @param p Point to be checked
     * @return if superTank was clicked
     */
    public boolean clickedOnSuperTank(Point p){
        return superTank.getBoundingBoxAt(new Point(155.0,90.0)).intersects(p);
    }

    /**
     *
     * @param p Point to be checked
     * @return if airSupport was clicked
     */
    public boolean clickedOnAirSupport(Point p){
        return airSupport.getBoundingBoxAt(new Point(275.0,90.0)).intersects(p);
    }

    /**
     * Draw background for buyPanel
     */
    private void drawBackground(){
        background.draw(0.0,0.0,new DrawOptions().setScale(2.0,2.0));
    }

    /**
     * Render tank image on buyPanel
     * @param currentMoney Player's current in game money
     */
    private void drawTank(int currentMoney){
        Colour textColor = currentMoney>=250?Colour.GREEN:Colour.RED;
        tank.draw(64.0,45.0);
        globalFont.drawString("$"+getTankPrice(),35.0,90.0,
                new DrawOptions().setBlendColour(textColor));
    }

    /**
     * Render superTank image on buyPanel
     * @param currentMoney Player's current in game money
     */
    private void drawSuperTank(int currentMoney){
        Colour textColor = currentMoney>=600?Colour.GREEN:Colour.RED;
        superTank.draw(184.0,45.0);
        globalFont.drawString("$"+getSuperTankPrice(),155.0,90.0,
                new DrawOptions().setBlendColour(textColor));
    }

    /**
     * Render airSupport image on buyPanel
     * @param currentMoney Player's current in game money
     */
    private void drawAirSupport(int currentMoney){
        Colour textColor = currentMoney>=500?Colour.GREEN:Colour.RED;
        airSupport.draw(304.0,45.0);
        globalFont.drawString("$"+getAirSupportPrice(),275.0,90.0,
                new DrawOptions().setBlendColour(textColor));
    }

    /**
     * Render key bindings
     */
    private void drawKeyBindings(){
        String data = "Key Bindings: \n\n"
                + "S - Start Wave \n"
                + "L - Increase Timescale \n"
                + "K - Decrease Timescale";
        new Font(DEJA_VU_SANS_BOLD,15)
                .drawString(data,445.0,25.0,
                        new DrawOptions().setBlendColour(Colour.WHITE));
    }
    /**
     * Render currentMoney
     * @param currentMoney Player's current in game money
     */
    private void drawCurrentMoney(int currentMoney){
        new Font(DEJA_VU_SANS_BOLD,50)
                .drawString("$"+currentMoney,750,70,
                new DrawOptions().setBlendColour(Colour.WHITE));
    }

    /**
     * Check if tower is purchasable
     * @param towerType Tower to be purchased
     * @param money currentMoney
     * @return if tower is purchasable
     */
    public boolean isPurchasable(String towerType, int money){
        if(towerType.equals("Tank")){
            return money>=getTankPrice();
        }
        else if(towerType.equals("SuperTank")){
            return money>=getSuperTankPrice();
        }
        else{
            return money>=getAirSupportPrice();
        }
    }

    /**
     *
     * @return price of tank
     */
    public int getTankPrice(){
        return 250;
    }

    /**
     *
     * @return price of superTank
     */
    public int getSuperTankPrice(){
        return 600;
    }

    /**
     *
     * @return price of airSupport
     */
    public int getAirSupportPrice(){
        return 500;
    }

}
