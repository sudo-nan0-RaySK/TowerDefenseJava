package lists;

import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

public class Explosive extends Sprite {
    private static final String EXPLOSIVE_IMAGE = "res/images/explosive.png";
    private final double EFFECT_RADIUS = 200.0;
    private final int DAMAGE = 500;
    private final int DETONATION_DELAY = 2000;
    private long spawnTime;
    private boolean detonated;

    public Explosive(Point p){
        super(p,EXPLOSIVE_IMAGE);
        this.spawnTime = System.currentTimeMillis();
        this.detonated = false;
    }

    public boolean isDetonated() {
        return detonated;
    }

    public void setDetonated() {
        detonated = true;
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

    private void detonate(List<List<? extends Slicer>> slicerLists){
        List<Slicer> slicersInRange = new ArrayList<>();
        for(List<? extends Slicer> slicerList : slicerLists){
            for(Slicer s : slicerList){
                if(isInRange(s.getCenter())){
                    slicersInRange.add(s);
                }
            }
        }
        if(slicersInRange.isEmpty()){
            setDetonated();
            return;
        }
        for(Slicer slicer : slicersInRange){
            slicer.inflictDamage(DAMAGE);
            if(slicer.isDead()){
                ShadowDefend.addMoney(slicer.getReward());
            }
        }
    }

    public void update(Input input, List<List<? extends Slicer>> slicerLists){
        if(isDetonated()){
            return;
        }
        if(System.currentTimeMillis()-spawnTime>=DETONATION_DELAY){
            detonate(slicerLists);
        }
        super.update(input);
    }
}
