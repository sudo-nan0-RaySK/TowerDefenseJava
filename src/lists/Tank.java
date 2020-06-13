package lists;

import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tank extends Sprite {

    private static final String TANK_IMAGE = "res/images/tank.png";
    private final double COOL_DOWN_PERIOD = 1.0;
    private boolean coolDown;
    private final int DAMAGE = 1;
    private final double EFFECT_RADIUS = 100;
    private Slicer lockedTarget;

    public  Tank(Point p){
        super(p,TANK_IMAGE);
        this.lockedTarget = null;
        this.coolDown = false;
    }

    /** Equation for the circle
     *  (x-x')^2 + (y-y')^2 <= r^2
     */
    private boolean isInRange(Point p){
        Point spriteCenter = this.getCenter();
        return (Math.pow(spriteCenter.x-p.x,2.0))
        + (Math.pow(spriteCenter.y-p.y,2.0))
        <= Math.pow(EFFECT_RADIUS,2.0);
    }

    public boolean isInCoolDown(){
        return coolDown;
    }

    public void seekTargets(List<List<? extends Slicer>> slicerLists){
        List<Slicer> slicersInRange = new ArrayList<>();
        for(List<? extends Slicer> slicerList : slicerLists){
            for(Slicer s : slicerList){
                if(isInRange(s.getCenter())){
                    slicersInRange.add(s);
                }
            }
        }
        if(slicersInRange.isEmpty()){
            return;
        }
        int randomIndex = new Random().nextInt(slicersInRange.size());
        lockedTarget = slicersInRange.get(randomIndex);
        System.out.println(lockedTarget);
    }

    public void update(Input input,List<List<? extends Slicer>> slicerLists){
        if(lockedTarget==null || !isInRange(lockedTarget.getCenter())){
            seekTargets(slicerLists);
        }
        super.update(input);
    }
}
