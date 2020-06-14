package lists;

import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * ShadowDefend, a tower defence game.
 */
public class ShadowDefend extends AbstractGame {

    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final String MAP_FILE = "res/levels/1.tmx";
    private static final String TANK_IMAGE = "res/images/tank.png";
    private static final String SUPER_TANK = "res/images/supertank.png";
    private static final String AIR_SUPPORT = "res/images/airsupport.png";
    // Change to suit system specifications. This could be
    // dynamically determined but that is out of scope.
    public static final double FPS = 60;
    // The spawn delay (in seconds) to spawn slicers
    private static double spawnDelay = 5.0;
    private static final int INTIAL_TIMESCALE = 1;
    private static final int MAX_SLICERS = 5;
    // Timescale is made static because it is a universal property of the game and the specification
    // says everything in the game is affected by this
    private static int timescale = INTIAL_TIMESCALE;
    private final TiledMap map;
    private final BuyPanel buyPanel;
    private final StatusPanel statusPanel;
    private final List<Point> polyline;
    private final List<RegularSlicer> slicers;
    private final List<SuperSlicer> superSlicers;
    private final List<MegaSlicer> megaSlicers;
    private final List<ApexSlicer> apexSlicers;
    private final List<Tank> tanks;
    private static List<TankProjectile> tankProjectiles;
    private final WaveFileReader waveFileReader;
    private  Iterator<String[]> currentWaveEvent;
    private static int money;
    private int livesLeft;
    private boolean placing;
    private int waveCount;
    private static double frameCount;
    private boolean waveStarted;
    private boolean waveGoing;
    private boolean spawnEventGoing;
    private int spawnEventCounter;
    private String slicerToSpawn;
    private String towerSelected;
    private int waveFlag = 0;

    /**
     * Creates a new instance of the ShadowDefend game
     */
    public ShadowDefend() throws Exception{
        super(WIDTH, HEIGHT, "ShadowDefend");
        this.map = new TiledMap(MAP_FILE);
        this.buyPanel = new BuyPanel();
        this.statusPanel =  new StatusPanel();
        this.polyline = map.getAllPolylines().get(0);
        this.slicers = new ArrayList<>();
        this.superSlicers = new ArrayList<>();
        this.megaSlicers = new ArrayList<>();
        this.apexSlicers =  new ArrayList<>();
        this.tankProjectiles =  new ArrayList<>();
        this.tanks =  new ArrayList<>();
        this.waveFileReader = new WaveFileReader();
        this.waveStarted = false;
        this.waveGoing = false;
        this.spawnEventGoing = false;
        this.placing = false;
        // Award $500 to player at the start of the game
        this.money = 500;
        // Award 25 lives at start
        this.livesLeft = 25;
        this.waveCount = 0;
        this.frameCount = Integer.MAX_VALUE;
        // Temporary fix for the weird slicer map glitch (might have to do with caching textures)
        // This fix is entirely optional
        new RegularSlicer(polyline);
    }

    /**
     * The entry-point for the game
     *
     * @param args Optional command-line arguments
     */
    public static void main(String[] args) throws Exception {
        new ShadowDefend().run();
    }

    public static int getTimescale() {
        return timescale;
    }

    public static void addMoney(int amount){
        money += amount;
    }

    public static double getFrameCount(){
        return frameCount;
    }

    public static List<TankProjectile> getTankProjectiles(){
        return tankProjectiles;
    }

    /**
     * Increases the timescale
     */
    private void increaseTimescale() {
        timescale++;
    }

    /**
     * Decreases the timescale but doesn't go below the base timescale
     */
    private void decreaseTimescale() {
        if (timescale > INTIAL_TIMESCALE) {
            timescale--;
        }
    }

    private String getGameStatus(){
        if(placing){
            return "Placing";
        }
        else if(waveGoing || !allSlicerListAreEmpty()){
            return "Wave in Progress";
        } else {
            return "Awaiting Start";
        }
    }

