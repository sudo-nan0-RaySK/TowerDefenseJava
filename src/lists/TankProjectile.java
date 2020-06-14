package lists;

import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

public class TankProjectile extends Sprite {

    private final int damage;
    private boolean finished;
    private final Point source;
    private final Point target;
    private final double effectRadius;
    private final double speed;

    /**
     *
     * @param TANK_PROJECTILE_IMAGE Image for tank projectile
     * @param source Center of launcher tank
     * @param target Locked target's center
     * @param effectRadius Reach of projectile
     * @param speed Speed of projectile
     * @param damage Damage inflict able
     */
    public TankProjectile(String TANK_PROJECTILE_IMAGE, Point source, Point target,
                          double effectRadius, double speed, int damage){
        super(source,TANK_PROJECTILE_IMAGE);
        this.finished = false;
        this.source = source;
        this.target = target;
        this.effectRadius = effectRadius;
        this.speed = speed;
        this.damage = damage;
    }

    /**
     *
     * @return If projectile went out of range
     */
    private boolean isOutOfRange(){
        Point spriteCenter = this.getCenter();
        return (Math.pow(spriteCenter.x-source.x,2.0))
                + (Math.pow(spriteCenter.y-source.y,2.0))
                >= Math.pow(effectRadius,2.0);
    }

    /**
     * @return If projectile is exhausted
     */
    public boolean isFinished(){
        return finished;
    }

    /**
     * Set projectile to be marked finished
     */
    public void setFinished(){ finished=true; }

    /**
     * @return Damage inflict able
     */
    public int getDamage(){
        return damage;
    }

    /**
     * Update sprite
     * @param input User input
     */
    @Override
    public void update(Input input){
        if(isFinished()){
            return;
        }
        if(isOutOfRange()){
            finished = true;
            return;
        }
        Point currentPoint = this.getCenter();
        Vector2 currentVector = currentPoint.asVector();
        Vector2 targetVector = target.asVector();

        Vector2 displacement = targetVector.sub(currentVector);
        if(displacement.length() < speed*ShadowDefend.getTimescale()){
            finished = true;
            return;
        }
        super.move(displacement.normalised().mul(speed*ShadowDefend.getTimescale()));
        super.setAngle(Math.atan2(target.y-currentPoint.y,target.x-targetVector.x));
        super.update(input);
    }
}
