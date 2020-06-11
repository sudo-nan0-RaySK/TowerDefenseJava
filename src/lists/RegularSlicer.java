package lists;

import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.List;

public class RegularSlicer extends Slicer {
    private static final String IMAGE_FILE = "res/images/slicer.png";
    private static final double SPEED = 1.0;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param polyLine    The list of points on polyLine
     */

    public RegularSlicer(List<Point> polyLine){
        super(polyLine,IMAGE_FILE,SPEED);
    }
}
