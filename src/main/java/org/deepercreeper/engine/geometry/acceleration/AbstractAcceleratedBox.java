package org.deepercreeper.engine.geometry.acceleration;

import org.deepercreeper.engine.geometry.position.Vector;
import org.deepercreeper.engine.geometry.velocity.AbstractVelocityBox;

public abstract class AbstractAcceleratedBox<T extends AbstractAcceleratedBox<T>> extends AbstractVelocityBox<T> {
    private final Vector acceleration = new Vector(this::updateHashCode);

    public final T setXAcceleration(double xAcceleration) {
        getAcceleration().setX(xAcceleration);
        return getThis();
    }

    public final T setYAcceleration(double yAcceleration) {
        getAcceleration().setY(yAcceleration);
        return getThis();
    }

    public final T setAcceleration(double xAcceleration, double yAcceleration) {
        getAcceleration().set(xAcceleration, yAcceleration);
        return getThis();
    }

    public final T setAcceleration(Vector acceleration) {
        setAcceleration(acceleration.getX(), acceleration.getY());
        return getThis();
    }

    public final Vector getAcceleration() {
        return acceleration;
    }

    public final double getXAcceleration() {
        return getAcceleration().getX();
    }

    public final double getYAcceleration() {
        return getAcceleration().getY();
    }
}
