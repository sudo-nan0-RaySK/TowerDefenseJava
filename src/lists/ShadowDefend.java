package lists;

import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
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
    private final List<Point> polyline;
    private final List<RegularSlicer> slicers;
    private final List<SuperSlicer> superSlicers;
    private final List<MegaSlicer> megaSlicers;
    private final List<ApexSlicer> apexSlicers;
    private final WaveFileReader waveFileReader;
    private  Iterator<String[]> currentWaveEvent;
    private double frameCount;
    private boolean waveStarted;
    private boolean waveGoing;
    private boolean spawnEventGoing;
    private int spawnEventCounter;
    private String slicerToSpawn;
    private int waveFlag = 0;

    /**
     * Creates a new instance of the ShadowDefend game
     */
    public ShadowDefend() throws Exception{
        super(WIDTH, HEIGHT, "ShadowDefend");
        this.map = new TiledMap(MAP_FILE);
        this.polyline = map.getAllPolylines().get(0);
        this.slicers = new ArrayList<>();
        this.superSlicers = new ArrayList<>();
        this.megaSlicers = new ArrayList<>();
        this.apexSlicers =  new ArrayList<>();
        this.waveFileReader = new WaveFileReader();
        this.waveStarted = false;
        this.waveGoing = false;
        this.spawnEventGoing = false;
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

    private void updateSlicerList(List<? extends  Slicer> slicerList,Input input){
        for (int i = slicerList.size() - 1; i >= 0; i--) {
            Slicer s = slicerList.get(i);
            s.update(input);
            if (s.isFinished()) {
                slicerList.remove(i);
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
    }

    void updateSlicers(Input input){
        updateSlicerList(slicers,input);
        updateSlicerList(superSlicers,input);
        updateSlicerList(megaSlicers,input);
        updateSlicerList(apexSlicers,input);
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

        // Draw map from the top left of the window
        map.draw(0, 0, 0, 0, WIDTH, HEIGHT);

        // Handle key input events
        handleKeyPressEvent(input);

        // Check if it is time to spawn a new slicer (and we have some left to spawn)
        if (waveStarted && frameCount / FPS >= spawnDelay) {
            // TODO: Handle SPAWN and DELAY events properly
            if(!spawnEventGoing){
                if(waveFileReader.hasNext()){
                    currentWaveEvent = waveFileReader.next().iterator();
                    String[] dataLine = currentWaveEvent.next();
                    System.out.println("EXECUTING"+Arrays.toString(dataLine));
                    if(dataLine[1].equals("spawn")){
                        spawnEventGoing = true;
                        slicerToSpawn = dataLine[3];
                        spawnEventCounter = Integer.parseInt(dataLine[2]);
                        spawnDelay = Double.parseDouble(dataLine[4])/1000.0;
                        // Spawn one of them immediately
                        spawnSlicer(slicerToSpawn);
                        frameCount = 0;
                        --spawnEventCounter;
                        if(spawnEventCounter==0){
                            spawnEventGoing = false;
                        }
                    } else {
                        spawnDelay = Double.parseDouble(dataLine[2])/1000.0;
                    }
                } else {
                    waveGoing = false;
                }
            } else {
                spawnSlicer(slicerToSpawn);
                frameCount = 0;
                --spawnEventCounter;
                if(spawnEventCounter==0){
                    spawnEventGoing = false;
                }
            }
        }
        // Close game if all slicers have finished traversing the polyline and all waves have finished
        if (!waveFileReader.hasNext() && !waveGoing && allSlicerListAreEmpty()) {
            Window.close();
        }
        // Update all sprites, and remove them if they've finished
        updateSlicers(input);
    }
}
