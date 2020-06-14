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

    private boolean isOutOfRange(){
        Point spriteCenter = this.getCenter();
        return (Math.pow(spriteCenter.x-source.x,2.0))
                + (Math.pow(spriteCenter.y-source.y,2.0))
                >= Math.pow(effectRadius,2.0);
    }

    public boolean isFinished(){
        return finished;
    }

    public void setFinished(){ finished=true; }

    public int getDamage(){
        return damage;
    }

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
