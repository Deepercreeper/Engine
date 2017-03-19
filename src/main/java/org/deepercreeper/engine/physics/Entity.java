package org.deepercreeper.engine.physics;

import org.deepercreeper.engine.display.Display;
import org.deepercreeper.engine.display.Renderable;
import org.deepercreeper.engine.geometry.acceleration.AbstractAcceleratedBox;
import org.deepercreeper.engine.geometry.box.Box;
import org.deepercreeper.engine.geometry.position.Vector;
import org.deepercreeper.engine.services.PhysicsService;
import org.deepercreeper.engine.util.Image;
import org.deepercreeper.engine.util.Updatable;

import java.util.HashSet;
import java.util.Set;

public abstract class Entity<T extends Entity<T>> extends AbstractAcceleratedBox<T> implements Updatable, Renderable {
    private final Set<Force> forces = new HashSet<>();

    private final PhysicsService physicsService;

    private int id = -1;

    private boolean solid = true;

    private double elasticity = .75;

    private double mass = 1;

    private double speed = 1;

    private boolean removed;

    private boolean onGround;

    private boolean updating;

    protected Entity(PhysicsService physicsService) {
        this.physicsService = physicsService;
    }

    protected final PhysicsService getPhysicsService() {
        return physicsService;
    }

    public final T setSolid(boolean solid) {
        assertNotUpdating("change solidity");
        this.solid = solid;
        return getThis();
    }

    public final T setMass(double mass) {
        assertNotUpdating("change mass");
        if (mass < 0) {
            throw new IllegalArgumentException("Mass has to be a non negative value");
        }
        this.mass = mass;
        return getThis();
    }

    public final T setElasticity(double elasticity) {
        assertNotUpdating("change elasticity");
        if (elasticity < 0 || elasticity > 1) {
            throw new IllegalArgumentException("Elasticity has to be a value between 0 and 1");
        }
        this.elasticity = elasticity;
        return getThis();
    }

    public final T setSpeed(double speed) {
        assertNotUpdating("change speed");
        if (speed < 0) {
            throw new IllegalArgumentException("Speed has to be a non negative value");
        }
        this.speed = speed;
        return getThis();
    }

    public final void addForce(Force force) {
        forces.add(force);
    }

    public final void removeForce(Force force) {
        forces.remove(force);
    }

    public final double getDensity() {
        return getMass() / getVolume();
    }

    public final double getMass() {
        return mass;
    }

    public final double getElasticity() {
        return elasticity;
    }

    public final double getSpeed() {
        return speed;
    }

    public final boolean isSolid() {
        return solid;
    }

    public final int getId() {
        return id;
    }

    public final Vector getDeltaVelocity(double delta) {
        return getVelocity().plus(getAcceleration().times(getSpeed() * delta));
    }

    public final Box getDeltaBox(double delta) {
        return shift(getVelocity().times(getSpeed() * delta).plus(getAcceleration().times(.5 * getSpeed() * getSpeed() * delta * delta)));
    }

    public final boolean isDeltaTouching(Entity<?> entity, double delta) {
        return getDeltaBox(delta).isTouching(entity.getDeltaBox(delta));
    }

    public final Box getDistanceBox(Vector velocity, double delta) {
        Vector distance = velocity.times(getSpeed() * delta);
        Box acceleratedExpandedBox = shift(getAcceleration().times(.5 * getSpeed() * getSpeed() * delta * delta)).getExpandedBox(distance);
        return getExpandedBox(distance).getContainment(acceleratedExpandedBox);
    }

    public final boolean isDistanceTouching(Vector distance, Entity<?> entity, Vector entityVelocity, double delta) {
        return getDistanceBox(distance, delta).isTouching(entity.getDistanceBox(entityVelocity, delta));
    }

    public final double getMaxDistanceDelta(double delta, double maxDistance) {
        if (getXAcceleration() == 0 && getYAcceleration() == 0) {
            double deltaDistance = getDistanceTo(delta);
            if (deltaDistance > maxDistance) {
                return delta * maxDistance / deltaDistance;
            }
            return delta;
        }
        double aa = getAcceleration().normSquared();
        double aaInvert = 1 / aa;
        double av = getAcceleration().times(getVelocity());
        double vv = getVelocity().normSquared();

        double leftTerm = -1.5 * av * aaInvert;
        double rightTerm = 0.5 * aaInvert * Math.sqrt(9 * av * av - 8 * vv * aa);

        double firstDelta = (leftTerm + rightTerm) / getSpeed();
        double secondDelta = (leftTerm - rightTerm) / getSpeed();

        double maxDelta = getMaxDelta(delta, maxDistance, firstDelta, secondDelta);
        if (maxDelta > 0) {
            return maxDelta;
        }
        while (getDistanceTo(delta) > maxDistance) {
            delta /= 2;
        }
        return delta;
    }