    private void updateSlicerList(List<? extends  Slicer> slicerList,Input input){
        for (int i = slicerList.size() - 1; i >= 0; i--) {
            Slicer s = slicerList.get(i);
            s.update(input);
            if (s.isFinished()) {
                slicerList.remove(i);
                if(s.didReachedEnd()){
                    continue;
                }
                else if(s instanceof SuperSlicer){
                    slicers.add(
                            new RegularSlicer(s.getTargetPointIndex(),polyline));
                    slicers.add(
                            new RegularSlicer(s.getTargetPointIndex(),polyline));
                }
                else if(s instanceof MegaSlicer){
                    superSlicers.add(
                            new SuperSlicer(s.getTargetPointIndex(),polyline));
                    superSlicers.add(
                            new SuperSlicer(s.getTargetPointIndex(),polyline));
                }
                else if(s instanceof ApexSlicer){
                    for(int k = 0; k<4; ++k){
                        megaSlicers.add(
                                new MegaSlicer(s.getTargetPointIndex(),polyline));
                    }
                }
            }
        }
    }

    private void updateTankList(List<Tank> tanks, Input input){
        List<List<? extends  Slicer>> slicersList
                = Arrays.asList(slicers, superSlicers, megaSlicers, apexSlicers);
        for(Tank tank : tanks){
            tank.update(input,slicersList);
        }
    }

    private void updateTankProjectiles(Input input){
        for(int i = ShadowDefend.getTankProjectiles().size()-1; i>=0; --i){
            TankProjectile projectile = ShadowDefend.getTankProjectiles().get(i);
            projectile.update(input);
            if(projectile.isFinished()){
                ShadowDefend.getTankProjectiles().remove(projectile);
            }
        }
    }

    private void handleKeyPressEvent(Input input){
        // Handle key presses
        if (input.wasPressed(Keys.S)) {
            waveStarted = true;
        }
        if (input.wasPressed(Keys.L)) {
            increaseTimescale();
        }
        if (input.wasPressed(Keys.K)) {
            decreaseTimescale();
        }
        // Handle mouse button presses
        if (input.wasPressed(MouseButtons.LEFT)){
            if(placing){
                if(canPlaceTower(input)){
                    switch (towerSelected){
                        case "Tank":
                            tanks.add(new Tank(input.getMousePosition()));
                            money -= buyPanel.getTankPrice();
                            placing = false;
                            break;
                        case "Super":
                            // TODO: SuperTank placement
                            break;
                        default:
                            // TODO: AirSupport placement
                            break;
                    }
                }
            } else {
                Point mousePos = input.getMousePosition();
                if(buyPanel.clickedOnTank(mousePos)
                        && buyPanel.isPurchasable("Tank",money)){
                    towerSelected = "Tank";
                    placing = true;
                } else if (buyPanel.clickedOnSuperTank(mousePos)
                        && buyPanel.isPurchasable("SuperTank",money)){
                    towerSelected = "SuperTank";
                    placing = true;
                } else if (buyPanel.clickedOnAirSupport(mousePos) &&
                        buyPanel.isPurchasable("AirSupport",money)){
                    towerSelected = "AirSupport";
                    placing = true;
                }
            }
        }
        if(input.wasPressed(MouseButtons.RIGHT) && placing){
            placing = false;
        }
    }

    void updateSlicers(Input input){
        updateSlicerList(slicers,input);
        updateSlicerList(superSlicers,input);
        updateSlicerList(megaSlicers,input);
        updateSlicerList(apexSlicers,input);
    }

    void updateTowers(Input input){
        updateTankList(tanks,input);
    }

    void updateSprites(Input input){
        updateSlicers(input);
        updateTowers(input);
        updateTankProjectiles(input);
    }

    boolean allSlicerListAreEmpty(){
        return slicers.isEmpty()
                && superSlicers.isEmpty()
                && megaSlicers.isEmpty()
                && apexSlicers.isEmpty();
    }

