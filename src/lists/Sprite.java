package lists;

import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 * Represents a game entity
 */
public abstract class Sprite {

    private final Image image;
    private Rectangle rect;
    private double angle;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point    The starting point for the entity
     * @param imageSrc The image which will be rendered at the entity's point
     */
    public Sprite(Point point, String imageSrc) {
        this.image = new Image(imageSrc);
        this.rect = image.getBoundingBoxAt(point);
        this.angle = 0;
    }

    /**
     *
     * @return BoundingBox for sprite
     */
    public Rectangle getRect() {
        return new Rectangle(rect);
    }

    /**
     *
     * @param p Change position of sprite to point p
     */
    public void setPosition(Point p){
        this.rect = image.getBoundingBoxAt(p);
    }
    /**
     * Moves the Sprite by a specified delta
     *
     * @param dx The move delta vector
     */
    public void move(Vector2 dx) {
        rect.moveTo(rect.topLeft().asVector().add(dx).asPoint());
    }

    /**
     *
     * @return Center of sprite
     */
    public Point getCenter() {
        return getRect().centre();
    }

    /**
     *
     * @param angle Set sprite's angle
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Updates the Sprite. Default behaviour is to render the Sprite at its current position.
     */
    public void update(Input input) {
        image.draw(getCenter().x, getCenter().y, new DrawOptions().setRotation(angle));
    }
}