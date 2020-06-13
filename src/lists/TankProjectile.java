package lists;

import bagel.Input;
import bagel.util.Point;

public class TankProjectile extends Sprite {

    private static final String TANK_PROJECTILE_IMAGE = "res/images/tank_projectile.png";
    private boolean finished;
    private final Point source;
    private final Point target;
    private final double effectRadius;
    private final double speed;

    public TankProjectile(Point source, Point target,
                          double effectRadius, double speed){
        super(source,TANK_PROJECTILE_IMAGE);
        this.finished = false;
        this.source = source;
        this.target = target;
        this.effectRadius = effectRadius;
        this.speed = speed;
    }

    private boolean isOutOfRange(){
        Point spriteCenter = this.getCenter();
        return (Math.pow(spriteCenter.x-source.x,2.0))
                + (Math.pow(spriteCenter.y-source.y,2.0))
                > Math.pow(effectRadius,2.0);
    }

    @Override
    public void update(Input input){
        if(isOutOfRange()){
            finished = true;
            return;
        }
        super.update(input);
    }
}
