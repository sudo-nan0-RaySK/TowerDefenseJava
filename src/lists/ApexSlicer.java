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
    /**
     * Creates a new Sprite (game entity)
     *
     * @param polyLine    The list of points on polyLine
     */

    public ApexSlicer(List<Point> polyLine){
        super(polyLine,IMAGE_FILE,SPEED,PENALTY);
        this.currentHealth = 25;
    }

    public void checkHitByTankProjectile(){
        for(TankProjectile projectile : ShadowDefend.getTankProjectiles()) {
            if (this.getRect().intersects(projectile.getCenter())) {
                this.currentHealth -= projectile.getDamage();
                projectile.setFinished();
            }
        }
    }

    public boolean isDead(){
        return this.currentHealth <= 0;
    }

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

    public int getPenalty(){
        return PENALTY;
    }
}
