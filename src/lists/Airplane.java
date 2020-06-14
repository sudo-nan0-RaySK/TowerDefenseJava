package lists;

import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.Random;

public class Airplane extends Sprite {

    //x>=80 && y>=130 && x<999 && y<700;
    private static final String AIRPLANE_IMAGE = "res/images/airsupport.png";
    private static final double SPEED =1.0;
    private static final double X_START = 80;
    private static final double Y_START = 130;
    private static final double X_END = 999;
    private static final double Y_END = 700;
    private static final int VERTICAL = 1;
    private static final int HORIZONTAL = 0;
    private int direction;
    private boolean finished;
    private Point pos;
    private long dropDelay;
    private long lastDrop;

    /**
     *
     * @param p Point where Airplane tower was placed
     */
    public Airplane(Point p){
        super(new Point(p.x,Y_END+1),AIRPLANE_IMAGE);
        this.finished = false;
        this.direction = ShadowDefend.DIRECTION;
        this.pos = p;
        this.dropDelay =  new Random().nextInt(3000);
        this.lastDrop = 0;
        if(direction==HORIZONTAL){
            this.setPosition(new Point(X_END+1,pos.y));
        } else {
            this.setPosition(new Point(pos.x,Y_END+1));
        }
    }

    /**
     *
     * @return finished If airplane has traversed the map
     */
    public boolean isFinished(){
        return finished;
    }

    /**
     *
     * @param input User input
     */

    @Override
    public void update(Input input){
        if(isFinished()){
            return;
        }
        Point current = this.getCenter();
        if(current.x<X_END && current.y<Y_END && current.x>X_START && current.y>Y_START){
            long currTime = System.currentTimeMillis();
            if(currTime-lastDrop>dropDelay){
                ShadowDefend.getExplosives().add(new Explosive(current));
                lastDrop = currTime;
                dropDelay = new Random().nextInt(3000);
            }
        }
        Point target = direction==VERTICAL?
                new Point(current.x,current.y-1):new Point(current.x-1,current.y);
        Vector2 currVec = current.asVector();
        Vector2 targetVec = target.asVector();
        Vector2 displacement = targetVec.sub(currVec);

        if(current.x<=X_START || current.y<=Y_START){
            finished = true;
        }
        if(direction==VERTICAL){
            super.setAngle(0.0);
        } else {
            super.setAngle(-(Math.PI/2));
        }

        super.move(displacement.normalised().mul(SPEED * ShadowDefend.getTimescale()));
        super.update(input);
    }

}
