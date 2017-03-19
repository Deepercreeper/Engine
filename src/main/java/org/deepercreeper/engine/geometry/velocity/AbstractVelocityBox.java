package org.deepercreeper.engine.geometry.velocity;

import org.deepercreeper.engine.geometry.position.Vector;
import org.deepercreeper.engine.geometry.box.AbstractBox;

public abstract class AbstractVelocityBox<T extends AbstractVelocityBox<T>> extends AbstractBox<T> {
    private final Vector velocity = new Vector(this::updateHashCode);

    public T setXVelocity(double xVelocity) {
        getVelocity().setX(xVelocity);
        return getThis();
    }

    public T setYVelocity(double yVelocity) {
        getVelocity().setY(yVelocity);
        return getThis();
    }

    public T setVelocity(double xVelocity, double yVelocity) {
        getVelocity().set(xVelocity, yVelocity);
        return getThis();
    }

    public final T setVelocity(Vector velocity) {
        setVelocity(velocity.getX(), velocity.getY());
        return getThis();
    }

    public final Vector getVelocity() {
        return velocity;
    }

    public final double getXVelocity() {
        return getVelocity().getX();
    }

    public final double getYVelocity() {
        return getVelocity().getY();
    }

    @Override
    protected int computeHashCode() {
        int hashCode = super.computeHashCode();
        hashCode = hashCode * 13 + Double.hashCode(getXVelocity());
        hashCode = hashCode * 13 + Double.hashCode(getYVelocity());
        return hashCode;
    }
}
