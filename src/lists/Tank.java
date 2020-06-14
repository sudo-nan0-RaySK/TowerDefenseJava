package lists;

import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tank extends Sprite {

    private static final String TANK_IMAGE = "res/images/tank.png";
    private static final String TANK_PROJECTILE_IMAGE = "res/images/tank_projectile.png";
    private final long COOL_DOWN_PERIOD = 1000;
    private final int DAMAGE = 1;
    private final double SPEED = 1.0;
    private final double EFFECT_RADIUS = 100;
    private long lastFire;
    private Slicer lockedTarget;

    /**
     * @param p Point to spawn tank on
     */
    public  Tank(Point p){
        super(p,TANK_IMAGE);
        this.lockedTarget = null;
        this.lastFire = Long.MAX_VALUE;
    }

    /** Equation for the circle
     *  (x-x')^2 + (y-y')^2 <= r^2
     * @param p Point to be checked
     * @return If point lies in range
     */
    private boolean isInRange(Point p){
        Point spriteCenter = this.getCenter();
        return (Math.pow(spriteCenter.x-p.x,2.0))
        + (Math.pow(spriteCenter.y-p.y,2.0))
        <= Math.pow(EFFECT_RADIUS,2.0);
    }

    /**
     * Lock a target
     * @param slicerLists List of all slicers
     */
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
    }

    /**
     * Update tank
     * @param input User input
     * @param slicerLists List of all slicers
     */
    public void update(Input input,List<List<? extends Slicer>> slicerLists){
        if(lockedTarget==null || lockedTarget.isFinished() || !isInRange(lockedTarget.getCenter())){
            seekTargets(slicerLists);
            if(lockedTarget!=null && isInRange(lockedTarget.getCenter())){
                Point current = this.getCenter();
                Point target = lockedTarget.getCenter();
                super.setAngle(Math.atan2(target.y-current.y,target.x-current.x));
                // Launch the projectile
                if(lastFire==Long.MAX_VALUE || System.currentTimeMillis()-lastFire >= COOL_DOWN_PERIOD){
                    super.setAngle(Math.atan2(target.y-current.y,target.x-current.x));
                    ShadowDefend.getTankProjectiles()
                            .add(new TankProjectile(TANK_PROJECTILE_IMAGE,current,target,EFFECT_RADIUS,SPEED,DAMAGE));
                    lastFire = System.currentTimeMillis();
                }
            }
        }
        super.update(input);
    }
}