    private double getMaxDelta(double maxDelta, double maxDistance, double... possibleDeltas) {
        double delta = 0;
        double deltaDistance = 0;
        {
            double maxDeltaDistance = getDistanceTo(maxDelta);
            if (maxDeltaDistance < maxDistance) {
                delta = maxDelta;
                deltaDistance = maxDeltaDistance;
            }
        }
        for (double possibleDelta : possibleDeltas) {
            if (possibleDelta <= 0 || maxDelta < possibleDelta) {
                continue;
            }
            double possibleDeltaDistance = getDistanceTo(possibleDelta);
            if (deltaDistance < possibleDeltaDistance && possibleDeltaDistance < maxDistance) {
                delta = possibleDelta;
            }
        }
        return delta;
    }

    public final double getDistanceTo(double delta) {
        return getDeltaBox(delta).getPosition().minus(getPosition()).norm();
    }

    public final void remove() {
        removed = true;
    }

    private Vector computeForce() {
        Vector forces = new Vector();
        if (getPhysicsService() != null) {
            for (Force force : getPhysicsService().getForces()) {
                forces.add(force.of(this));
            }
        }
        for (Force force : this.forces) {
            forces.add(force.of(this));
        }
        return forces;
    }

    @Override
    public final void update(double delta) {
        assertNotUpdating("update");
        updating = true;
        updateInternal(delta);
        updating = false;
    }

    public final void updateAll(double delta) {
        move(delta);
        accelerate(delta);
        update(delta);
        updateProperties();
    }

    public final void updateProperties() {
        assertNotUpdating("update properties");
        setPosition(computePosition());
        setSize(computeSize());
        setVelocity(computeVelocity());
        setAcceleration(computeAcceleration());
        setElasticity(computeElasticity());
        setSpeed(computeSpeed());
        setMass(computeMass());
        onGround = false;
    }

    public final void move(double delta) {
        assertNotUpdating("move");
        moveBy(getVelocity().times(getSpeed() * delta).plus(getAcceleration().times(.5 * getSpeed() * getSpeed() * delta * delta)));
    }

    public final void moveXAccelerated(double delta) {
        assertNotUpdating("move");
        moveBy(getXVelocity() * getSpeed() * delta + getXAcceleration() * .5 * getSpeed() * getSpeed() * delta * delta, 0);
    }

    public final void moveYAccelerated(double delta) {
        assertNotUpdating("move");
        moveBy(0, getYVelocity() * getSpeed() * delta + getYAcceleration() * .5 * getSpeed() * getSpeed() * delta * delta);
    }

    public final void accelerate(double delta) {
        assertNotUpdating("accelerate");
        getVelocity().add(getAcceleration().times(getSpeed() * delta));
    }

    public final void accelerateX(double delta) {
        assertNotUpdating("accelerate");
        getVelocity().add(getAcceleration().getX() * getSpeed() * delta, 0);
    }

    public final void accelerateY(double delta) {
        assertNotUpdating("accelerate");
        getVelocity().add(0, getAcceleration().getY() * getSpeed() * delta);
    }

    public final void hitGround() {
        assertNotUpdating("hit the ground");
        onGround = true;
    }

    public final boolean isOnGround() {
        return onGround;
    }

    public final double getMassScaleTo(Entity<?> entity) {
        if (Double.isInfinite(getMass())) {
            return Double.isInfinite(entity.getMass()) ? 0.5 : 1;
        }
        if (Double.isInfinite(entity.getMass())) {
            return 0;
        }
        return getMass() / (getMass() + entity.getMass());
    }

    public final void init(int id) {
        assertNotUpdating("initialize");
        this.id = id;
    }

    public final void clear() {
        assertNotUpdating("clear");
        this.id = -1;
    }

    private void assertNotUpdating(String action) {
        if (updating) {
            throw new IllegalStateException("Cannot " + action + " while updating");
        }
    }

    public final boolean isRemoved() {
        return removed;
    }

    public boolean canTouch(Entity<?> entity) {
        return true;
    }

    public void collideWith(Entity<?> entity) {}

    public void updateInternal(double delta) {}

    public Vector computeAcceleration() {
        if (!Double.isFinite(getMass())) {
            return new Vector();
        }
        return computeForce().times(getMass());
    }

    public Vector computePosition() {
        return getPosition();
    }

    public Vector computeSize() {
        return getSize();
    }

    public Vector computeVelocity() {
        return getVelocity();
    }

    public double computeMass() {
        return getMass();
    }

    public double computeSpeed() {
        return getSpeed();
    }

    public double computeElasticity() {
        return getElasticity();
    }

    @Override
    public Image generateImage(double scale) {
        Image image = new Image().set(asScaledRectangle(scale));
        image.setData(Display.createFilledRectangle(image.getWidth(), image.getHeight(), 0xffffffff));
        return image;
    }

    @Override
    public final boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    protected boolean isHashCodeFinal() {
        return true;
    }

    @Override
    protected int computeHashCode() {
        return id;
    }

    @Override
    public String toString() {
        if (id == -1) {
            return "Entity";
        }
        return "Entity-" + id;
    }
}
