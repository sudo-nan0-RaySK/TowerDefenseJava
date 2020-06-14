package lists;

import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.List;

public class ApexSlicer extends Slicer {
    private static final String IMAGE_FILE = "res/images/apexslicer.png";
    private static final double SPEED = 3.0/8.0;
    private int currentHealth;
    private static final int  REWARD = 150;
    private static final int PENALTY = 16;
    private static final int HEALTH = 25;
    /**
     * Creates a new Sprite (game entity)
     *
     * @param polyLine    The list of points on polyLine
     */

    public ApexSlicer(List<Point> polyLine){
        super(polyLine,IMAGE_FILE,SPEED,PENALTY,HEALTH,REWARD);
        this.currentHealth = 25;
    }

    /**
     * Check if slicer was attacked by a TankProjectile
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
     * @return Penalty associated with Slicer
     */
    public int getPenalty(){
        return PENALTY;
    }
}