    void spawnSlicer(String type){
        switch (type){
            case "slicer":
                slicers.add(new RegularSlicer(polyline));
                break;
            case "superslicer":
                superSlicers.add(new SuperSlicer(polyline));
                break;
            case "megaslicer":
                megaSlicers.add(new MegaSlicer(polyline));
                break;
            default:
                apexSlicers.add(new ApexSlicer(polyline));
        }
        frameCount = 0;
        --spawnEventCounter;
        if(spawnEventCounter==0){
            spawnEventGoing = false;
        }
    }

    void executeWaveEvent(String[] dataLine){
        System.out.println("EXECUTING"+Arrays.toString(dataLine));
        if(dataLine[1].equals("spawn")) {
            spawnEventGoing = true;
            slicerToSpawn = dataLine[3];
            spawnEventCounter = Integer.parseInt(dataLine[2]);
            spawnDelay = Double.parseDouble(dataLine[4]) / 1000.0;
            // Spawn one of them immediately
            spawnSlicer(slicerToSpawn);
        } else {
            spawnDelay = Double.parseDouble(dataLine[2])/1000.0;
        }
    }

    boolean isSafeCoordinate(int x, int y){
        return x>=80 && y>=130 && x<999 && y<700;
    }
    
    boolean canPlaceTower(Input input){
        double mouseX = input.getMouseX();
        double mouseY = input.getMouseY();
        return  isSafeCoordinate((int)mouseX,(int)mouseY)
                && !buyPanel.isInsideBuyPanel(input.getMousePosition())
                && !map.hasProperty((int)mouseX,(int)mouseY,"blocked");
    }

    void drawScene(Input input,int currentMoney, int waveCount,
                   double timeScale, String gameStatus, int livesLeft){

        map.draw(0, 0, 0, 0, WIDTH, HEIGHT);
        buyPanel.draw(currentMoney);
        statusPanel.draw(waveCount,timeScale,gameStatus,livesLeft);

        double mouseX = input.getMouseX();
        double mouseY = input.getMouseY();

        if(gameStatus.equals("Placing")
                && canPlaceTower(input)){

            if(towerSelected.equals("Tank")){
                new Image(TANK_IMAGE).draw(mouseX,mouseY);
            }
            else if (towerSelected.equals("SuperTank")){
                new Image(SUPER_TANK).draw(mouseX,mouseY);
            }
            else {
                new Image(AIR_SUPPORT).draw(mouseX,mouseY);
            }
        }
    }

    void updateMoney(){
        money += 150 + (waveCount*100);
    }

    /**
     * Update the state of the game, potentially reading from input
     *
     * @param input The current mouse/keyboard state
     */
    @Override
    protected void update(Input input) {
        // Increase the frame counter by the current timescale
        frameCount += getTimescale();
        // Handle key input events
        handleKeyPressEvent(input);
        // Draw map from the top left of the window
        drawScene(input,money,waveCount,getTimescale(),getGameStatus(),livesLeft);
        // Check if it is time to spawn a new slicer (and we have some left to spawn)
        if (waveStarted && frameCount / FPS >= spawnDelay) {
            if(!spawnEventGoing){
                if(currentWaveEvent!=null && currentWaveEvent.hasNext()){
                    executeWaveEvent(currentWaveEvent.next());
                }
                else if(waveFileReader.hasNext()){
                    // Update money since we survived a wave
                    updateMoney();
                    ++waveCount;
                    waveGoing = true;
                    currentWaveEvent = waveFileReader.next().iterator();
                    String[] dataLine = currentWaveEvent.next();
                    executeWaveEvent(dataLine);
                } else {
                    waveGoing = false;
                }
            } else {
                spawnSlicer(slicerToSpawn);
            }
        }
        // Close game if all slicers have finished traversing the polyline and all waves have finished
        if (!waveFileReader.hasNext() && !waveGoing && allSlicerListAreEmpty()) {
            Window.close();
        }
        // Update all sprites, and remove them if they've finished
        updateSprites(input);

    }
}
