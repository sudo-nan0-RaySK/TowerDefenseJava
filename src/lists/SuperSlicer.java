package lists;

import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.List;

public class SuperSlicer extends Slicer {
    private static final String IMAGE_FILE = "res/images/superslicer.png";
    private static final double SPEED = 3.0/4.0;
    private static final int REWARD = 15;
    private static final int PENALTY = 2;
    private static final int HEALTH = 1;
    private int currentHealth;
    /**
     * Creates a new Sprite (game entity)
     *
     * @param polyLine    The list of points on polyLine
     */

    public SuperSlicer(List<Point> polyLine){
        super(polyLine,IMAGE_FILE,SPEED,PENALTY,HEALTH,REWARD);
        this.currentHealth = 1;
    }

    /**
     *
     * @param targetPoint Target point
     * @param polyLine The list of points on polyLine
     */
    public SuperSlicer(int targetPoint,List<Point> polyLine){
        super(targetPoint,polyLine,IMAGE_FILE,SPEED,PENALTY,HEALTH,REWARD);
        this.currentHealth = 1;
    }

    /**
     * Check if slicer was attacked with projectile
     */
    public void checkHitByTankProjectile(){
        for(TankProjectile projectile : ShadowDefend.getTankProjectiles()) {
            if (this.getRect().intersects(projectile.getCenter())) {
                this.inflictDamage(projectile.getDamage());
                projectile.setFinished();
            }
        }
    }

    /**
     *
     * @param input User input
     */
    @Override
    public void update(Input input){
        if(this.isFinished()){
            return;
        }
        checkHitByTankProjectile();
        if(isDead()){
            this.setFinished(true);
            ShadowDefend.addMoney(REWARD);
            return;
        }
        super.update(input);
    }

    /**
     *
     * @return Penalty associated with slicer
     */
    public int getPenalty(){
        return PENALTY;
    }
}
