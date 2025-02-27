package lists;

import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.List;

/**
 * A regular slicer.
 */
public class Slicer extends Sprite {

    private static double speed;
    private final List<Point> polyline;
    private int targetPointIndex;
    private boolean finished;
    private boolean reachedEnd;
    private int penalty;
    private int health;
    private boolean dead;
    private int reward;
    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public Slicer(List<Point> polyline,String imageFile,
                  double speed, int penalty, int health, int reward) {
        super(polyline.get(0), imageFile);
        this.polyline = polyline;
        this.targetPointIndex = 1;
        this.finished = false;
        this.speed = speed;
        this.health = health;
        this.reachedEnd = false;
        this.penalty = penalty;
        this.dead = false;
        this.reward = reward;
    }

    /**
     *
     * @param targetrPointIndex Next target on polyLine
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     * @param imageFile Slicer's image file
     * @param speed Slicer's speed
     * @param penalty Penalty associated with slicer
     * @param health Slicer's initial health
     * @param reward Reward associated with slicer
     */
    public Slicer(int targetrPointIndex, List<Point> polyline,
                  String imageFile, double speed, int penalty, int health, int reward) {
        super(polyline.get(targetrPointIndex), imageFile);
        this.polyline = polyline;
        this.targetPointIndex = targetrPointIndex+2;
        this.finished = false;
        this.speed = speed;
        this.health = health;
        this.reachedEnd = false;
        this.penalty = penalty;
        this.dead = false;
        this.reward = reward;
    }

    /**
     *
     * @param damage Damage to be inflicted
     */
    public void inflictDamage(int damage){
        health -= damage;
        if (health<=0){
            dead = true;
        }
    }

    /**
     *
     * @return If slicer is dead
     */
    public boolean isDead(){
        return dead;
    }

    /**
     *
     * @return Reward associated with Slicer
     */
    public int getReward(){
        return reward;
    }

    /**
     * Updates the current state of the slicer. The slicer moves towards its next target point in
     * the polyline at its specified movement rate.
     */
    @Override
    public void update(Input input) {
        if (finished) {
            return;
        }
        // Obtain where we currently are, and where we want to be
        Point currentPoint = getCenter();
        Point targetPoint = polyline.get(targetPointIndex);
        // Convert them to vectors to perform some very basic vector math
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // Distance we are (in pixels) away from our target point
        double magnitude = distance.length();
        // Check if we are close to the target point
        if (magnitude < speed * ShadowDefend.getTimescale()) {
            // Check if we have reached the end
            if (targetPointIndex == polyline.size() - 1) {
                finished = true;
                reachedEnd = true;
                return;
            } else {
                // Make our focus the next point in the polyline
                targetPointIndex += 1;
            }
        }
        // Move towards the target point
        // We do this by getting a unit vector in the direction of our target, and multiplying it
        // by the speed of the slicer (accounting for the timescale)
        super.move(distance.normalised().mul(speed * ShadowDefend.getTimescale()));
        // Update current rotation angle to face target point
        super.setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.x));
        super.update(input);
    }

    /**
     *
     * @return if slicer is finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     *
     * @param val set value for finish
     */
    public void setFinished(boolean val){
        this.finished = val;
    }

    /**
     *
     * @return If slicer reached end
     */
    public boolean didReachedEnd(){
        return reachedEnd;
    }

    /**
     *
     * @return Next target on polyLine
     */
    public int getTargetPointIndex(){
        return targetPointIndex;
    }

    /**
     *
     * @return Penalty associated with slicer
     */
    public int getPenalty(){
        return penalty;
    }
}
