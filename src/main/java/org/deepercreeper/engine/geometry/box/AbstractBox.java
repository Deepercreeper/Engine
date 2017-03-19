package org.deepercreeper.engine.geometry.box;

import org.deepercreeper.engine.geometry.position.Vector;
import org.deepercreeper.engine.geometry.rectangle.Rectangle;
import org.deepercreeper.engine.util.Generic;

public abstract class AbstractBox<T extends AbstractBox<T>> extends Generic<T> {
    private final Vector position = new Vector(this::updateCenterAndHashCode);

    private final Vector size = new Vector(this::updateCenterAndHashCode);

    private final Vector center = new Vector();

    private boolean hashCodeComputed;

    private int hashCode;

    public T setX(double x) {
        getPosition().setX(x);
        return getThis();
    }

    public T setY(double y) {
        getPosition().setY(y);
        return getThis();
    }

    public T setPosition(double x, double y) {
        getPosition().set(x, y);
        return getThis();
    }

    public T setWidth(double width) {
        getSize().setX(width);
        return getThis();
    }

    public T setHeight(double height) {
        getSize().setY(height);
        return getThis();
    }

    public T setSize(double width, double height) {
        getSize().set(width, height);
        return getThis();
    }

    public T setCenterX(double x) {
        setX(x - getWidth() * .5);
        return getThis();
    }

    public T setCenterY(double y) {
        setY(y - getHeight() * .5);
        return getThis();
    }

    public T setCenter(double x, double y) {
        setPosition(x - getWidth() * .5, y - getHeight() * .5);
        return getThis();
    }

    public final T set(AbstractBox<?> box) {
        setPosition(box.getPosition());
        setSize(box.getSize());
        return getThis();
    }

    public final T setPosition(Vector position) {
        setPosition(position.getX(), position.getY());
        return getThis();
    }

    public final T setSize(Vector size) {
        setSize(size.getX(), size.getY());
        return getThis();
    }

    public final T setCenter(Vector center) {
        setCenter(center.getX(), center.getY());
        return getThis();
    }

    public final T setMaxX(double x) {
        setX(x - getWidth());
        return getThis();
    }

    public final T setMaxY(double y) {
        setY(y - getHeight());
        return getThis();
    }

    public final T moveBy(double x, double y) {
        setPosition(getX() + x, getY() + y);
        return getThis();
    }

    public final T moveBy(Vector vector) {
        moveBy(vector.getX(), vector.getY());
        return getThis();
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getCenter() {
        return center;
    }

    public Vector getSize() {
        return size;
    }

    public final double getX() {
        return getPosition().getX();
    }

    public final double getY() {
        return getPosition().getY();
    }

    public final double getWidth() {
        return getSize().getX();
    }

    public final double getHeight() {
        return getSize().getY();
    }

    public final double getMaxX() {
        return getX() + getWidth();
    }

    public final double getMaxY() {
        return getY() + getHeight();
    }

    public final double getCenterX() {
        return getCenter().getX();
    }

    public final double getCenterY() {
        return getCenter().getY();
    }

    public final double getVolume() { return getWidth() * getHeight(); }

    public final boolean isTouching(AbstractBox<?> box) {
        return box.getX() <= getMaxX() && getX() <= box.getMaxX() && box.getY() <= getMaxY() && getY() <= box.getMaxY();
    }

    public final double getXDistanceTo(AbstractBox<?> box) {
        if (getMaxX() >= box.getX() && box.getMaxX() >= getX()) {
            return 0;
        }
        return Math.min(Math.abs(getX() - box.getMaxX()), Math.abs(getMaxX() - box.getX()));
    }

    public final double getYDistanceTo(AbstractBox<?> box) {
        if (getMaxY() >= box.getY() && box.getMaxY() >= getY()) {
            return 0;
        }
        return Math.min(Math.abs(getY() - box.getMaxY()), Math.abs(getMaxY() - box.getY()));
    }

    public final Box shiftX(double x) {
        return new Box().set(this).setX(getX() + x);
    }

    public final Box shiftY(double y) {
        return new Box().set(this).setY(getY() + y);
    }

    public final Box shift(Vector vector) {
        return new Box().setPosition(getX() + vector.getX(), getY() + vector.getY()).setSize(getSize());
    }

    public final Box getContainment(AbstractBox<?> box) {
        Box containment = new Box();
        containment.setPosition(Math.min(getX(), box.getX()), Math.min(getY(), box.getY()));
        containment.setSize(Math.max(getMaxX(), box.getMaxX()) - containment.getX(), Math.max(getMaxY(), box.getMaxY()) - containment.getY());
        return containment;
    }

    public final Box getExpandedBox(Vector distance) {
        Box expandedBox = new Box();
        expandedBox.setPosition(getX() - distance.getAbsX(), getY() - distance.getAbsY());
        expandedBox.setSize(getWidth() + 2 * distance.getAbsX(), getHeight() + 2 * distance.getAbsY());
        return expandedBox;
    }

    public final Rectangle asScaledRectangle(double scale) {
        return new Rectangle().setPosition(getPosition().times(scale).asPoint()).setSize(getSize().times(scale).asPoint());
    }

    private void updateCenterAndHashCode() {
        getCenter().set(getX() + getWidth() * .5, getY() * getHeight() * .5);
        updateHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractBox<?>) {
            AbstractBox<?> box = (AbstractBox<?>) obj;
            return getPosition().equals(box.getPosition()) && getSize().equals(box.getSize());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return hashCode;
    }

    protected final void updateHashCode() {
        if (!hashCodeComputed || !isHashCodeFinal()) {
            hashCode = computeHashCode();
            hashCodeComputed = true;
        }
    }

    protected boolean isHashCodeFinal() {
        return false;
    }

    protected int computeHashCode() {
        int hashCode = Double.hashCode(getX());
        hashCode = hashCode * 13 + Double.hashCode(getY());
        hashCode = hashCode * 13 + Double.hashCode(getWidth());
        hashCode = hashCode * 13 + Double.hashCode(getHeight());
        return hashCode;
    }

    @Override
    public String toString() {
        return "(" + getPosition() + ", " + getSize() + ")";
    }
}
